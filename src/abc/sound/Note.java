package abc.sound;

/**
 * An immutable type that represents a single note
 * @author alican, mabunass, narek
 *
 */
public class Note implements Music {
    
    private final Pitch pitch;
    
    private final double duration;
    
    // Abstraction function:
    //  represents a note at the specified pitch and played for duration duration
    // Rep Invariant:
    //  pitch != null
    //  duration > 0
    // Safety from rep exposure:
    //  all fields are marked as private and final and are immutable types. Every method that returns something, returns an instance of an 
    //  immutable type
    
    /**
     * creates a new Note with specified pitch and duration
     * @param pitch the pitch of the note
     * @param duration of the note
     */
    public Note(Pitch pitch, double duration) {
        this.pitch = pitch;
        this.duration = duration;
        checkRep();
    }
    
    private void checkRep() {
        assert pitch != null;
        assert duration > 0;
    }

    @Override
    public double duration() {
        return this.duration;
    }

    @Override
    public Music transpose(int semitonesUp) {
        return new Note(pitch.transpose(semitonesUp), duration);
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        //player.addNote(pitch, atBeat, duration);
    }
    
    @Override
    public Music addVoice(Music voice) {
        return null;
    }
    
    /**
     * converts the note to a human-readable string of the format:
     *  pitch: pitch
     *  duration: duration
     */
    @Override
    public String toString() {
        return null;
    }
    
    @Override
    public boolean equals(Object that) {
        return false;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Music concat(Music music1, Music music2) {
        // TODO Auto-generated method stub
        return null;
    }
}
