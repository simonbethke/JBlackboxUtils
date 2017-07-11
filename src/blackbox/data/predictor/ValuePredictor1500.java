package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts 1500.
 * @author Simon
 *
 */
public class ValuePredictor1500 extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return 1500;
  }
}
