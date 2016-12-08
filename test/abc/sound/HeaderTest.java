package abc.sound;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import abc.parser.AbcGrammar;
import abc.parser.MusicParser;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

public class HeaderTest {
    
    private final static File FILE1 = new File("abc_song.abc");
    
    // TODO: testing strategies
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testComposer() {
        try {
            Parser<AbcGrammar> parser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
            ParseTree<AbcGrammar> tree = parser.parse(FILE1);
            Header header = MusicParser.buildHeader(tree);
            
        } catch (UnableToParseException ex) {
            throw new IllegalArgumentException("input argument is invalid, could not be parsed: ", ex);
        } catch (IOException ex) {
            throw new RuntimeException("Could not open grammar file, this shouldn't happen: ", ex);
        }
    }

    @Test
    public void testToString() {
    
    }
    
    @Test
    public void testVoices() {
        
    }

}
