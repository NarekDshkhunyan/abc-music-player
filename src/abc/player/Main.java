package abc.player;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import abc.sound.Header;
import abc.sound.Music;
import abc.sound.SequencePlayer;

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
            int ticksPerBeat = 2;
            double atBeat = 0;                                                         // TODO placeholder
            SequencePlayer player = new SequencePlayer(beatsPerMinute, ticksPerBeat);  // TODO figure this out
            music.play(player, atBeat);
        
        } catch (MidiUnavailableException mue) {
            mue.printStackTrace();
        } catch (InvalidMidiDataException imde) {
            imde.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        arguments.add("sample_abc/piece1.abc");
        //arguments.add("sample_abc/piece2.abc");
        //arguments.add("sample_abc/sample1.abc");
        if (arguments.isEmpty()) {
            throw new RuntimeException("No abc file specified!");
        } else {
            String fileName = arguments.remove();
            play(fileName);
        }
    }
}
