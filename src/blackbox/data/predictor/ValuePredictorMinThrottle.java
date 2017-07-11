package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the min throttle as defined in the header.
 * @author Simon
 *
 */
public class ValuePredictorMinThrottle extends AbstractValuePredictor{ 

  private long value = -1;
  
  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {    
    if(value == -1)
      value = header.getEntryAsLong(BlackboxHeader.HEADER_ENTRY_MIN_THROTTLE);
    
    return value;
  }

}
