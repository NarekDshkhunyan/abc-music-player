package abc.sound;

public class Note implements Music {

    @Override
    public double duration() {
        return 0;
    }

    @Override
    public Music transpose(int semitonesUp) {
        return null;
    }

    @Override
    public void play(SequencePlayer player, double atBeat) {

    }

}
