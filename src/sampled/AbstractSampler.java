package sampled;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that provides samples with a similar mechanism as an input stream
 * @author Simon
 *
 * @param <T> The type of samples
 * @param <D> The type of payload in the samples
 */
public abstract class AbstractSampler<T extends AbstractDataSample<D>, D> {
  private int sampleRate;
  private int sampleIndex = 0;
  private Map<Integer, Resampler> resampler;
  
  /**
   * Constructs a sampler with the given sample rate
   * @param sampleRate the sample rate in Hz
   */
  public AbstractSampler(int sampleRate){
    this.sampleRate = sampleRate;
  }
  
  /**
   * Gets the sample rate
   * @return the sample rate in Hz
   */
  public int getSampleRate(){
    return sampleRate;
  }
  
  /**
   * Calculate the position of the next sample
   * @return the position of the next sample in seconds from the beginning
   */
  protected double nextSamplePosition(){
    return (double)sampleIndex++ / (double)sampleRate;
  }
  
  /**
   * Read the next sample
   * @return The next sample object or null if no more samples are available
   */
  public abstract T getNextSample();
  
  /**
   * Convert the sample rate of this sampler
   * @param newSampleRate the new sample rate in Hz
   * @return A sampler object that will provide interpolated data from this sampler in the new sample rate
   */
  public AbstractSampler<T, D> resample(int newSampleRate){
    if(resampler == null)
      resampler = new HashMap<Integer, AbstractSampler<T,D>.Resampler>();
    if(resampler.get(newSampleRate) == null){
      resampler.put(newSampleRate, new Resampler(newSampleRate, this));
    }
    return resampler.get(newSampleRate);
  }
  
  /**
   * An inner class for resampling data of the outter sampler
   * @author Simon
   *
   */
  private class Resampler extends AbstractSampler<T, D>{
    private AbstractSampler<T, D> inputSampler;    
    private int nextInputSampleIndex = 0;    
    private T lastInputSample = null;
    private T nextInputSample = null;
    
    /**
     * Constructs a resampler
     * @param sampleRate the new sample rate in Hz
     * @param inputSampler the sampler to read data from
     */
    public Resampler(int sampleRate, AbstractSampler<T, D> inputSampler) {
      super(sampleRate);
      this.inputSampler = inputSampler;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getNextSample() {
      if(lastInputSample == null) lastInputSample = readInputSample();
      double nextOutputSamplePosition = nextSamplePosition();
      if(nextInputSample == null) nextInputSample = readInputSample();
      while(nextInputSample != null && nextInputSample.getPosition() <= nextOutputSamplePosition){
        lastInputSample = nextInputSample;
        nextInputSample = readInputSample();        
      }
      if(nextInputSample != null){
        double base = lastInputSample.getPosition();      
        return (T) nextInputSample.interpolateTo(lastInputSample, (nextOutputSamplePosition - base) / (nextInputSample.getPosition() - base));
      }
      
      return null;
    }
    
    /**
     * Read sample from input and increment the number of read samples
     * @return A sample object
     */
    private T readInputSample(){
      nextInputSampleIndex++;
      return inputSampler.getNextSample();
    }
  }

}
