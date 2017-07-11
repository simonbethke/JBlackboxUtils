package blackbox.data.predictor;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxFrameFormat;
import blackbox.data.BlackboxHeader;

/**
 * Predictor that always predicts the value of the first motor output in this frame.
 * @author Simon
 *
 */
public class ValuePredictorMotor extends AbstractValuePredictor{

  private int motorIndex = -1;

  @Override
  public long predictValue(BlackboxFrame frame, int fieldIndex,
      BlackboxHeader header) {
    if(motorIndex == -1){
      motorIndex = header
          .getFieldDefinition(BlackboxFrameFormat.FRAME_TYPE_INTRA)
          .getFieldIndexByName(BlackboxFrameFormat.FRAME_FIELD_MOTOR_1);
    }
    return frame.getData()[motorIndex];
  }
}
