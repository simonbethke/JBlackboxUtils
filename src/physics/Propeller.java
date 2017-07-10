package physics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Represents a simulated propeller and contains the audible variables
 * @author Simon
 *
 */
public class Propeller {
  private int blades = 3;
  private Map<Double, Double> harmonics;
  private double rotation = 0;
  
  /**
   * Constructs a default propeller
   */
  public Propeller(){
    harmonics = new HashMap<Double, Double>();
    initDefault();
  }
  
  /**
   * Removes all additional harmonic frequencies
   */
  public void resetHarmonics(){
    harmonics.clear();
    addHarmonic(1, 1);
  }
  
  /**
   * Sets default harmonic frequencies
   */
  public void initDefault(){
    resetHarmonics();
    generateHarmonics(0.9, 4, false);
    addHarmonic(0.33333, 1);
  }
  
  /**
   * Generates and adds a range of harmonic frequencies
   * 
   * @param damping a factor for reducing the power of them based on the level
   * @param count number of harmonic frequencies
   * @param sub if true sub-harmonics are generated instead
   */
  public void generateHarmonics(double damping, double count, boolean sub){    
    for(double i = 2; i < 2 + count; i++){
      addHarmonic(sub ? 1 / i : i, Math.pow(damping, i - 1));
    }
  }
  
  /**
   * Adds a single harmonic frequncy
   * 
   * @param relativeFrequency factor to the base frequency
   * @param relativePower factor to the power of the base frequency
   */
  public void addHarmonic(double relativeFrequency, double relativePower){
    harmonics.put(relativeFrequency, relativePower);
  }

  /**
   * Set how many blades this propeller shall have
   * @param blades number of blades
   */
  public void setBlades(int blades) {
    this.blades = blades;
  }
  
  /**
   * Generate an audio sample between -1.0 and 1.0 calculated from all harmonic frequencies and the current
   * rotation angle of the propeller
   * @return the audio sample
   */
  public double sampleForRotation(){
    double sample = 0;
    double fullPower = 0;
    for(Entry<Double, Double> harmonic : harmonics.entrySet()){
      sample += Math.sin(rotation * harmonic.getKey() * blades) * harmonic.getValue();
      fullPower += harmonic.getValue();
    }
    
    sample /= fullPower;
    
    return sample;
  }
  
  /**
   * Rotate the propeller
   * @param rounds angle of rotation. 1.0 is a full rotation
   */
  public void rotate(double rounds){
    rotation += rounds * 2 * Math.PI;
  }
}
