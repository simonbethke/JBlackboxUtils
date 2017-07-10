package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for variable byte length encoding that returns unsigned values.
 * @author Simon
 *
 */
public class ValueDecoderUnsignedByte extends AbstractValueDecoder{

  /**
   * Constructs a decoder for variable byte length encoding that returns unsigned values.
   * @param dataSource
   */
  public ValueDecoderUnsignedByte(InputStream dataSource) {
    super(dataSource);
  }

  @Override
  public long readValue(int fieldIndex) {    
    long next;
    long value = 0;
    int byteCount = 0;
    
    while(true){
      next = nextByte();
      value |= (next & 0b01111111) << (7 * byteCount++);
      if(next < 128)
        return value;
    }
  }

}
