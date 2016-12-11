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
        Header header = Music.parseHeader(abcFile);
        System.out.println(header.toString());
        
        Music music = Music.parseMusic(abcFile);
        
        try {
            int beatsPerMinute = header.getTempoBPM(); 
            int ticksPerBeat = 192;
            int atBeat = 12;                                                        
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
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        
        /* These pieces can be played now */
        //arguments.add("sample_abc/piece1.abc"); 
        //arguments.add("sample_abc/piece2.abc");
        //arguments.add("sample_abc/sample1.abc");
        //arguments.add("sample_abc/sample2.abc");
        //arguments.add("sample_abc/sample3.abc");
        //arguments.add("sample_abc/scale.abc");
        //arguments.add("sample_abc/prelude.abc");
        //arguments.add("sample_abc/invention.abc");
        //arguments.add("sample_abc/little_night_music.abc");
        
        /* These pieces still cannot be played */
        //arguments.add("sample_abc/fur_elise.abc");
        //arguments.add("sample_abc/paddy.abc");
        //arguments.add("sample_abc/waxies_dargle.abc");
        //arguments.add("sample_abc/abc_song.abc");
        
        
        if (false) {
            throw new RuntimeException("No abc file specified!");
        } else {
            for (int i = 0; i < arguments.size(); i++) {
                String fileName = arguments.poll();
                //System.out.println(fileName);
                play(fileName);
            }     
        }
    }
}
