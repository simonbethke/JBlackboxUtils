package blackbox.data.predictor;

import java.util.HashMap;
import java.util.Map;

import blackbox.data.BlackboxFrame;
import blackbox.data.BlackboxHeader;

/**
 * Singleton manager for predictors. Use this helper to find the correct predictors and predict values
 * through this class.
 * @author Simon
 *
 */
public class ValuePredictors {
  private BlackboxFrame currentFrame;
  private int currentFieldIndex;
  private BlackboxHeader header;
  private Map<Integer, AbstractValuePredictor> predictorMap;
  private static ValuePredictors instance;
  
  /**
   * Get the predictor manager instance.
   * @return the ValuePredictors instance.
   */
  public static ValuePredictors getInstance(){
    return instance;
  }
  
  /**
   * Provides the context to the value predictors
   * @param header the BlackboxHeader as context
   */
  public static void init(BlackboxHeader header){
    instance = new ValuePredictors(header);
  }
 
  
  private ValuePredictors(BlackboxHeader header){
    this.header = header;
    predictorMap = new HashMap<Integer, AbstractValuePredictor>();
  }
  
  /**
   * Get the correct cached predictor by id and make some context data available for it
   * 
   * @param predictorId the id of the predictor
   * @param forFrame the context frame
   * @param forFieldIndex the field index of the field that shall be predicted
   * @return an AbstractValuePredictor
   */
  public AbstractValuePredictor getPredictor(int predictorId, BlackboxFrame forFrame, int forFieldIndex){
    currentFrame = forFrame;
    currentFieldIndex = forFieldIndex;
    
    return getPredictor(predictorId);
  }

  private AbstractValuePredictor getPredictor(int predictorId){
    if(predictorMap.get(predictorId) == null){
      predictorMap.put(predictorId, newPredictor(predictorId));
    }
    return predictorMap.get(predictorId);
  }
  
  private AbstractValuePredictor newPredictor(int predictorId){
    switch(predictorId){
      case 0:
        return new ValuePredictorZero(this);
      case 1:
        return new ValuePredictorLast(this);
      case 2:
        return new ValuePredictorLine(this);
      case 3:
        return new ValuePredictorAverage(this);
      case 4:
        return new ValuePredictorMinThrottle(this);
      case 5:
        return new ValuePredictorMotor(this);
      case 6:
        return new ValuePredictorIncrement(this);
      case 7:
        return new ValuePredictorZero(this); //HomeCoord not Implemented 
      case 8:
        return new ValuePredictor1500(this);
      case 9:
        return new ValuePredictorVBat(this);
      case 10:
        return new ValuePredictorZero(this); //MainFrameTime not Implemented 
      case 11:
        return new ValuePredictorMinMotor(this);
    }
    return null;
  }

  /**
   * Get a frame that is n frames in the past.
   * @param toPast the number of frames to go back (0 would be the current)
   * @return a BlackboxFrame
   */
  public BlackboxFrame getFrame(int toPast) {
    if(toPast == 0)
      return currentFrame;
    return currentFrame.getPrevious(toPast - 1);
  }

  /**
   * Get the index of the current field that shall be predicted
   * @return the index
   */
  public int getCurrentFieldIndex() {
    return currentFieldIndex;
  }

  /** Get the blackbox header
   * 
   * @return a BlackboxHeader
   */
  public BlackboxHeader getHeader() {
    return header;
  }
}
