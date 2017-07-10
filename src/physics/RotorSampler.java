package physics;

import sampled.AbstractSampler;
import audio.AudioSample;
import blackbox.BlackboxReader;

/**
 * Sampler that reads blackbox data and uses motor and propeller objects to calculate audio samples
 * from that.
 * 
 * @author Simon
 *
 */
public class RotorSampler extends AbstractSampler<AudioSample, double[]>{
  
  private BlackboxMotorSampler motorSampler;
  private Motor[] motors;
  
  /**
   * Constructs a rotor audio sampler
   * 
   * @param sampleRate the sample rate in Hz. This should already be the target audio sample rate
   * @param blackboxReader the datasource
   */
  public RotorSampler(int sampleRate, BlackboxReader blackboxReader){
    super(sampleRate);
    this.motorSampler = new BlackboxMotorSampler(blackboxReader);
    this.motors = new Motor[blackboxReader.getMotorCount()];
  }
  
  /**
   * Get rotor for modification
   * @param index Motor index
   * @return a Motor with mounted Propeller
   */
  public Motor getRotor(int index){
    if(motors[index] == null){
      motors[index] = new Motor();
    }
    return motors[index];
  }

  @Override
  public AudioSample getNextSample() {
    double[] data = new double[motors.length];    
    
    MotorArraySample blackboxSample = motorSampler.resample(getSampleRate()).getNextSample();
    
    if(blackboxSample != null){
      for(int i = 0; i < motors.length; i++){
        getRotor(i).applyThrottle(blackboxSample.getData()[i], 1d / (double)getSampleRate());
        data[i] = getRotor(i).nextSample();
      }
      
      return new AudioSample(data, blackboxSample.getPosition());
    }
    return null;
  }
}
