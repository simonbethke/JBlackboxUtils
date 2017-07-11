package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for the advanced Elias Delta variable bit-length encoding. This decoder returns signed values.
 * @author Simon
 *
 */
public class ValueDecoderEliasDeltaSigned32Bit extends ValueDecoderEliasDeltaUnsigned32Bit{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    long unsigned = super.readValue(datasource, fieldIndex);
    return (unsigned >>> 1) ^ -(unsigned & 1);
  }

}
