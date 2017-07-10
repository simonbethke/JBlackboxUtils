package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for variable byte length encoding that returns signed values.
 * @author Simon
 *
 */
public class ValueDecoderSignedByte extends ValueDecoderUnsignedByte{

  /**
   * Constructs a decoder for variable byte length encoding that returns signed values.
   * @param dataSource
   */
  public ValueDecoderSignedByte(InputStream dataSource) {
    super(dataSource);
  }

  @Override
  public long readValue(int fieldIndex) {
    long unsigned = super.readValue(fieldIndex);
    return (unsigned >>> 1) ^ -(unsigned & 0x01);
  }

}
