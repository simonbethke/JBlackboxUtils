package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for unsigned 32bit values.
 * @author Simon
 *
 */
public class ValueDecoderUnsigned32Bit extends AbstractValueDecoder{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    long value = 0;
    for(int i = 0; i < 32 / Byte.SIZE ; i++){
      value <<= Byte.SIZE;
      value |= nextByte(datasource);
    }
    
    return value;
  }

}
