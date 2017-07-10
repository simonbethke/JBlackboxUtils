package blackbox.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for holding all header information of a blackbox log
 * @author Simon
 *
 */
public class BlackboxHeader {
  /**
   * Entry name for minimum throttle logged
   */
  public static final String HEADER_ENTRY_MIN_THROTTLE = "minthrottle";
  
  /**
   * Entry name for maximum throttle logged
   */
  public static final String HEADER_ENTRY_MAX_THROTTLE = "maxthrottle";
  
  /**
   * Entry name for the interval P-Frames are logged in relation to the looptime.
   * The value is logged as fraction with numerator and denominator separated by a forward slash.
   */
  public static final String HEADER_ENTRY_INTERVAL_P = "P interval";
  
  /**
   * Entry name for the interval I-Frames are logged in relation to the looptime.
   * The value is logged as fraction with numerator and denominator separated by a forward slash.
   */
  public static final String HEADER_ENTRY_INTERVAL_I = "I interval";
  
  /**
   * Entry name for the time one internal loop-cycle takes in microseconds. Note that frames are often logged only at a
   * fraction of this speed. 
   */
  public static final String HEADER_ENTRY_LOOPTIME = "looptime";
  
  /**
   * Entry name for the battery voltage at the time of arming
   */
  public static final String HEADER_ENTRY_VBAT_REF = "vbatref";
  
  /**
   * Entry name for the minimum and maximum motor outputs. The values are seperated by a comma.
   */
  public static final String HEADER_ENTRY_MOTOR_OUTPUT = "motorOutput";
  
  /**
   * Entry name for the percentage of the motor-range that the propellers rotated during idling. For a better precision
   * this value has to get divided by 100 to get the actual percentage value. 
   */
  public static final String HEADER_ENTRY_IDLE_OFFSET = "digitalIdleOffset";
  
  /**
   * Entry name for the last entry of the blackbox header.
   */
  public static final String HEADER_ENTRY_FEATURES = "features";
  
  
  private int loopsInterval = -1;
  private Map<String, String> entries;
  private Map<Character, BlackboxFrameFormat> fieldDefinitions;
  private int motorCount = -1;
  
  /**
   * Constructs a new blackbox header object
   */
  public BlackboxHeader(){
    entries = new HashMap<String, String>();
    fieldDefinitions = new HashMap<Character, BlackboxFrameFormat>();
  }
  
  /**
   * Store a header entry
   * @param key Header entry key
   * @param value Header entry payload
   */
  public void put(String key, String value){
    entries.put(key, value);
  }
  
  /**
   * Get a raw header entry
   * @param key the key of the header entry to get
   * @return the raw header entry
   */
  public String getEntry(String key){
    return entries.get(key).trim();
  }
  
  /**
   * Get a header entry parsed as long value
   * @param key the key of the header entry to get
   * @return the header entry parsed as long value
   */
  public long getEntryAsLong(String key){
    return Long.parseLong(getEntry(key));
  }
  
  /**
   * Get the frame format holding the field defintions for a frame of the given type
   * @param type the identifier of the frame format
   * @return a BlackboxFrameFormat object
   */
  public BlackboxFrameFormat getFieldDefinition(char type){
    if(fieldDefinitions.get(type) == null){
      fieldDefinitions.put(type, generateFieldDefinition(type));      
    }
    return fieldDefinitions.get(type);
  }
  
  private BlackboxFrameFormat generateFieldDefinition(char type){
    BlackboxFrameFormat definition = (type == BlackboxFrameFormat.FRAME_TYPE_PREDICTED) ? 
        new BlackboxFrameFormat(type, getFieldDefinition(BlackboxFrameFormat.FRAME_TYPE_INTRA)) : new BlackboxFrameFormat(type);
            
    String keyPrefix = "Field " + type + " ";
    for(Map.Entry<String, String> headerEntry : entries.entrySet()){
      if(headerEntry.getKey().startsWith(keyPrefix)){
        String key = headerEntry.getKey().substring(keyPrefix.length());
        definition.put(key, headerEntry.getValue());
      }
    }
    
    return definition;
  }
  
  /**
   * Get the loop interval the is actually logged. For example every 2nd loop.
   * @return the number of loops that pass for one logged frame
   */
  public int getLoopsInterval(){
    if(loopsInterval == -1){
      loopsInterval = Integer.parseInt(getEntry(HEADER_ENTRY_INTERVAL_P).split("/")[1]);
    }
    return loopsInterval;
  }
  
  /**
   * Calculates the sample rate of this blackbox log
   * @return the sample rate in Hz
   */
  public int getSampleRate(){
    String[] interval = getEntry(HEADER_ENTRY_INTERVAL_P).split("/");
    int looptime = Integer.parseInt(getEntry(HEADER_ENTRY_LOOPTIME));
    
    return (1000000 * Integer.parseInt(interval[0])) / (looptime * Integer.parseInt(interval[1]));
    
  }
  
  /**
   * Get the number of motors
   * @return the number of motors
   */
  public int getMotorCount(){
    if(motorCount == -1){
      motorCount = getFieldDefinition('I').countMotors();
    }
    return motorCount;
  }
}
