package blackbox.data.predictor;

import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the vbat reference from the header.
 * @author Simon
 *
 */
public class ValuePredictorVBat extends AbstractValuePredictor{

  private long value = -1;
  
  /**
   * Constructs a predictor that always predicts the vbat reference from the header.
   * @param predictors
   */
  public ValuePredictorVBat(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    if(value == -1){
      value = Long.parseLong(predictors.getHeader().getEntry(BlackboxHeader.HEADER_ENTRY_VBAT_REF));
    }
    return base + value;
  }
}
