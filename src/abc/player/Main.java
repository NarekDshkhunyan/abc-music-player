package abc.player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
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
        
        Music music = Music.parseMusic(header);
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

    public static void main(String[] args) { 
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        arguments.add("sample_abc/piece1.abc");
        //arguments.add("sample_abc/piece2.abc");
        
        if (arguments.size() == 0) {
            throw new RuntimeException("No abc file specified!");
        } else {
            for (int i = 0; i < arguments.size(); i++) {
                String fileName = arguments.poll();
                play(fileName);
            }     
        }
    }
}
