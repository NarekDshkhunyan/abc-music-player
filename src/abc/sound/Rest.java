package abc.sound;

public class Rest implements Music {
    
    private final double duration;
    
    // Abstraction Function: Represents rest for a certain duration given by duration.
    //                       A rest is an interval of silence in a piece of music.
    //                       (source: https://en.wikipedia.org/wiki/Rest_(music))
    //
    // Representation Invariant: Duration of rest, duration, must be non-negative.
    //      
    // Safety from rep exposure: Duration field is made final and private.
    
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
    public Music transpose(int semitonesUp) {
        return this;
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {
        // TODO Auto-generated method stub

    }

}
