package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;


/**
 * Predictor that always predicts the next value based on a line-function.
 * @author Simon
 *
 */
public class ValuePredictorLine extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return 2 * frame.getPrevious(1).getData()[fieldIndex] - frame.getPrevious(2).getData()[fieldIndex];
  }

}
