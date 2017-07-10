package physics;

/**
 * Represents a simple motor to simulate the constraints of it
 * @author Simon
 *
 */
public class Motor {
  private double maxRPM;
  private Propeller propeller;
  private double throttle;
  
  /**
   * Constructs a default motor with a max RPM of 32000 and a default propeller
   */
  public Motor(){
    this(32000, new Propeller());
  }
  
  /**
   * 
   * Constructs a motor 
   * @param maxRPM the maximal speed of this motor propeller combination
   * @param propeller the propeller that is mounted to this motor
   */
  public Motor(double maxRPM, Propeller propeller){
    this.maxRPM = maxRPM;
    this.propeller = propeller;
  }
  
  /**
   * As this motor is controlled sample based, this methods will spin the motor for the given duration at the given percent
   * of its maximum speed
   * 
   * @param throttle percent of maximum speed (0.0 - 1.0)
   * @param duration duration in seconds
   */
  public void applyThrottle(double throttle, double duration){
    //TODO: implement Damping    
    propeller.rotate(throttle * maxRPM / 60d * duration);
    this.throttle = throttle;
  }
  
  /**
   * Generate an audio sample between -1.0 and 1.0 calculated from the propeller and with the current throttle
   * @return the audio sample
   */
  public double nextSample(){
    return (0.5 + throttle * 0.5) * propeller.sampleForRotation();
  }
  
  /**
   * Get the mounted propeller
   * @return a Propeller object
   */
  public Propeller getPropeller(){
    return propeller;
  }
}
