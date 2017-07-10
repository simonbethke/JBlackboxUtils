package init;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import physics.RotorSampler;
import audio.AudioSample;
import blackbox.BlackboxReader;

/**
 * Initializer that creates prop noise audio from blackbox data
 * @author Simon
 *
 */
public class BlackboxAsAudio {
  
  /**
   * Matrices that define how the noise of each motor is mapped to an audio channel. Typically there are
   * two channels for stereo and four motors. The sum of all motors on one channel should not break the limit of 1.0
   * to prevent clipping.
   */
  public static final double[][] DEFAULT_CHANNEL_MIX = {{0.05, 0.05, 0.25, 0.25}, {0.25, 0.25, 0.05, 0.05}};
  
  private BlackboxReader blackboxReader;
  private RotorSampler rotorSampler;
  
  private double[][] channelMix;
  private AudioFormat audioFormat;
  
  private static final String PARAMETER_NAME_INPUT_FILE = "input";
  private static final String PARAMETER_NAME_OUTPUT_FILE = "output";
  
  /**
   * Runs the audio creator with the mandatory paramter "input=&lt;blackboxfile&gt;"<br>
   * Optionally "output=&lt;wavfile&gt;" can be defined to write the result in a wave file. Else the audio is played back.
   * @param args The commandline parameters
   */
  public static void main(String[] args){
    try {
      BlackboxAsAudio b = new BlackboxAsAudio();
      boolean inputSet = false;
      String outputFile = null;
      for(String arg : args){
        if(arg.startsWith(PARAMETER_NAME_INPUT_FILE + "=")){
          String fileName = arg.substring(PARAMETER_NAME_INPUT_FILE.length() + 1);
          
          b.openFile(new File(fileName));
          b.initAudioSystem(44200, DEFAULT_CHANNEL_MIX);
          inputSet = true;
        }
        else if(arg.startsWith(PARAMETER_NAME_OUTPUT_FILE + "=")){
          outputFile = arg.substring(PARAMETER_NAME_OUTPUT_FILE.length() + 1);
        }
      }
      
      if(inputSet){
        if(outputFile == null)
          b.playAudio();
        else
          b.writeToFile(outputFile);
      }
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  /**
   * Opens the specified blackbox file and already parses the header
   * @param file A blackbox log file
   * @throws IOException 
   */
  public void openFile(File file) throws IOException{
    blackboxReader = new BlackboxReader();
    blackboxReader.openFile(file);
  }
  
  /**
   * Initializes the audio output syste
   * @param sampleRate the audio sample-rate
   * @param channelMix Matrices that define how the noise of each motor is mapped to an audio channel
   */
  public void initAudioSystem(int sampleRate, double[][] channelMix){
    audioFormat = new AudioFormat(sampleRate, 16, channelMix.length, true, true);
    rotorSampler = new RotorSampler(sampleRate, blackboxReader);
    this.channelMix = channelMix;
  }
  
  /**
   * Playes the audio on the default audio output system
   */
  public void playAudio(){
    try {
      ByteBuffer buffer = ByteBuffer.wrap(new byte[512]);
      AudioSample sample;
      SourceDataLine dataLine = AudioSystem.getSourceDataLine(audioFormat);
      dataLine.open();
      dataLine.start();
      
      sample = rotorSampler.getNextSample();
      while(sample != null){
        while(sample != null && buffer.hasRemaining()){          
          buffer.putShort(sample.getMixed16BitSample(channelMix[0]));
          buffer.putShort(sample.getMixed16BitSample(channelMix[1]));
          sample = rotorSampler.getNextSample();
        }
        dataLine.write(buffer.array(), 0, buffer.position());
        buffer.rewind();
      }
      
      dataLine.close();
    }
    catch (LineUnavailableException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Writes the audio to a new file with the given filename
   * @param fileName the name of the file to write to
   */
  public void writeToFile(String fileName){    
    ByteBuffer buffer = ByteBuffer.wrap(new byte[1024 * 1024]);
    AudioSample sample;
    ByteArrayOutputStream baos = new ByteArrayOutputStream(1024 * 1024);
    
    int sampleCount = 0;
    
    sample = rotorSampler.getNextSample();
    while(sample != null){
      while(sample != null && buffer.hasRemaining()){          
        buffer.putShort(sample.getMixed16BitSample(channelMix[0]));
        buffer.putShort(sample.getMixed16BitSample(channelMix[1]));
        sample = rotorSampler.getNextSample();
        sampleCount++;
      }
      
      baos.write(buffer.array(), 0, buffer.position());
      buffer.rewind();
    }
    
    try {
      AudioSystem.write(new AudioInputStream(new ByteArrayInputStream(baos.toByteArray()), audioFormat, sampleCount), Type.WAVE, new File(fileName));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }
}
