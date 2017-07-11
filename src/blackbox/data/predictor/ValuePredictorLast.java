package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;


/**
 * Predictor that always predicts the last value.
 * @author Simon
 *
 */
public class ValuePredictorLast extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return frame.getPrevious(1).getData()[fieldIndex];
  }

}
