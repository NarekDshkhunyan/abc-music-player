package abc.player;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
        Music music = Music.parseMusic(abcFile);
        System.out.println(music.toString());
        try {
            SequencePlayer player = new SequencePlayer(140, 192); 
            music.play(player, 12);
            player.play();
        } catch (MidiUnavailableException mue) {
            throw new RuntimeException("Could not initialize MIDI");
        } catch (InvalidMidiDataException imd) {
            throw new RuntimeException("MidiData is invalid");
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
