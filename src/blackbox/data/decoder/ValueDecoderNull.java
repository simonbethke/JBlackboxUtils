package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder that reads zero with no bytes read from the source.
 * @author Simon
 *
 */
public class ValueDecoderNull extends AbstractValueDecoder{

  /**
   * Constructs a decoder that reads zero with no bytes read from the source.
   * @param dataSource
   */
  public ValueDecoderNull(InputStream dataSource) {
    super(dataSource);
  }

  @Override
  public long readValue(int fieldIndex) {
    return 0;
  }

}
