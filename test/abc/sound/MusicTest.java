package abc.sound;

import org.junit.Test;

/**
 * This file contains tests on the immutable Music datatype. Play will be tested in a separate file as it won't be run on didit
 *
 */
public class MusicTest {
    
    /*
     * Testing strategy:
     *  duration() --> result
     *      Music is a single note,
     *      Music is a rest,
     *      Music is a concat
     *      Music is a MultipleVoices
     *  
     *  transpose(semitonesUp) --> result
     *      Music is a single note
     *          semitones is a factor of 12, semitones is not a factor of 12
     *      Music is a single rest
     *      Music is a concat
     *      Music is a MultipleVoices
     *  
     *  addVoice(voice) --> result
     *      this/voice is a single note
     *      this/voice is a single rest
     *      this/voice is a concat
     *      this/voice is a Multiple voices
     *  
     *  concat(music) --> result
     *      this/music is a single note
     *      this/music is a single rest
     *      this/music is a concat
     *      this/music is a MultipleVoices
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
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO: Implement tests
}
