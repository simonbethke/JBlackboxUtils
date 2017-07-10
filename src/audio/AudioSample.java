package audio;

import physics.MotorArraySample;

/**
 * Constructs an AudioSample that provides mixing methods
 * @author Simon
 *
 */
public class AudioSample extends MotorArraySample{

  /**
   * Constructs the simple audio sample
   * 
   * @param floatArray the datasource
   * @param position the position in the sample stream in seconds
   */
  public AudioSample(double[] floatArray, double position) {
    super(floatArray, position);
  }
  
  /**
   * Mix all samples to one
   * @param volumes an Array of factors for the volumes
   * @return one mixed floating point sample
   */
  public double getMixedSample(double[] volumes){
    double sample = 0;
    for(int i = 0; i < getData().length; i++)
      sample += getData()[i] * volumes[i];
    
    return sample;
  }
  
  /**
   * Mix all samples to one
   * @param volumes an Array of factors for the volumes
   * @return one mixed 16bit sample
   */
  public short getMixed16BitSample(double[] volumes){
    return (short)(getMixedSample(volumes) * Short.MAX_VALUE);
  }
}
