package abc.player;

import java.io.File;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import abc.sound.*;

/**
 * Main entry point of your application.
 */
public class Main {

    /**
     * Plays the input file using Java MIDI API and displays
     * header information to the standard output stream.
     * 
     * (Your code should not exit the application abnormally using
     * System.exit().)
     * 
     * @param file the name of input abc file
     */
    public static void play(String file) {
        File abcFile = new File(file);
        Header header = Music.parseHeader(abcFile);
        System.out.println(header.toString());
        
        Music music = Music.parseMusic(abcFile);
        
        try {
            int beatsPerMinute = header.getTempoBPM(); 
            int ticksPerBeat = 192;
            double atBeat = 12;                                                        
            SequencePlayer player = new SequencePlayer(beatsPerMinute, ticksPerBeat); 
            music.play(player, atBeat);
            player.play();
        
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            throw new RuntimeException("No abc file specified!");
        } else {
            for (int i = 0; i < args.length; i++) {
                String fileName = args[i];
                play(fileName);
            }     
        }
    }
}
