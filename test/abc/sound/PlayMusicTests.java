package abc.sound;

import org.junit.Test;

import abc.player.Main;

/** These tests focus on the play method for Music
 * @category no_didit
 *
 */
public class PlayMusicTests {
    /*
     * Testing strategy
     *  play(player, atBeat) --> result
     *      music is a single note
     *      music is a note-rest-note
     *      music is a complex concatenation
     *      music is a combination of multiple voices
     */
    
    @Test
    public void testPlaySingleNote() {
        String testFileName = "sample_abc/single_note.abc";
        Main.play(testFileName);
    }
        
    @Test
    public void testPlayNoteRestNote() {
        String testFileName = "sample_abc/note-rest.abc";
        Main.play(testFileName);  
    }
    
    @Test
    public void testPlayConcatenation() {
        String testFileName = "sample_abc/noQ.abc";
        Main.play(testFileName);   
    }
    
    @Test
    public void testPlayMultipleVoices() {
        String testFileName = "sample_abc/fure_elise.abc";
        Main.play(testFileName);    
    }
}
