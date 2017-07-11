package blackbox.data.predictor;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton manager for predictors. Use this helper to find the correct predictors and predict values
 * through this class.
 * @author Simon
 *
 */
public class ValuePredictors {
  private Map<Integer, AbstractValuePredictor> predictorMap;
  private static ValuePredictors instance;
  
  /**
   * Get the predictor manager instance.
   * @return the ValuePredictors instance.
   */
  public static ValuePredictors getInstance(){
    if(instance == null){
      instance = new ValuePredictors();
    }
    return instance;
  }
 
  
  private ValuePredictors(){
    predictorMap = new HashMap<Integer, AbstractValuePredictor>();
  }
  
  /**
   * Get the correct cached predictor by id
   * 
   * @param predictorId the id of the predictor
   * @return an AbstractValuePredictor
   */
  public AbstractValuePredictor getPredictor(int predictorId){
    if(predictorMap.get(predictorId) == null){
      predictorMap.put(predictorId, newPredictor(predictorId));
    }
    return predictorMap.get(predictorId);
  }
  
  private AbstractValuePredictor newPredictor(int predictorId){
    switch(predictorId){
      case 0:
        return new ValuePredictorZero();
      case 1:
        return new ValuePredictorLast();
      case 2:
        return new ValuePredictorLine();
      case 3:
        return new ValuePredictorAverage();
      case 4:
        return new ValuePredictorMinThrottle();
      case 5:
        return new ValuePredictorMotor();
      case 6:
        return new ValuePredictorIncrement();
      case 7:
        System.err.println("Predictor with id 7 is not implemented");
        return new ValuePredictorZero(); //HomeCoord not Implemented 
      case 8:
        return new ValuePredictor1500();
      case 9:
        return new ValuePredictorVBat();
      case 10:
        System.err.println("Predictor with id 10 is not implemented");
        return new ValuePredictorZero(); //MainFrameTime not Implemented 
      case 11:
        return new ValuePredictorMinMotor();
    }
    return null;
  }
}
