package blackbox.data.predictor;

import blackbox.data.BlackboxHeader;


/**
 * Predictor that always predicts the min motor output as defined in the header.
 * @author Simon
 *
 */
public class ValuePredictorMinMotor extends AbstractValuePredictor{

  private long value = -1;
  
  /**
   * Constructs the predictor that always predicts the min motor output as defined in the header.
   * @param predictors
   */
  public ValuePredictorMinMotor(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    if(value == -1){
      value = Long.parseLong(predictors.getHeader().getEntry(BlackboxHeader.HEADER_ENTRY_MOTOR_OUTPUT).split(",")[0]);
    }
    
    if(((base >> 31) & 1) == 1)      
      base = base | 0xFFFFFFFF00000000L;
    return base + value;
  }
}
