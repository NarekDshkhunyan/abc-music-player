package abc.sound;

/**
 * Immutable representation of a concatenation of two pieces of music
 * @author alican, mabunass, narek
 *
 */
public class Concat implements Music {
    
    private final Music music1;
    private final Music music2;
    
    // Abstraction function:
    //  represents two pieces of music that should be played sequentially with music1 being played first and music2 played second
    
    // Rep Invariant:
    //  music1 != null
    //  music2 != null
    
    // Safety from rep exposure:
    //  all fields are marked as private and final and are immutable
    
    public Concat(Music music1, Music music2) {
        this.music1 = music1;
        this.music2 = music2;
        checkRep();
    }
    
    private void checkRep() {
        assert music1 != null;
        assert music2 != null;
    }
    
    @Override
    public double duration() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Music transpose(int semitonesUp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "Concat [music1=" + music1 + ", music2=" + music2 + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((music1 == null) ? 0 : music1.hashCode());
        result = prime * result + ((music2 == null) ? 0 : music2.hashCode());
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
        Concat other = (Concat) obj;
        if (music1 == null) {
            if (other.music1 != null)
                return false;
        } else if (!music1.equals(other.music1))
            return false;
        if (music2 == null) {
            if (other.music2 != null)
                return false;
        } else if (!music2.equals(other.music2))
            return false;
        return true;
    }
}
