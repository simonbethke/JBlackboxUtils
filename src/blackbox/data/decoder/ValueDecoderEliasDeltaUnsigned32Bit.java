package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for the advanced Elias Delta variable bit-length encoding. This decoder returns unsigned values.
 * @author Simon
 *
 */
public class ValueDecoderEliasDeltaUnsigned32Bit extends AbstractValueDecoder{
  
  private long[] values;
  private int valuesIndex = 0;
  
  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    if(valuesIndex % 3 == 0){
      int l = 0;
      while(!nextBit(datasource)) l++;
      
      int n = 1;     
      for(int i = 0; i < l; i++)
        n = (n << 1) + (nextBit(datasource) ? 1 : 0);
      n--;
      
      long value = 0;
      for(int i = 0; i < n; i++)
        value = (value << 1) + (nextBit(datasource) ? 1 : 0);
      
      value += Math.pow(2, n);
    }
      
    return values[valuesIndex++];
  }
}
