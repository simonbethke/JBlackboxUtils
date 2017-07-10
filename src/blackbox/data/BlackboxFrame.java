package blackbox.data;

import java.util.Arrays;

import sampled.AbstractDataSample;

/**
 * Represents a blackbox frame that basically is a single sample of the blackbox.
 * @author Simon
 *
 */
public class BlackboxFrame extends AbstractDataSample<long[]>{
  
  private BlackboxFrameFormat format;
  private BlackboxFrame previousFrame;

  /**
   * Constructs a blackbox frame.
   * @param data the array holding the base data.
   * @param position the position of this frame in seconds from start
   * @param format the definition of the format of this frame
   * @param previousFrame the previous frame for incremental changes as the P-Frames are
   */
  public BlackboxFrame(long[] data, double position, BlackboxFrameFormat format, BlackboxFrame previousFrame) {
    super(data, position);
    this.format = format;
    this.previousFrame = (previousFrame != null) ? previousFrame : this;
  }
  
  /**
   * Sets the position of this frame in seconds from start
   * @param position position in seconds from start
   */
  public void setPosition(double position){
    this.position = position;
  }
  
  /**
   * Returns the frame n frames before this in the stream.
   * @param toPast number of frames to go back
   * @return a previous blackbox frame
   */
  public BlackboxFrame getPrevious(int toPast){
    if(previousFrame == null)
      return this;
    if(toPast == 0)
      return previousFrame;
    return previousFrame.getPrevious(toPast - 1);
  }

  @Override
  public AbstractDataSample<long[]> interpolateTo(
      AbstractDataSample<long[]> to, double relativePosition) {
    throw new RuntimeException("interpolateTo() has not yet been implemented for BlackboxFrame");
  }

  /**
   * Returns the format of this frame
   * @return a BlackboxFrameFormat object
   */
  public BlackboxFrameFormat getFormat(){
    return format;
  }
  
  /**
   * Check if this frame is a frame that contains all data that is logged in the full blackbox logging rate
   * @return true if the frame is a P-Frame or I-Frame
   */
  public boolean isContinuousData(){
    return format.getType() == BlackboxFrameFormat.FRAME_TYPE_PREDICTED 
        || format.getType() == BlackboxFrameFormat.FRAME_TYPE_INTRA;
  }
  
  /**
   * Stringifies the values of this frame.
   */
  public String toString(){
    return format.getType() + " " + Arrays.toString(getData());
  }
}
