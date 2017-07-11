package propsynth.physics;

import sampled.AbstractDataSample;

/**
 * A simple implementation of a sample containing an array of floating point values
 * as payload
 * @author Simon
 *
 */
public class MotorArraySample extends AbstractDataSample<double[]>{
  
  /**
   * Constructs a simple floating point sample
   * @param floatArray the payload data
   * @param position postion of this sample in the stream in seconds
   */
  public MotorArraySample(double[] floatArray, double position){
    super(floatArray, position);
  }

  @Override
  public MotorArraySample interpolateTo(
      AbstractDataSample<double[]> to, double relativePosition) {
    
    int length = getData().length;
    
    double[] floatArray = new double[length];
    for(int i = 0; i < length; i++){      
      floatArray[i] = interpolateTo(getData()[i], to.getData()[i], relativePosition);
    }
    
    double position = interpolateTo(getPosition(), to.getPosition(), relativePosition);
    
    return new MotorArraySample(floatArray, position);
  }
}
