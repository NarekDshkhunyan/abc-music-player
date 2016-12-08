package abc.sound;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import abc.parser.AbcGrammar;
import abc.parser.MusicParser;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/**
 * This file contains tests for abc.sound.Header and relates to the construction of Header and it's methods.
 * For tests on parsing header information in abc files, look at ParserTests
 *
 */
public class HeaderTest {
    
    /*
     * Testing strategy:
     *  Since most of the methods are observers, we will use one test to check all observers
     *  toString() --> result
     *      header contains one voice, file contains multiple voices
     *  hashCode() --> result
     *      check if hashCodes are equal when two headers are equal
     *  equals() --> result
     *      result = true; result = false
     */
    
    private final String index = "1000";
    private final String title = "Test";
    private final String key = "Abm";
    private final String meter = "4/4";
    private final String length = "1/4";
    private final String tempoBaseNote = "1/8";
    private final String composer = "Unknown";
    private final int tempoBPM = 120;
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testObservers() {
        Map<String, List<String>> voices = new HashMap<>();
        voices.put("voice 1", Arrays.asList("C ","D ", "E ", "F ", "|"));
        Header header = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        assertEquals("expected correct index", index, header.getIndex());
        assertEquals("expected correct title", title, header.getTitle());
        assertEquals("expected correct key", key, header.getKey());
        assertEquals("expected correct meter", meter, header.getMeter());
        assertEquals("expected correct length", length, header.getLength());
        assertEquals("expected correct composer", composer, header.getComposer());
        assertEquals("expected correct tempo basenote", tempoBaseNote, header.getTempoBaseNote());
        assertEquals("expected correct tempo beats per minute", tempoBPM, header.getTempoBPM());
        assertEquals("expected correct number of voices", 1, voices.entrySet().size());
        assertTrue("expected correct voice", voices.containsKey("voice 1"));
        assertEquals("expected correct line associated with voice", 5, voices.get("voice 1").size());
    }

    @Test
    public void testToStringOneVoice() {
        Map<String, List<String>> voices = new HashMap<>();
        voices.put("", Arrays.asList("C ","D ", "E ", "F ", "|"));
        Header header = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        String expected = "X: 1000\nT: Test\nC: Unknown\nL: 1/4\nM: 4/4\nQ: 1/8=120\nV: \nK: Abm";
        assertEquals("expected correct string representation", expected, header.toString());
    }
    
    @Test
    public void testToStringMultipleVoices() {
        Map<String, List<String>> voices = new HashMap<>();
        voices.put("voice 1", Arrays.asList("C ","D ", "E ", "F ", "|"));
        voices.put("voice 2", Arrays.asList("C ","D ", "E ", "F ", "|"));
        Header header = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        String possible_value = "X: 1000\nT: Test\nC: Unknown\nL: 1/4\nM: 4/4\nQ: 1/8=120\nV: voice 1\nV: voice 2\nK: Abm";
        String possible_value2 = "X: 1000\nT: Test\nC: Unknown\nL: 1/4\nM: 4/4\nQ: 1/8=120\nV: voice 2\nV: voice 1\nK: Abm";
        assertTrue("expected correct string representation", header.toString().equals(possible_value) || header.toString().equals(possible_value2));
    }
    
    @Test
    public void testEqualsSameHeader() {
        Map<String, List<String>> voices = new HashMap<>();
        voices.put("", Arrays.asList("C ","D ", "E ", "F ", "|"));
        Header header = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        Header header2 = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        assertTrue("expected the two headers to be equal", header.equals(header2));
        assertTrue("expected hashCodes to be the same", header.hashCode() == header2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentHeaders() {
        Map<String, List<String>> voices = new HashMap<>();
        voices.put("", Arrays.asList("C ","D ", "E ", "F ", "|", "A"));
        Map<String, List<String>> voices2 = new HashMap<>();
        voices2.put("voice 1", Arrays.asList("C ","D ", "E ", "F ", "|"));

        Header header = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices);
        Header header2 = new Header(title, index, key, composer, meter, length, tempoBaseNote, tempoBPM, voices2);
        assertFalse("expected the two headers to not be equal", header.equals(header2));        
    }

}
