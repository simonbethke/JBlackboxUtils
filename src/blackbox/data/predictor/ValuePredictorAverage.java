package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;


/**
 * Predictor that always predicts the average value of the last and the value before that.
 * @author Simon
 *
 */
public class ValuePredictorAverage extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return (frame.getPrevious(1).getData()[fieldIndex] + frame.getPrevious(2).getData()[fieldIndex]) / 2;
  }
}
