package blackbox.data.predictor;


/**
 * Predictor that always predicts the last value.
 * @author Simon
 *
 */
public class ValuePredictorLast extends AbstractValuePredictor{
  
  /**
   * Constructs a predictor that always predicts the last value.
   * @param predictors
   */
  public ValuePredictorLast(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    return base + predictors.getFrame(1).getData()[predictors.getCurrentFieldIndex()];
  }

}
