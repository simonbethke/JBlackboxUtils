package blackbox.data.predictor;


/**
 * Predictor that always predicts zero.
 * @author Simon
 *
 */
public class ValuePredictorZero extends AbstractValuePredictor{

  /**
   * Constructs the predictor that always predicts zero.
   * @param predictors
   */
  public ValuePredictorZero(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    return base;
  }
}
