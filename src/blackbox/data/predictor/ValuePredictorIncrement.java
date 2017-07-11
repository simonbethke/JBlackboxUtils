package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the last value incremented by interval of a predicted frame as defined in the header.
 * @author Simon
 *
 */
public class ValuePredictorIncrement extends AbstractValuePredictor{

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    return frame.getPrevious(1).getData()[fieldIndex] + header.getLoopsInterval();
  }

}
