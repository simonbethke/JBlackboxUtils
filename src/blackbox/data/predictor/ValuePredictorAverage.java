package blackbox.data.predictor;


/**
 * Predictor that always predicts the average value of the last and the value before that.
 * @author Simon
 *
 */
public class ValuePredictorAverage extends AbstractValuePredictor{
  
  /**
   * Constructs the predictor that always predicts the average value of the last and the value before that.
   * @param predictors
   */
  public ValuePredictorAverage(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    return base + (predictors.getFrame(1).getData()[predictors.getCurrentFieldIndex()]
        + predictors.getFrame(2).getData()[predictors.getCurrentFieldIndex()]) / 2;
  }
}
