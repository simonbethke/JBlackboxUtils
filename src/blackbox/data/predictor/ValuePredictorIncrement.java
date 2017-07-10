package blackbox.data.predictor;

/**
 * Predictor that always predicts the last value incremented by interval of a predicted frame as defined in the header.
 * @author Simon
 *
 */
public class ValuePredictorIncrement extends AbstractValuePredictor{

  /**
   * Constructs a predictor that always predicts the last value incremented by interval of a predicted frame as defined in the header.
   * @param predictors
   */
  public ValuePredictorIncrement(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {    
    return base + predictors.getFrame(1).getData()[predictors.getCurrentFieldIndex()] + predictors.getHeader().getLoopsInterval();
  }

}
