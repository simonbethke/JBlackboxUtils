package propsynth.physics;

import blackbox.BlackboxInterpreter;
import blackbox.BlackboxReader;
import blackbox.BlackboxSampler;
import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxFrameFormat;
import sampled.AbstractSampler;

/**
 * Sampler that samples the speeds of all motors
 * @author Simon
 *
 */
public class BlackboxMotorSampler extends AbstractSampler<MotorArraySample, double[]>{

  private BlackboxInterpreter interpreter;
  private BlackboxSampler sampler;
  private boolean initialized = false;
  private int motorCount;
  private int firstMotorIndex;
  private BlackboxFrame frame;
  private double[] sampleData;
  
  /**
   * Constructs a motor sampler based on the blackbox reader
   * @param reader the source reader
   */
  public BlackboxMotorSampler(BlackboxReader reader) {
    super(reader.sampler().getSampleRate());
    this.sampler = reader.sampler();
    this.interpreter = reader.getInterpreter();
  }

  @Override
  public MotorArraySample getNextSample() {
    frame = sampler.getNextSample();
    if(frame == null)
      return null;
    
    if(!initialized){
      firstMotorIndex = frame.getFormat().getFieldIndexByName(BlackboxFrameFormat.FRAME_FIELD_MOTOR_1);
      motorCount = frame.getFormat().countMotors();
      sampleData = new double[motorCount];
      initialized = true;
    }
    
    for(int i = 0; i < motorCount; i++)
      sampleData[i] = interpreter.normalizeMotorOutput(frame.getData()[i + firstMotorIndex]);
    
    return new MotorArraySample(sampleData, frame.getPosition());
  }
}
