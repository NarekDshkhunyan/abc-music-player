package abc.sound;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This file contains tests on the immutable Music datatype. Play will be tested in a separate file as it won't be run on didit
 *
 */
public class MusicTests {
    
    /*
     * Testing strategy:
     *  duration() --> result
     *      Music is a single note,
     *      Music is a rest,
     *      Music is a concat
     *      Music is a MultipleVoices
     *  
     *  addVoice(voice,rest) --> result
     *      voice is a single note, single rest, is a concat, is a MultipleVoices
     *      rest is a single note, single rest, is a concat, is a MultipleVoices

     *  concat(music1, music2) --> result
     *      music1 is a single note, single rest, is a concat, is a MultipleVoices
     *      music2 is a single note, single rest, is a concat, is a MultipleVoices

     *      
     *  toString() --> result
     *      this is a single note, single rest, concat, MultipleVoices
     *  
     *  parseMusic(abcFile) --> Music
     *      abcFile is invalid file
     *      abcFile is a proper file
     *          contains single voice
     *          contains multiple voices
     *          contains basic elements (e.g. single notes, rests)
     *          contains more complex elements (e.g. repeats, tuplets, chords)
     *  
     *  
     */
     
    private final static Pitch A = new Pitch('A');
    private final static Pitch B = new Pitch('B'); 
    private final static Pitch C = new Pitch('C');
    private final static Pitch D = new Pitch('D');
    private final static Pitch E = new Pitch('E');
    
    private final static Note NOTE_1 = new Note(B, 1.0);
    
    private final static Rest REST_1 = new Rest(1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // test duration() with single note
    @Test
    public void testDurationSingleNote() {
        assertEquals("expected correct duration for single note", new Double(1.0), new Double(NOTE_1.duration()));
    }
    
    // covers duration() with a rest
    @Test
    public void testDurationSingleRest() {
        assertEquals("expected correct duration for rest", new Double(1.0), new Double(REST_1.duration()));
    }
    
    // covers duration() with a concat
    // covers concat music1 is a single note, music2 is a single rest
    @Test
    public void testDurationConcat() {
        Music music = Music.concat(NOTE_1, REST_1);
        assertEquals("expected correct duration for a concat", new Double(2.0), new Double(music.duration()));
    }
    
    // covers duration() with multiple voices
    // covers addVoice music1 is a single note, music2 is a concat
    @Test
    public void testDurationMultipleVoices() {
        Music music = Music.concat(NOTE_1, REST_1);
        Music together = Music.addVoice(music, NOTE_1);
        assertEquals("expected correct duration for multiple voices", new Double(2.0), new Double(together.duration()));
    }
    
    // covers toString for Rest
    @Test
    public void testToStringRest() {
        assertEquals("expected correct string representation", "z" + 1.0, REST_1.toString());
    }
    
    // covers toString for Note
    @Test
    public void testToStringNote() {
        assertEquals("expected correct string representation", B.toString() + 1.0, NOTE_1.toString());
    }
    
    // covers toString for Concat
    @Test
    public void testToStringConcat() {
        Music music = Music.concat(NOTE_1, REST_1);
        assertEquals("expected correct duration for a concat", B.toString() + 1.0 + " z" + 1.0, music.toString());        
    }
    
    // covers toString for MultipleVoices
    @Test
    public void testToStringMultipleVoices() {
        Music music = Music.addVoice(NOTE_1, REST_1);
        assertEquals("expected correct duration for a concat", "Together[" + B.toString() + 1.0 + "||||z" + 1.0 +"]", music.toString());        
    }      
}
