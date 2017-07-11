package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the vbat reference from the header.
 * @author Simon
 *
 */
public class ValuePredictorVBat extends AbstractValuePredictor{

  private long value = -1;

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    if(value == -1)
      value = Long.parseLong(header.getEntry(BlackboxHeader.HEADER_ENTRY_VBAT_REF));
    
    return value;
  }
}
