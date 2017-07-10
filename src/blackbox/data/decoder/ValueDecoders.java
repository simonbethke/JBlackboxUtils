package blackbox.data.decoder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class for managing all value decoders.
 * @author Simon
 *
 */
public class ValueDecoders {
  
  public static final int DECODER_KEY_SIGNED_BYTE = 0;
  public static final int DECODER_KEY_UNSIGNED_BYTE = 1;
  public static final int DECODER_KEY_UNSIGNED_32BIT = 2;
  public static final int DECODER_KEY_NEG_14BIT = 3;
  public static final int DECODER_KEY_ELIAS_DELTA_UNSIGNED_32BIT = 4;
  public static final int DECODER_KEY_ELIAS_DELTA_SIGNED_32BIT = 5;
  public static final int DECODER_KEY_8SVB = 6;
  public static final int DECODER_KEY_3S32 = 7;
  public static final int DECODER_KEY_4S16 = 8;
  public static final int DECODER_KEY_NULL = 9;
  
  private InputStream dataSource;
  private Map<Integer, AbstractValueDecoder> decoderMap;
  private static ValueDecoders instance;
  
  /**
   * Get the decoder manager instance.
   * @return the ValueDecoders instance.
   */
  public static ValueDecoders getInstance(){
    return instance;
  }
  
  /**
   * Provides the context datasource to the value decoders
   * @param header the InputStream as datasource
   */
  public static void init(InputStream dataSource){
    instance = new ValueDecoders(dataSource);
  }
  
  private ValueDecoders(InputStream dataSource){
    this.dataSource = dataSource;
    decoderMap = new HashMap<Integer, AbstractValueDecoder>();
  }
  
  /**
   * Get a cached decoder for the given decoder key
   * @param decoderKey
   * @return an AbstractValueDecoder
   */
  public AbstractValueDecoder getDecoder(int decoderKey){
    if(decoderMap.get(decoderKey) == null){
      decoderMap.put(decoderKey, newDecoder(decoderKey));
    }
    return decoderMap.get(decoderKey);
  }
  
  private AbstractValueDecoder newDecoder(int decoderKey){
    switch(decoderKey){
      case DECODER_KEY_SIGNED_BYTE:
        return new ValueDecoderSignedByte(dataSource);
      case DECODER_KEY_UNSIGNED_BYTE:
        return new ValueDecoderUnsignedByte(dataSource);
      case DECODER_KEY_UNSIGNED_32BIT:
        return new ValueDecoderUnsigned32Bit(dataSource);
      case DECODER_KEY_NEG_14BIT:
        return new ValueDecoderNeg14Bit(dataSource);
      case DECODER_KEY_ELIAS_DELTA_UNSIGNED_32BIT:
        return new ValueDecoderEliasDeltaUnsigned32Bit(dataSource);
      case DECODER_KEY_ELIAS_DELTA_SIGNED_32BIT:
        return new ValueDecoderEliasDeltaSigned32Bit(dataSource);
      case DECODER_KEY_8SVB:
        return new ValueDecoder8SVB(dataSource);
      case DECODER_KEY_3S32:
        return new ValueDecoder3S32(dataSource);
      case DECODER_KEY_4S16:
        return new ValueDecoder4S16(dataSource);
      case DECODER_KEY_NULL:
        return new ValueDecoderNull(dataSource);
    }
    return null;
  }
}
