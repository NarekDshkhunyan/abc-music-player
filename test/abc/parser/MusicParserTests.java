package abc.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import abc.sound.Header;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/** This file contains a test suite on the parser used for music
 *
 */
public class MusicParserTests {

    /*
     * Testing strategies for parsers:
     *  buildHeader(tree) --> header
     *      tree contains length, doesn't contain length
     *      tree contains tempo, doesn't contain tempo
     *      tree contains meter, doesn't contain meter
     *      tree contains composer, doesn't contain composer
     *      tree contains voice field in actual header
     *      tree contains voices in body
     *      
     *  buildMusic(tree, header) --> music
     *      music is a single voice/multiple voices
     *      tree contains repeats and nth repeats
     *      tree contains tuplets
     *      tree contains chords
     *      tree contains tuplets
     *      tree contains accidentals
     *      tree contains different octaves of the same note
     *      header contains key signature besides C/Am
     */
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Tests for buildHeader
    
    // covers tree doesn't contain length, tempo, meter, composer
    // single voice
    @Test
    public void testBuildHeaderOnlyRequiredFieldsPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/sample1.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct index", "1", header.getIndex());
        assertEquals("expected correct title", "sample 1", header.getTitle());
        assertTrue("expected correct key", header.getKey().equals("C") || header.getKey().equals("Am"));
        assertEquals("expected default length", "1/8", header.getLength());
        assertEquals("expected default tempo", "1/8", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 100, header.getTempoBPM());
        assertTrue("expected correct meter", header.getMeter().equals("4/4") || header.getMeter().equals("C"));
    }
    
    // covers case where meter is present but no length
    @Test
    public void testBuildHeaderMeterPresentNoLength() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/paddy.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected default length", "1/8", header.getLength());
        assertEquals("expected default tempo", "1/8", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 200, header.getTempoBPM());
        assertEquals("expected correct meter", "6/8", header.getMeter());        
    }
    
    // covers case where length is present but Q is not specified
    @Test
    public void testBuildHeaderNoQButLPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/noQ.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected default length", "1/2", header.getLength());
        assertEquals("expected default tempo", "1/2", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 100, header.getTempoBPM());
        assertEquals("expected correct meter", "4/4", header.getMeter());                
    }
    
    // covers everything but voices being present in the header of the file
    @Test
    public void testBuildHeaderEverythingPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/piece1.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct index", "1", header.getIndex());
        assertEquals("expected correct title", "Piece No.1", header.getTitle());
        assertTrue("expected correct key", header.getKey().equals("C") || header.getKey().equals("Am"));
        assertEquals("expected default length", "1/4", header.getLength());
        assertEquals("expected default tempo", "1/4", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 140, header.getTempoBPM());
        assertTrue("expected correct meter", header.getMeter().equals("4/4") || header.getMeter().equals("C"));
        
    }
    
    // covers case where voices are put in body
    @Test
    public void testBuildHeaderVoicesInBody() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/invention.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct number of voices", 2, header.getVoices().keySet().size());
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("1"));
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("2"));        
    }
    
    // covers buildHeader voices are first stated in header
    @Test
    public void testBuildHeaderVoicesInHeader() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/fur_elise.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(new File("src/abc/parser/abcNotation.g"), AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct number of voices", 2, header.getVoices().keySet().size());
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("1"));
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("2"));
    }
}
