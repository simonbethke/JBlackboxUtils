package blackbox.data.decoder;

import java.io.InputStream;

/**
 * Decoder for tagged groups of four signed 16 bit values.
 * @author Simon
 *
 */
public class ValueDecoder4S16 extends AbstractValueDecoder{
  
  private int header;
  private int i, b;

  /**
   * Constructs a decoder for tagged groups of four signed 16 bit values.
   * @param dataSource
   */
  public ValueDecoder4S16(InputStream dataSource) {
    super(dataSource);
  }

  private long[] values;
  private int valuesIndex = 0;
  
  @Override
  public long readValue(int valueIndexInFrame) {
    valuesIndex %= 4;
    if(valuesIndex == 0){
      header = nextByte();
      resetBits();
      values = new long[4];
      
      for(i = 0; i < 4; i++){
        switch(header & 0b00000011){
          case 0:
            break;
          case 1:
            for(b = 0; b < 4; b++)
              values[i] = values[i] | (nextBit() ? 1 << b : 0);
            values[i] = signAtBit(values[i], 4);
            break;
          case 2:
            for(b = 0; b < 8; b++)
              values[i] = values[i] | (nextBit() ? 1 << b : 0);
              //values[i] = (values[i] << 1) | (nextBit() ? 1 : 0);
            values[i] = signAtBit(values[i], 8);
            break;
          case 3:
            for(b = 0; b < 16; b++)
              values[i] = values[i] | (nextBit() ? 1 << b : 0);
            values[i] = signAtBit(values[i], 16);
            break;
        }        
        header >>= 2;    
        
      }
    }
    
    return values[valuesIndex++];
  }

}
