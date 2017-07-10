package blackbox.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import blackbox.data.decoder.AbstractValueDecoder;
import blackbox.data.decoder.ValueDecoders;

/**
 * Defines the format of a blackbox frame as it has been parsed from the blackbox header
 * @author Simon
 *
 */
public class BlackboxFrameFormat {
  
  private Map<String, String[]> entries;  
  private char type;
  private Map<String, Integer> fieldIndexByName;  

  /**
   * Identifier of an Intra-Frame
   */
  public static final char FRAME_TYPE_INTRA = 'I';
  
  /**
   * Identifier of a Predicted-Frame
   */
  public static final char FRAME_TYPE_PREDICTED = 'P';
  
  /**
   * Identifier of a Slow Frame
   */
  public static final char FRAME_TYPE_SLOW = 'S';
  
  /**
   * Identifier of an Event Frame
   */
  public static final char FRAME_TYPE_EVENT = 'E';
  
  /**
   * Blackbox Header Entry name for frame-field names
   */
  public static final String FRAME_PROPTERTY_NAME = "name";
  
  /**
   * Blackbox Header Entry name for frame-field predictor ids
   */
  public static final String FRAME_PROPTERTY_PREDICTOR = "predictor";
  
  /**
   * Blackbox Header Entry name for frame-field encoding ids
   */
  public static final String FRAME_PROPTERTY_ENCODING = "encoding";
  
  /**
   * Prefix for frame-fields that log the motor output
   */
  public static final String FRAME_FIELD_MOTOR_PREFIX = "motor";
  
  /**
   * Full name of the frame-field that log the motor output of the first motor
   */
  public static final String FRAME_FIELD_MOTOR_1 = FRAME_FIELD_MOTOR_PREFIX + "[0]";
  
  /**
   * Full name of the frame-field that logs the timestamp of the frame
   */
  public static final String FRAME_FIELD_TIMESTAMP = "time";

  private List<String> names;
  private List<AbstractValueDecoder> decoders;
  private List<Integer> predictorIds;
  private BlackboxFrameFormat parentFormat;
  
  /**
   * Constructs a blackbox frame format definition for the given type
   * @param type Identifier for the frame type
   */
  public BlackboxFrameFormat(char type){
    this(type, null);
  }
  
  /**
   * Constructs a blackbox frame format definition for the given type based on
   * a parent frame format.
   * @param type Identifier for the frame type
   * @param parentFormat the frame format to inherit from
   */
  public BlackboxFrameFormat(char type, BlackboxFrameFormat parentFormat){
    this.type = type;
    this.parentFormat = parentFormat;
    entries = new HashMap<String, String[]>();
  }
  
  /**
   * Add a a row of frame-field definitions
   * @param key the definition what this row defines
   * @param value a comma separated list of values for each field in this frame format
   */
  public void put(String key, String value){
    entries.put(key, value.split(","));
  }
  
  /**
   * Get the names of fields in this format
   * @return a list of names
   */
  public List<String> getNames(){
    if(names == null){
      names = Arrays.asList(get(FRAME_PROPTERTY_NAME));      
    }
    return names;
  }
  
  /**
   * Get a lazily constructed list of decoders in the order as they
   * are used in this frame format.
   * @return a List of Implementations of AbstractValueDecoder
   */
  public List<AbstractValueDecoder> getDecoders(){
    if(decoders == null){
      decoders = new ArrayList<AbstractValueDecoder>();
      for(String decoderId : get(FRAME_PROPTERTY_ENCODING))
        decoders.add(ValueDecoders.getInstance().getDecoder(Integer.parseInt(decoderId)));
    }
    return decoders;
  }
  
  /**
   * Get a lazily constructed list of predictor ids in the order as they
   * are used in this frame format.
   * @return a List of Integers
   */
  public List<Integer> getPredictorIds(){
    if(predictorIds == null){
      predictorIds = new ArrayList<Integer>();
      for(String predictorId : get(FRAME_PROPTERTY_PREDICTOR))
        predictorIds.add(Integer.parseInt(predictorId));
    }
    return predictorIds;
  }
  private String[] get(String key){
    String[] result = entries.get(key);
    if(result == null && parentFormat != null){
      return parentFormat.get(key);
    }
    
    return result;
  }
  
  /**
   * Find the index of a field in this frame format by name
   * @param name the name of the field to find
   * @return the index of the field in this frame format 
   */
  public int getFieldIndexByName(String name){
    if(fieldIndexByName == null){  
      fieldIndexByName = new HashMap<String, Integer>();
      for(int i = 0; i < getNames().size(); i++)
        fieldIndexByName.put(getNames().get(i), i);
    }
    return fieldIndexByName.get(name);
  }
  
  /**
   * Get the type of this frame
   * @return the type identifier
   */
  public char getType(){
    return type;
  }
  
  /**
   * Count and cache the number of motors defined in this frame format
   * @return the number of motors
   */
  public int countMotors(){
    int motorCount = 0;
    for(String entryKey : names){
      if(entryKey.startsWith(FRAME_FIELD_MOTOR_PREFIX))
        motorCount++;
    }
    return motorCount;
  }
}
