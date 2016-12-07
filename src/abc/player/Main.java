package abc.player;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import abc.sound.Music;

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
        // YOUR CODE HERE
    }

    public static void main(String[] args) {
        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        if (arguments.isEmpty()) {
            throw new RuntimeException("No abc file specified!");
        } else {
            String fileName = arguments.remove();
            File abcFile = new File(fileName);
            Music.parseMusic(abcFile);
        }
    }
}
