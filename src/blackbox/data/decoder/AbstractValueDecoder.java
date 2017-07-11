package blackbox.data.decoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * Base class for decoders that can decode numeric values from a binary input stream.
 * 
 * @author Simon
 *
 */
public abstract class AbstractValueDecoder{
  
  private int bitByte = 0;
  private int bitIndex = 0;
  
  /**
   * Reads and caches a byte and the provides the bits of it.
   * The bits are read LSB to MSB. After the last bit is reached,
   * a new byte is pulled and cached.<br>
   * Call resetBits() to ensure that a new byte is loaded.
   * 
   * @return true if the bit is 1
   */
  protected boolean nextBit(InputStream dataSource){
    bitIndex %= Byte.SIZE;
    if(bitIndex == 0){
      bitByte = nextByte(dataSource);
    }
    return ((bitByte >> bitIndex++) & 0x01) == 0x01;
  }
  
  /**
   * Get the complete byte that was cached for nextBit().
   * @return The cached byte the bits are read from.
   */
  protected int bitByte(){
    return bitByte;
  }
  
  /**
   * Ensure that the next nextBit() call will read and cache a
   * new byte.
   */
  protected void resetBits(){
    bitIndex = 0;
  }
  
  /**
   * Simply reads the next byte from the data source and
   * cares about the potential exception.
   * 
   * @return the next byte from the data source.
   */
  protected int nextByte(InputStream dataSource){
    try {
      return dataSource.read();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return 0;
  }
  
  /**
   * If not all bits are used and the MSB is the sign-bit, this method will
   * transform the value to a normal signed value.
   * 
   * @param value Input Bits
   * @param signBit Index of the Bit with the sign info
   * @return a signed value
   */
  protected long signAtBit(long value, int signBit){
    return (((value >> signBit) & 0x01) == 0x01) ? (value ^ (0x01 << signBit)) * -1 : value;
  }
  
  /**
   * Reads a variable number of bytes from the data source and provides the
   * decoded value.<br>
   * The provided fieldIndex may later be used to evaluate if the current value
   * belongs to a group of values that started with previous reads.
   * 
   * @param dataSource The data source to read from
   * @param fieldIndex The index of this value in the frame
   * @return the decoded value
   */
  public abstract long readValue(InputStream dataSource, int fieldIndex);
}
