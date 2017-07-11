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
  public ValueDecoder8SVB() {
    internalDecoder = new ValueDecoderSignedByte();
  }

  private long[] values;
  private int valuesIndex = 0;
  private int lastFieldIndex = -2;
  
  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    if(valuesIndex == 0 || lastFieldIndex + 1 != fieldIndex){
      valuesIndex = 0;
      values = new long[Byte.SIZE];
      resetBits();
      for(int i = 0; i < Byte.SIZE; i++)
        if(nextBit(datasource))
          values[i] = internalDecoder.readValue(datasource, fieldIndex);
    }
    
    lastFieldIndex = fieldIndex;
    
    return values[valuesIndex++];
  }

}
