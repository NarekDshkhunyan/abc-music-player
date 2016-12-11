package abc.sound;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import abc.parser.*;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * Immutable datatype representing a piece of music specified in a given abc file 
 * @author alican, mabunass, narek
 */
public interface Music {
    // Music = Rest(duration: double) + Note(duration: double, pitch: Pitch) + MultipleVoices(voice: Music, rest: Music) + Concat(m1: Music, m2: Music)
        
     public static final String DEFAULT_METER = "4/4";
     
     public static final String DEFAULT_LENGTH = "1/8";
     
     public static final int DEFAULT_TEMPO_BPM = 100;
     
     public static final String DEFAULT_COMPOSER = "Unknown";
     
     public static final int DEFAULT_DURATION_OF_DEFAULT_NOTE = 192;
     
     
     /**
      * Extracts header information from abc file
      * @param musicFile abc file to be parsed; must be properly formatted as defined by the abc notation
      * @return Header representing the header information in the abc file
      */
     public static Header parseHeader(File musicFile) {
        try {
            Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
            ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
            Header header = HeaderParser.buildHeader(headerTree);
            return header;
            
        } catch (UnableToParseException ex) {
            throw new IllegalArgumentException("input argument is invalid, could not be parsed: ", ex);
        } catch (IOException ex) {
           throw new RuntimeException("Could not open grammar file, this shouldn't happen: ", ex);
       }
     }
                    
    /**
     * parses abc file into Music
     * @param musicFile abc file to be parsed; must be properly formatted as defined by the abc notation
     * @return Music representing the piece in the abc file
     */
    public static Music parseMusic(File musicFile) {
        try {
            Header header = parseHeader(musicFile) ;
            
            System.out.println(header);
            
            Parser<MusicGrammar> musicParser = GrammarCompiler.compile(new File("src/abc/parser/musicNotation.g"), MusicGrammar.ROOT);
            
            Music music = new Rest(0);
            Map<String, List<String>> voicesMap = header.getVoices();
            List<String> voicesLines = new ArrayList<>();
            for (List<String> lines : voicesMap.values()) {
                if (!lines.isEmpty()) {
                    voicesLines.add(String.join("", lines));
                }
            }
            for (String voiceLines : voicesLines) {
                ParseTree<MusicGrammar> musicTree = musicParser.parse(voiceLines);
                Music musicNew = MusicParser.buildMusic(musicTree, header);
                music = new MultipleVoices(musicNew, music);
            }
            return music;
            
        } catch (UnableToParseException ex) {
            throw new IllegalArgumentException("input argument is invalid, could not be parsed: ", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Could not open grammar file, this shouldn't happen: ", ex);
        }
    }
        
    /**
     * @return double corresponding to the total duration in number of beats for given piece of music
     */
    double duration();
    
    /**
     * Transpose all notes upward or downward in pitch.
     * @param semitonesUp semitones by which to transpose
     * @return for Music m, return m' such that for all notes n in m, the
     *         corresponding note n' in m' has
     *         n'.pitch() == n.pitch().transpose(semitonesUp), and m' is
     *         otherwise identical to m
     */
    public Music transpose(int semitonesUp);
    
    /**
     * Play this piece.
     * @param player player to play on
     * @param atBeat when to play
     */
    public void play(SequencePlayer player, double atBeat);
    
    /**
     * adds the specified voice to the music to be played simultaneously
     * @param voices currently existing voices
     * @param voice the voice to add
     * @return new Music that now includes the extra voice played alongside with voices
     */
    public static Music addVoice(Music voices, Music voice) {
        return new MultipleVoices(voices, voice);
    }
    
    /**
     * concatenates two pieces of music
     * @param music1 piece to be concanated with music2
     * @param music2 piece to be added to end of music1
     * @return new Music representing a sequence of this followed by music2
     */
    public static Music concat(Music music1, Music music2) {
        return new Concat(music1, music2);
    }

}
