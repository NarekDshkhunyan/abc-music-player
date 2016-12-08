package abc.parser;

import org.junit.Test;

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
    
    // TODO: Implement tests
}
