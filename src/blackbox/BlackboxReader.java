package blackbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import blackbox.data.BlackboxHeader;

/**
 * Reader that handles the blackbox file reading and provides a blackbox sampler with the
 * sampler() method.
 * 
 * @author Simon
 *
 */
public class BlackboxReader{
  
  private BlackboxHeader header;
  private BlackboxInterpreter interpreter;
  private BlackboxSampler sampler;
  private InputStream dataSource;
  
  private static final char TYPE_HEADER_KEY_VALUE_SEPARATOR = ':';
  private static final char TYPE_HEADER_LINE_SEPARATOR = '\n';
    
  /**
   * Opens the given file and immediately reads the header of it.
   * 
   * @param file The file to read from
   * @throws IOException 
   */
  public void openFile(File file) throws IOException{
    dataSource = new FileInputStream(file);
    readHeader();
  }
  
  private void readHeader() throws IOException{
    header = new BlackboxHeader();
    
    char c;
    String key, value;
    while(true){
      c = (char)dataSource.read();
      dataSource.read();
      
      c = (char)dataSource.read();
      key = "";
      value = null;
      
      while(c != TYPE_HEADER_LINE_SEPARATOR){
        if(value == null){
          if(c == TYPE_HEADER_KEY_VALUE_SEPARATOR)
            value = "";
          else
            key += c;
        }
        else
          value += c; 
        c = (char)dataSource.read();
      }
      header.put(key, value);      
      if(key.equals(BlackboxHeader.HEADER_ENTRY_FEATURES))        
        break;
    }    
  }
  
  /**
   * Provides an interpreter for the sampled data of this blackbox reader.
   * @return an Interpreter object
   */
  public BlackboxInterpreter getInterpreter(){
    if(interpreter == null){
      interpreter = new BlackboxInterpreter(header);
    }
    return interpreter;
  }
  
  /**
   * Get the number of motors logged in this blackbox log
   * @return the number of motors
   */
  public int getMotorCount(){
    return header.getMotorCount();
  }
  
  /**
   * Provides the sampler that the payload data can be read from
   * @return a BlackboxSampler
   */
  public BlackboxSampler sampler(){
    if(sampler == null){
      sampler = new BlackboxSampler(header, dataSource);
    }
    return sampler;
  }
}
