package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for the advanced Elias Delta variable bit-length encoding. This decoder returns signed values.
 * @author Simon
 *
 */
public class ValueDecoderEliasDeltaSigned32Bit extends ValueDecoderEliasDeltaUnsigned32Bit{

  /**
   * Constructs a decoder for the advanced Elias Delta variable bit-length encoding. This decoder returns signed values.
   * @param dataSource
   */
  public ValueDecoderEliasDeltaSigned32Bit(InputStream dataSource) {
    super(dataSource);
  }

  @Override
  public long readValue(int fieldIndex) {
    long unsigned = super.readValue(fieldIndex);
    return (unsigned >>> 1) ^ -(unsigned & 1);
  }

}
