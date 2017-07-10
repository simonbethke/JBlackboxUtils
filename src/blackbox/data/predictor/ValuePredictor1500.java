package blackbox.data.predictor;

/**
 * Predictor that always predicts 1500.
 * @author Simon
 *
 */
public class ValuePredictor1500 extends AbstractValuePredictor{

  /**
   * Constructs the predictor always predicting 1500.
   * @param predictors
   */
  public ValuePredictor1500(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    return base + 1500;
  }
}
