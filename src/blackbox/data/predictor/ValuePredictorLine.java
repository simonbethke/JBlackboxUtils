package blackbox.data.predictor;


/**
 * Predictor that always predicts the next value based on a line-function.
 * @author Simon
 *
 */
public class ValuePredictorLine extends AbstractValuePredictor{

  /**
   * Constructs a predictor that always predicts the next value based on a line-function.
   * @param predictors
   */
  public ValuePredictorLine(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    return base + 2 * predictors.getFrame(1).getData()[predictors.getCurrentFieldIndex()]
        - predictors.getFrame(2).getData()[predictors.getCurrentFieldIndex()];
  }

}
