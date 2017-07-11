package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for variable byte length encoding that returns unsigned values.
 * @author Simon
 *
 */
public class ValueDecoderUnsignedByte extends AbstractValueDecoder{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {    
    long next;
    long value = 0;
    int byteCount = 0;
    
    while(true){
      next = nextByte(datasource);
      value |= (next & 0b01111111) << (7 * byteCount++);
      if(next < 128)
        return value;
    }
  }

}
