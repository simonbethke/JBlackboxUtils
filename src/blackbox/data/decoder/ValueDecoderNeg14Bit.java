package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Special decoder for negative 14bit values
 * @author Simon
 *
 */
public class ValueDecoderNeg14Bit extends ValueDecoderUnsignedByte{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    long unsigned = super.readValue(datasource, fieldIndex);    
    long value = unsigned >>> 1;
    if((unsigned & 1) > 0)
      value *= -1;
    
    return value;
  }

}
