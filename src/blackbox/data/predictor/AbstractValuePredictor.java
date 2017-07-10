package blackbox.data.predictor;

/**
 * Base class for all value predictors
 * @author Simon
 *
 */
public abstract class AbstractValuePredictor{
    
  protected ValuePredictors predictors;
  
  /**
   * Base constructor for a value predictor
   * @param predictors The predictor provider as context
   */
  public AbstractValuePredictor(ValuePredictors predictors){
    this.predictors = predictors;
  }
  
  /**
   * Adds the prediction to the encoded value from the log
   * @param base the encoded value 
   * @return the complete decoded value
   */
  public abstract long predictValue(long base);
}
