package blackbox.data.decoder;

import java.io.InputStream;


/**
 * Decoder for tagged groups of three signed 32 bit values.
 * @author Simon
 *
 */
public class ValueDecoder3S32 extends AbstractValueDecoder{
    
  private int header;
  private int i, b, bCnt;
  

  private long[] values;
  private int valuesIndex = 0;
  
  @Override
  public long readValue(InputStream datasource, int fieldIndex) {
    valuesIndex %= 3;
    if(valuesIndex == 0){
      header = nextByte(datasource);
      values = new long[3];
      
      switch((header & 0b11000000) >> 6){
        case 0:
          values[0] = signAtBit((header >> 4) & 0b00000011, 2);
          values[1] = signAtBit((header >> 2) & 0b00000011, 2);
          values[2] = signAtBit((header >> 0) & 0b00000011, 2);
          break;
        case 1:
          values[0] = signAtBit(header & 0b00001111, 4);
          int byteValue = nextByte(datasource);
          values[1] = signAtBit((byteValue >> 4) & 0b00001111, 4);
          values[2] = signAtBit((byteValue >> 0) & 0b00001111, 4);
          break;
        case 2:
          values[0] = signAtBit(header     & 0b00111111, 6);
          values[1] = signAtBit(nextByte(datasource) & 0b00111111, 6);
          values[2] = signAtBit(nextByte(datasource) & 0b00111111, 6);
          break;
        case 3:
          for(i = 0; i < 3; i++){
            bCnt = (header >> (2 - i)) & 0b00000011;
            values[i] = 0;
            for(b = 0; b < bCnt; b++){
              values[i] <<= Byte.SIZE;
              values[i] |= nextByte(datasource);
            }
            values[i] = signAtBit(values[i], bCnt * Byte.SIZE);
          }
          break;
      }
    }

    return values[valuesIndex++];
  }

}
