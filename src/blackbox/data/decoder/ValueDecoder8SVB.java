package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for tagged groups of 8 signed variable bytelength values.
 * @author Simon
 *
 */
public class ValueDecoder8SVB extends AbstractValueDecoder{

  private ValueDecoderSignedByte internalDecoder;
  
  /**
   * Constructs a decoder for tagged groups of 8 signed variable bytelength values.
   * @param dataSource
   */
  public ValueDecoder8SVB(InputStream dataSource) {
    super(dataSource);
    internalDecoder = new ValueDecoderSignedByte(dataSource);
  }

  private long[] values;
  private int valuesIndex = 0;
  private int lastFieldIndex = -2;
  
  @Override
  public long readValue(int fieldIndex) {
    if(valuesIndex == 0 || lastFieldIndex + 1 != fieldIndex){
      valuesIndex = 0;
      values = new long[Byte.SIZE];
      resetBits();
      for(int i = 0; i < Byte.SIZE; i++)
        if(nextBit())
          values[i] = internalDecoder.readValue(fieldIndex);
    }
    
    lastFieldIndex = fieldIndex;
    
    return values[valuesIndex++];
  }

}
