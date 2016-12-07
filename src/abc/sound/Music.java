package abc.sound;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
    
    enum AbcGrammar {ROOT, HEADER, MUSIC, NUMBER, TITLE, OTHERS, COMPOSER, DEFAULTLENGTH, METERFIELD, TEMPOFIELD, VOICE, KEYFIELD, KEY, KEYNOTE, KEYACCIDENTAL, 
                    MODEMINOR, METER, METERFRACTION, TEMPO, LINE, ELEMENT, NOTEELEMENT, NOTE, NOTEORREST, PITCH, OCTAVE, NOTELENGTH, NOTELENGTHSTRICT, ACCIDENTAL,
                    BASENOTE, REST, TUPLETELEMENT, TUPLETSPEC, MULTINOTE, BARLINE, NTHREPEAT, MIDTUNEFIELD, COMMENT, EOL, TEXT, DIGIT, ALPHABET, NEWLINE, WHITESPACE};
    
    /**
     * parses abc file into Music
     * @param musicFile abc file to be parsed; must be properly formatted as defined by the abc notation
     * @return Music representing the piece in the abc file
     */
    public static Music parseMusic(File musicFile) {
        try {
            Parser<AbcGrammar> parser = GrammarCompiler.compile(new File("src/abc/parser/Abc.g"), AbcGrammar.ROOT);
            ParseTree<AbcGrammar> tree = parser.parse(musicFile);
            return buildAST(tree);            
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
     * @param music1 first piece to be concatenated
     * @param music2 piece to be added to end of music1
     * @return new Music representing a sequence of music1 and music2
     */
    public Music concat(Music music1, Music music2);

    /**
     * builds an abstract syntax tree of an Expression from a ParseTree
     * @param tree the parse tree that is constructed according to the specified Expression grammar
     * @return an Expression that the parse tree represents
     */
    static Music buildAST(ParseTree<AbcGrammar> tree) {
        switch(tree.getName()) {
            
            case HEADER: {
                System.out.println(tree.getContents().trim());
                return new Rest(0);
            }
            
            case MUSIC: {
                List<ParseTree<AbcGrammar>> voices = tree.childrenByName(AbcGrammar.MIDTUNEFIELD);
                if (!voices.isEmpty()) {
                    
                } else {
                    
                }
            }
            
            case NOTEELEMENT: {
                List<ParseTree<AbcGrammar>> notes = tree.childrenByName(AbcGrammar.NOTE);
                List<ParseTree<AbcGrammar>> chords = tree.childrenByName(AbcGrammar.MULTINOTE);
                if (!notes.isEmpty()) {
                    return buildAST(notes.get(0));
                } else {
                    return buildAST(notes.get(0));
                }
            }
//    
//            case VARIABLE: {
//                return new Variable(tree.getContents());
//            }
//            
//            case PRIMITIVE: {
//                List<ParseTree<AbcGrammar>> vars = tree.childrenByName(AbcGrammar.VARIABLE);
//                List<ParseTree<AbcGrammar>> numbers = tree.childrenByName(AbcGrammar.NUMBER);  
//                List<ParseTree<AbcGrammar>> sum = tree.childrenByName(AbcGrammar.SUM);
//                if (!vars.isEmpty()) {
//                    return buildAST(vars.get(0));
//                } else if (!numbers.isEmpty()) {
//                    return buildAST(numbers.get(0));
//                } else {
//                    return buildAST(sum.get(0));
//                }
//            }
//            
//            case SUM: {
//                Expression result = null;
//                for(ParseTree<AbcGrammar> child : tree.childrenByName(AbcGrammar.PRODUCT)) {
//                    if (result == null) {
//                        result = buildAST(child);
//                    } else {
//                        result = new Add(result, buildAST(child));
//                    }
//                }
//                return result;
//            }
//            
//            case PRODUCT: {
//                Expression result = null;
//                for(ParseTree<AbcGrammar> child : tree.childrenByName(AbcGrammar.PRIMITIVE)) {
//                    if (result == null) {
//                        result = buildAST(child);
//                    } else {
//                        result = new Multiply(result, buildAST(child));
//                    }
//                }
//                return result;
//            }
            
            case ROOT: {
                System.out.println("Here");
                List<ParseTree<AbcGrammar>> header = tree.childrenByName(AbcGrammar.HEADER);
                List<ParseTree<AbcGrammar>> music = tree.childrenByName(AbcGrammar.MUSIC);
                buildAST(header.get(0));
                //return buildAST(music.get(0));
            }
            
            case WHITESPACE: {   
            }
            
            default: {
                throw new RuntimeException("you should never reach here");
            }
        }
    }

    
    
}
