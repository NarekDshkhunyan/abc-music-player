package abc.sound;

/**
 * Immutable epresentation of a rest
 * @author alican, mabunass, narek
 *
 */
public class Rest implements Music {
    
    private final double duration;
    
    // Abstraction Function: Represents rest for a certain duration given by duration.
    //                       A rest is an interval of silence in a piece of music. 
    //                       (source: https://en.wikipedia.org/wiki/Rest_(music)). Duration of 0 indicates an empty piece of music
    //
    // Representation Invariant: Duration of rest, duration, must be non-negative.
    //      
    // Safety from rep exposure: Duration field is a primitive and it is made final and private
    
    private void checkRep(){
        assert this.duration >= 0;
    }
    
    public Rest(double duration){
        this.duration = duration;
        checkRep();    
    }

    @Override
    public double duration() {
        return duration;
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        return;
    }
    /**
     * Converts rest into a human-readable string of the format:
     *  z*<duration>/192.0
     * @return string representation of this
     */
    @Override
    public String toString() {
        return "z" + duration/192.0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(duration);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Rest other = (Rest) obj;
        if (Double.doubleToLongBits(duration) != Double.doubleToLongBits(other.duration))
            return false;
        return true;
    }   
}
