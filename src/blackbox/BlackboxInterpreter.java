package blackbox;

import blackbox.data.BlackboxHeader;

/**
 * Helper class for interpreting blackbox data such as motor output values
 * @author Simon
 *
 */
public class BlackboxInterpreter {
  private BlackboxHeader header;
  
  private double motorOutputDivisor = 0;
  private double motorOutputOffset = 0;
  
  /**
   * Constructs this helper class. 
   * @param header The blackbox header with all dataformat defintions
   */
  public BlackboxInterpreter(BlackboxHeader header){
    this.header = header;
  }
  
  /**
   * Takes a raw motor output value and transforms it to a normalized value
   * @param rawValue The motor output value read from the log data
   * @return a normalized value from 0.0 (not spinning) to 1.0 (full speed)
   */
  public double normalizeMotorOutput(long rawValue){
    if(motorOutputDivisor == 0){
      String[] motorOutputs = header.getEntry(BlackboxHeader.HEADER_ENTRY_MOTOR_OUTPUT).split(",");
      double idlePercent = Integer.parseInt(header.getEntry(BlackboxHeader.HEADER_ENTRY_IDLE_OFFSET));
            
      long minMotorOutput = Integer.parseInt(motorOutputs[0]);
      long maxMotorOutput = Integer.parseInt(motorOutputs[1]);      
      double outputRange = maxMotorOutput - minMotorOutput;
      
      motorOutputOffset = idlePercent / 10000;
      motorOutputDivisor = outputRange / (1 - motorOutputOffset);
    }
    return ((double)rawValue) / motorOutputDivisor + motorOutputOffset;
  }
}
