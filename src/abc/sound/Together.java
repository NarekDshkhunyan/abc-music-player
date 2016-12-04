package abc.sound;

/**
 * Together represents many pieces pieces of music playing at the same time.
 * It is a recursive data type of the form 
 *      Together = Music + Together
 * The pieces start at the same instant, but may end at different times.
 */
public class Together implements Music {
    
    private Music m1;
    private Together rest;
    
    // Abstraction function
    //      m1 is one piece of music
    //      rest is another Together, which is recursively many pieces of music
    
    // Representation Invariant
    //      both fields are not null
    
    // Rep exposure
    //      music is immutable
    //      both fields are final
    
    private void checkRep() {
        assert m1 != null;
        assert rest != null;
    }
    
    /**
     * Make a Together of many pieces of music.
     * @param m1 one piece of music
     * @param rest the remaining pieces of music
     */
    public Together(Music m1, Together rest) {
        this.m1 = m1;
        this.rest = rest;
        checkRep();
    }
    
    /**
     * @return one of the pieces of music in this together
     */
    public Music top() {
        return m1;
    }
    
    /**
     * @return the other piece of music in this together
     */
    public Together bottom() {
        return rest;
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

}
