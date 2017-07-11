package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for variable byte length encoding that returns signed values.
 * @author Simon
 *
 */
public class ValueDecoderSignedByte extends ValueDecoderUnsignedByte{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    long unsigned = super.readValue(datasource, fieldIndex);
    return (unsigned >>> 1) ^ -(unsigned & 0x01);
  }

}
