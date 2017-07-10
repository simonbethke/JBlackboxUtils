package sampled;

/**
 * This class represents a container for a sample with data that can be interpolated over time
 * @author Simon
 *
 * @param <D> the format of the data
 */
public abstract class AbstractDataSample<D>{
  private D data;
  protected double position;
  
  /**
   * Constructs a sample
   * @param data the payload
   * @param position time from start of the stream in seconds
   */
  public AbstractDataSample(D data, double position){
    this.data = data;
    this.position = position;
  }

  /**
   * Get an interpolated sample that is in between this and the given one
   * @param to the other sample to interpolate this with
   * @param relativePosition The position between the samples 0.0 means 100% this, 1.0 means 100% to
   * @return an interpolated sample
   */
  public abstract AbstractDataSample<D>  interpolateTo(AbstractDataSample<D> to, double relativePosition);
  
  /**
   * Get the payload
   * @return the payload data
   */
  public D getData(){
    return data;
  }
  
  /**
   * Get the position of this sample in the stream
   * @return the position in seconds from start
   */
  public double getPosition(){
    return position;
  }
  
  /**
   * Helpermethod to interpolate beween two double values
   * @param from first value
   * @param to second value
   * @param relativePosition The position between the values 0.0 means 100% from, 1.0 means 100% to
   * @return an interpolated value
   */
  protected double interpolateTo(double from, double to, double relativePosition){
    return from * (1 - relativePosition) + to * relativePosition;
  }
}
