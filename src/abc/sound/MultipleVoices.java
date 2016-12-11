package abc.sound;

/**
 * MultipleVoices represents many pieces pieces of music playing at the same time.
 * The pieces start at the same instant, but may end at different times.
 */
public class MultipleVoices implements Music {
    
    private final Music recentVoice;
    private final Music rest;
    
    // Abstraction function
    //  represents a piece of music consisting of recentVoice voice and the voices present in rest
    
    // Representation Invariant
    //      both fields are not null
    
    // Rep exposure
    //      Music is immutable
    //      both fields are marked as private and final
    
    private void checkRep() {
        assert recentVoice != null;
        assert rest != null;
    }
    
    /**
     * Make a Together of many pieces of music.
     * @param recentVoice one piece of music
     * @param rest the remaining pieces of music
     */
    public MultipleVoices(Music recentVoice, Music rest) {
        this.recentVoice = recentVoice;
        this.rest = rest;
        checkRep();
    }

    @Override
    public double duration() {
        return Double.max(recentVoice.duration(), rest.duration());
    }

    @Override
    public Music transpose(int semitonesUp) {
        return null;
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        recentVoice.play(player, atBeat);
        rest.play(player, atBeat);
    }
    
    /**
     * represents a human readable string of the format:
     * Together[<recentVoiceStringRep> |||| <restStringRep>]
     * @return string of this as described above
     */
    @Override
    public String toString() {
        return "Together[" + recentVoice.toString() + "||||" + rest.toString() +"]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((recentVoice == null) ? 0 : recentVoice.hashCode());
        result = prime * result + ((rest == null) ? 0 : rest.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MultipleVoices other = (MultipleVoices) obj;
        if (recentVoice == null) {
            if (other.recentVoice != null)
                return false;
        } else if (!recentVoice.equals(other.recentVoice))
            return false;
        if (rest == null) {
            if (other.rest != null)
                return false;
        } else if (!rest.equals(other.rest))
            return false;
        return true;
    }
}
