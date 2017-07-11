package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Base class for all value predictors
 * @author Simon
 *
 */
public abstract class AbstractValuePredictor{
    
  protected ValuePredictors predictors;
  
  /**
   * Predicts the value in this frame
   * @param frame The current frame to predict the value for
   * @param fieldIndex The index of the field to predict the value for
   * @param header The blackbox header
   * @return the predicted value
   */
  public abstract long predictValue(BlackboxFrame frame, int fieldIndex, BlackboxHeader header);
}
