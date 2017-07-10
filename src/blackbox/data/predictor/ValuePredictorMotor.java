package blackbox.data.predictor;

import blackbox.data.BlackboxFrameFormat;

/**
 * Predictor that always predicts the value of the first motor output in this frame.
 * @author Simon
 *
 */
public class ValuePredictorMotor extends AbstractValuePredictor{

  private int fieldIndex = -1;
  
  /**
   * Constructs a predictor that always predicts the value of the first motor output in this frame.
   * @param predictors
   */
  public ValuePredictorMotor(ValuePredictors predictors) {
    super(predictors);
  }

  @Override
  public long predictValue(long base) {
    if(fieldIndex == -1){
      fieldIndex = predictors.getHeader()
          .getFieldDefinition(BlackboxFrameFormat.FRAME_TYPE_INTRA)
          .getFieldIndexByName(BlackboxFrameFormat.FRAME_FIELD_MOTOR_1);
    }
    return base + predictors.getFrame(0).getData()[fieldIndex];
  }
}
