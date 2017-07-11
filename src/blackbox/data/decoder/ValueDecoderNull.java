package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder that reads zero with no bytes read from the source.
 * @author Simon
 *
 */
public class ValueDecoderNull extends AbstractValueDecoder{

  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    return 0;
  }

}
