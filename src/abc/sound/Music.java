package abc.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import abc.parser.*;
import abc.parser.MusicGrammar.AbcMusicGrammar;
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

                             
                    
    // TODO: need to store key signatures in some sort of look up table
                    
    /**
     * parses abc file into Music
     * @param musicFile abc file to be parsed; must be properly formatted as defined by the abc notation
     * @return Music representing the piece in the abc file
     */
    public static Music parseMusic(File musicFile) {
        try {
            Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
            ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
            Header header = HeaderParser.buildHeader(headerTree);
            
            Parser<AbcMusicGrammar> musicParser = GrammarCompiler.compile(new File("src/abc/parser/musicNotation.g"), AbcMusicGrammar.ROOT);
            
            Music music = new Rest(0);
            for (String voice: header.getVoices().keySet()){
                ParseTree<AbcMusicGrammar> musicTree = musicParser.parse((InputStream) header.getVoices().get(voice));
                Music musicNew = MusicParser.buildMusic(musicTree, header);
                if (music == new Rest(0)){
                    music = musicNew;  
                }else{
                    music = new MultipleVoices(musicNew, music);   
                }    
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
     * @param voice the voice to add
     * @return new Music that now includes the extra voice
     */
    public Music addVoice(Music voice);
    
    /**
     * concatenates two pieces of music
     * @param music2 piece to be added to end of music1
     * @return new Music representing a sequence of this followed by music2
     */
    public Music concat(Music music2);

}
