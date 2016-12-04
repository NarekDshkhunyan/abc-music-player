package abc.sound;

import java.io.File;

/**
 * Immutable datatype representing a piece of music specified in a given abc file 
 * @author alican, mabunass, narek
 */
public interface Music {
    // Music = Rest(duration: double) + Note(duration: double, pitch: Pitch) + Together(voice: Music, rest: Music)
    
    /**
     * parses abc file into Music
     * @param musicFile abc file to be parsed; must be properly formatted as defined by the abc notation
     * @return Music representing the piece in the abc file
     */
    public static Music parseMusic(File musicFile) {
        throw new RuntimeException("Unimplemented");
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
    Music transpose(int semitonesUp);
    
    /**
     * Play this piece.
     * @param player player to play on
     * @param atBeat when to play
     */
    void play(SequencePlayer player, double atBeat);
    
    
}
