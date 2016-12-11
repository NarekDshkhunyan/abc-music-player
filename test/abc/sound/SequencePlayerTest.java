package abc.sound;

import static org.junit.Assert.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import org.junit.Test;
/**
 * Tests piece1.abc and piece2.abc
 * @author narek
 * @category no_didit
 *
 */

public class SequencePlayerTest {

    // warmup #2
    
    @Test
    public void testPiece1() {
        try {
            SequencePlayer player = new SequencePlayer(140, 12);
            //Check whether to start at 0 or 1.
            player.addNote(new Pitch('C').toMidiNote(), 0, 12);
            player.addNote(new Pitch('C').toMidiNote(), 12, 12);
            player.addNote(new Pitch('C').toMidiNote(), 24, 9);
            player.addNote(new Pitch('D').toMidiNote(), 33, 3);
            player.addNote(new Pitch('E').toMidiNote(), 36, 12);
     
            player.addNote(new Pitch('E').toMidiNote(), 48, 9);
            player.addNote(new Pitch('D').toMidiNote(), 57, 3);
            player.addNote(new Pitch('E').toMidiNote(), 60, 9);
            player.addNote(new Pitch('F').toMidiNote(), 69, 3);
            player.addNote(new Pitch('G').toMidiNote(), 72, 24);

            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 96, 4);
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 100, 4);
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 104, 4);
            player.addNote(new Pitch('G').toMidiNote(), 108, 4);
            player.addNote(new Pitch('G').toMidiNote(), 112, 4);
            player.addNote(new Pitch('G').toMidiNote(), 116, 4);
            
            player.addNote(new Pitch('E').toMidiNote(), 120, 4);
            player.addNote(new Pitch('E').toMidiNote(), 124, 4);
            player.addNote(new Pitch('E').toMidiNote(), 128, 4);
            player.addNote(new Pitch('C').toMidiNote(), 132, 4);
            player.addNote(new Pitch('C').toMidiNote(), 136, 4);
            player.addNote(new Pitch('C').toMidiNote(), 140, 4);
            
            player.addNote(new Pitch('G').toMidiNote(), 144, 9);
            player.addNote(new Pitch('F').toMidiNote(), 153, 3);
            player.addNote(new Pitch('E').toMidiNote(), 156, 9);
            player.addNote(new Pitch('D').toMidiNote(), 165, 3);
            player.addNote(new Pitch('C').toMidiNote(), 168, 24);

            System.out.println(player);
            System.out.println(player.toString());

            // play!
            player.play();
            Thread.sleep(10000);
            System.exit(0);

        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
   }
    
    
    @Test
    public void testPiece1_testingtpm() {
        try {
            int tpm = 192;
            SequencePlayer player = new SequencePlayer(140, tpm);
            
            //Check whether to start at 0 or 1.
            int currentTime = 12;
            player.addNote(new Pitch('C').toMidiNote(), currentTime, tpm);
            player.addNote(new Pitch('C').toMidiNote(), currentTime+=tpm, tpm); // 12
            player.addNote(new Pitch('C').toMidiNote(), (int) (currentTime+=tpm), (int) (tpm*0.75)); // 24
            player.addNote(new Pitch('D').toMidiNote(), (int) (currentTime+=tpm*0.75), (int) (tpm*0.25)); // 33
            player.addNote(new Pitch('E').toMidiNote(), currentTime+=tpm*0.25, tpm); // 36
            
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm), (int) (tpm*0.75)); //48
            player.addNote(new Pitch('D').toMidiNote(), (int) (currentTime+=tpm*0.75), (int) (tpm*0.25));//57
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm*0.25), (int) (tpm*0.75));//60
            player.addNote(new Pitch('F').toMidiNote(), (int) (currentTime+=tpm*0.75), (int) (tpm*0.25));//69
            player.addNote(new Pitch('G').toMidiNote(), (int) (currentTime+=tpm*0.25), (int) (tpm*2));//72
            
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), (int) (currentTime+=tpm*2), (int) (tpm/3.0));//96
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//100
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//104
            player.addNote(new Pitch('G').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//108
            player.addNote(new Pitch('G').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//112
            player.addNote(new Pitch('G').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//116
            
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//120
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//124
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//128
            player.addNote(new Pitch('C').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//132
            player.addNote(new Pitch('C').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//136
            player.addNote(new Pitch('C').toMidiNote(), (int) (currentTime+=tpm/3.0), (int) (tpm/3.0));//140
            
            player.addNote(new Pitch('G').toMidiNote(), (int) (currentTime+=tpm*1.0/3.0), (int) (tpm*0.75));//144
            player.addNote(new Pitch('F').toMidiNote(), (int) (currentTime+=tpm*0.75), (int) (tpm*0.25));//153
            player.addNote(new Pitch('E').toMidiNote(), (int) (currentTime+=tpm*0.25), (int) (tpm*0.75));//156
            player.addNote(new Pitch('D').toMidiNote(), (int) (currentTime+=tpm*0.75), (int) (tpm*0.25));//165
            player.addNote(new Pitch('C').toMidiNote(), (int) (currentTime+=tpm*0.25), (int) (tpm*2));//168
            System.out.println(currentTime);
            System.out.println(player);
            System.out.println(player.toString());

            // play!
            player.play();
            Thread.sleep(10000);
            System.exit(0);

        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testPiece2() {
        try {
            SequencePlayer player = new SequencePlayer(200, 12);

            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 0, 6);
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 0, 6);
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 6, 6);
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 6, 6);
            
            // z/2
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 18, 6);
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 18, 6);
            // z/2
            
            
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 30, 6);
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 30, 6);
            
            player.addNote(new Pitch('F').transpose(1).toMidiNote(), 36, 12);
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 36, 12);
            
            
            player.addNote(new Pitch('G').toMidiNote(), 48, 12);
            player.addNote(new Pitch('B').toMidiNote(), 48, 12);
            player.addNote(new Pitch('G').transpose(12).toMidiNote(), 48, 12);
            // z
            player.addNote(new Pitch('G').toMidiNote(), 72, 12);
            // z
            
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 96, 18);
            player.addNote(new Pitch('G').toMidiNote(), 114, 6);
            // z
            player.addNote(new Pitch('E').toMidiNote(), 132, 12);

            player.addNote(new Pitch('E').toMidiNote(), 144, 6);
            player.addNote(new Pitch('A').toMidiNote(), 150, 12);
            player.addNote(new Pitch('B').toMidiNote(), 162, 12);
            player.addNote(new Pitch('B').transpose(-1).toMidiNote(), 174, 6);
            player.addNote(new Pitch('A').toMidiNote(), 180, 12);
            
            player.addNote(new Pitch('G').toMidiNote(), 192, 8);
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 200, 8);
            player.addNote(new Pitch('G').transpose(12).toMidiNote(), 208, 8);
            
            player.addNote(new Pitch('A').transpose(12).toMidiNote(), 216, 12);
            player.addNote(new Pitch('F').transpose(12).toMidiNote(), 228, 6);
            player.addNote(new Pitch('G').transpose(12).toMidiNote(), 234, 6);
            
            // z/2
            player.addNote(new Pitch('E').transpose(12).toMidiNote(), 246, 12);
            player.addNote(new Pitch('C').transpose(12).toMidiNote(), 258, 6);
            player.addNote(new Pitch('D').transpose(12).toMidiNote(), 264, 6);
            player.addNote(new Pitch('B').toMidiNote(), 270, 9);
            // z/4
            // z/4
            // z/4
            
            System.out.println(player);
            System.out.println(player.toString());

            // play!
            player.play();
            Thread.sleep(10000);
            System.exit(0);

        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
