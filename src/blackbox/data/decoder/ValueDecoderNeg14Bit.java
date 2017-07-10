package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Special decoder for negative 14bit values
 * @author Simon
 *
 */
public class ValueDecoderNeg14Bit extends ValueDecoderUnsignedByte{

  /**
   * Constructs a special decoder for negative 14bit values
   * @param dataSource
   */
  public ValueDecoderNeg14Bit(InputStream dataSource) {
    super(dataSource);
  }

  @Override
  public long readValue(int fieldIndex) {
    long unsigned = super.readValue(fieldIndex);    
    long value = unsigned >>> 1;
    if((unsigned & 1) > 0)
      value *= -1;
    
    return value;
  }

}
