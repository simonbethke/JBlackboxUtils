package blackbox.data.predictor;

import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the min throttle as defined in the header.
 * @author Simon
 *
 */
public class ValuePredictorMinThrottle extends AbstractValuePredictor{ 
  
  /**
   * Constructs the predictor that always predicts the min throttle as defined in the header.
   * @param predictors
   */
  public ValuePredictorMinThrottle(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {    
    if(((base >> 31) & 1) == 1)      
      base = base | 0xFFFFFFFF00000000L;
    
    return base + predictors.getHeader().getEntryAsLong(BlackboxHeader.HEADER_ENTRY_MIN_THROTTLE);
  }

}
