package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;


/**
 * Predictor that always predicts zero.
 * @author Simon
 *
 */
public class ValuePredictorZero extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return 0;
  }
}
