package blackbox;

import java.io.IOException;
import java.io.InputStream;

import sampled.AbstractSampler;
import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxFrameFormat;
import blackbox.data.BlackboxHeader;
import blackbox.data.decoder.ValueDecoders;
import blackbox.data.predictor.ValuePredictors;

/**
 * Represents a sampler that provides blackbox samples. These samples are called
 * frames and don't necessarily have to be sampled at the returned sample rate.
 * 
 * @author Simon
 *
 */
public class BlackboxSampler extends AbstractSampler<BlackboxFrame, long[]>{
  
  private InputStream dataSource;
  private BlackboxHeader header;
  
  private BlackboxFrame ipFrame;
  private long[] ipData;
  
  private BlackboxFrame sFrame;
  private long[] sData;
  
  private BlackboxFrameFormat eFrameFormat;
  private BlackboxFrame eFrame;
  private long[] eData;
  
  private BlackboxFrame nextFrame = null;
  private long logStartTime = -1;
  private int logTimeDataIndex;
  private double nextFramePosition = 0;
  
  private static final String END_OF_LOG_MARKER = "End of log";

  /**
   * Constructs a blackbox sampler that reads data from the provided datasource presuming that
   * the format matches the format define in the header.
   * 
   * @param header The header defining the data
   * @param dataSource The source for data to read from
   */
  public BlackboxSampler(BlackboxHeader header, InputStream dataSource) {
    super(header.getSampleRate());
    this.header = header;
    this.dataSource = dataSource;
    initialize();
  }
  
  private void initialize(){
    ipData = new long[header.getFieldDefinition(BlackboxFrameFormat.FRAME_TYPE_INTRA).getNames().size()];
    sData = new long[header.getFieldDefinition(BlackboxFrameFormat.FRAME_TYPE_SLOW).getNames().size()];
    eFrameFormat = new BlackboxFrameFormat(BlackboxFrameFormat.FRAME_TYPE_EVENT);
    eFrameFormat.put(BlackboxFrameFormat.FRAME_PROPTERTY_NAME, "event,value");
    ValueDecoders.init(dataSource);
    ValuePredictors.init(header);
  }
  
  /**
   * Prefetches the next sample to read and return the sample-postion from it.
   */
  @Override
  protected double nextSamplePosition(){
    if(nextFrame == null)
      nextFrame = getNextSample();
    
    return nextFrame.getPosition();
  }
  
  private double samplePositionFromData(long[] data, BlackboxFrameFormat format){
    if(logStartTime == -1){
      logTimeDataIndex = format.getFieldIndexByName(BlackboxFrameFormat.FRAME_FIELD_TIMESTAMP);
      logStartTime = data[logTimeDataIndex];
      return nextFramePosition;
    }
    
    nextFramePosition = (double)(data[logTimeDataIndex] - logStartTime) / 1000000;
    
    return nextFramePosition;
  }

  @Override
  public BlackboxFrame getNextSample() {
    BlackboxFrame frame = null;
    
    if(nextFrame != null){
      frame = nextFrame;
      nextFrame = null;
      return frame;
    }
    
    frame = getNextFrame();
    while(frame != null && !frame.isContinuousData())
      frame = getNextFrame();
    return frame;
  }
  
  /**
   * Reads and returns the next frame from the data source.
   *  
   * @return a BlackboxFrame
   */
  public BlackboxFrame getNextFrame(){
    try {
      char type = (char)dataSource.read();
      
      BlackboxFrameFormat format = header.getFieldDefinition(type);
      
      if(type == BlackboxFrameFormat.FRAME_TYPE_INTRA || type == BlackboxFrameFormat.FRAME_TYPE_PREDICTED) {
        ipData = new long[ipData.length];
        BlackboxFrame previousFrame = (type == BlackboxFrameFormat.FRAME_TYPE_INTRA) ? null : ipFrame;
        ipFrame = new BlackboxFrame(ipData, -1, format, previousFrame);        
        for(int i = 0; i < ipData.length; i++){
          ipData[i] = format.getDecoders().get(i).readValue(i);
          ipData[i] = ValuePredictors.getInstance().getPredictor(
              format.getPredictorIds().get(i), ipFrame, i).predictValue(ipData[i]);
        }
        ipFrame.setPosition(samplePositionFromData(ipData, format));
        return ipFrame;
      }
      else if(type == BlackboxFrameFormat.FRAME_TYPE_SLOW){
        sData = new long[sData.length];
        sFrame = new BlackboxFrame(sData, nextFramePosition, format, sFrame);        
        for(int i = 0; i < sData.length; i++){
          sData[i] = format.getDecoders().get(i).readValue(i);
          sData[i] = ValuePredictors.getInstance().getPredictor(
              format.getPredictorIds().get(i), sFrame, i).predictValue(sData[i]);          
        }        
        return sFrame;
      }
      else if(type == BlackboxFrameFormat.FRAME_TYPE_EVENT){
        eData = new long[2];
        eData[0] = dataSource.read();
        if(eData[0] == 0xff){
          byte[] endMarker = new byte[END_OF_LOG_MARKER.length() + 1];
          dataSource.read(endMarker, 0, END_OF_LOG_MARKER.length() + 1);
          if(new String(endMarker).startsWith(END_OF_LOG_MARKER)){
            return null;
          }
        }
        eData[1] = ValueDecoders.getInstance().getDecoder(
            eData[0] < 127 ? ValueDecoders.DECODER_KEY_SIGNED_BYTE : ValueDecoders.DECODER_KEY_SIGNED_BYTE).readValue(0);
        eFrame = new BlackboxFrame(eData, nextFramePosition, eFrameFormat, eFrame);
        return eFrame;
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
