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
            SequencePlayer player = new SequencePlayer(120, 2);

            player.addNote(new Pitch('C').toMidiNote(), 0, 1);
            player.addNote(new Pitch('C').toMidiNote(), 1, 1);
            //player.addNote(new Pitch('C3/4').toMidiNote, 2, 1);
            //player.addNote(new Pitch('D/4').toMidiNote(), 3, 1);
            player.addNote(new Pitch('E').toMidiNote(), 2, 1);

            System.out.println(player);
            System.out.println(player.toString());

            // play!
            player.play();
            System.exit(0);

        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }
    
    @Test
    public void testPiece2() {
        try {
            SequencePlayer player = new SequencePlayer(120, 2);

            player.addNote(new Pitch('C').toMidiNote(), 0, 1);
            player.addNote(new Pitch('D').toMidiNote(), 1, 1);
            player.addNote(new Pitch('E').toMidiNote(), 2, 1);
            player.addNote(new Pitch('F').toMidiNote(), 3, 1);

            System.out.println(player);
            System.out.println(player.toString());

            // play!
            player.play();
            System.exit(0);

        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }
}
