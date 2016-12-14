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
    public void testPlaySingleNote() throws InterruptedException {
        String testFileName = "sample_abc/single_note.abc";
        Main.play(testFileName);
        Thread.sleep(1000);
    }
        
    @Test
    public void testPlayNoteRestNote() throws InterruptedException {
        String testFileName = "sample_abc/note-rest.abc";
        Main.play(testFileName);  
        Thread.sleep(1000);
    }
    
    @Test
    public void testPlayConcatenation() throws InterruptedException {
        String testFileName = "sample_abc/noQ.abc";
        Main.play(testFileName);  
        Thread.sleep(100000);
    }
    
    @Test
    public void testPlayMultipleVoices() throws InterruptedException {
        String testFileName = "sample_abc/fur_elise.abc";
        Main.play(testFileName);  
        Thread.sleep(1000000);
    }
}
