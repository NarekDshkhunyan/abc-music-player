package abc.sound;

import static org.junit.Assert.*;

import org.junit.Test;

public class MusicTest {
    
    // TODO: testing strategies
     
    private final static Pitch A = new Pitch('A');
    private final static Pitch B = new Pitch('B'); 
    private final static Pitch C = new Pitch('C');
    private final static Pitch D = new Pitch('D');
    private final static Pitch E = new Pitch('E');
    
    private final static Note NOTE_1 = new Note(B, 1.0);
    
    private final static Rest REST_1 = new Rest(1);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void testNote() {
        //System.out.println(NOTE_1.toString());
    }
    
    @Test
    public void testPitch() {
        int diff_C_A = C.difference(A.transpose(-12));
        int diff_C_E = C.difference(E);
        boolean D_less_than_C = D.lessThan(C);
        boolean D_less_than_E = D.lessThan(E);
        assertEquals(C.toMidiNote(), 60);
        assertEquals(diff_C_A, 3);
        assertEquals(diff_C_E, -4);
        assertEquals(D_less_than_C, false);
        assertEquals(D_less_than_E, true);
    }
    
    @Test
    public void testTransposeUp() {
        String[] expected = {"D", "^D", "E", "F", "^F", "G", "^G", "A", "^A", "B", "C'", "^C'", "D'"}; 
        for (int i = 0; i < 13; i++) {
            Pitch transposed = D.transpose(i);
            //System.out.println(transposed.toString());
            assertEquals(transposed.toString(), expected[i]);
        }
    }
    
    @Test
    public void testTransposeDown() {
        String[] expected = {"A,", "^A,", "B,", "C", "^C", "D", "^D", "E", "F", "^F", "G", "^G", "A"}; 
        for (int i = -12; i < 1; i++) {
            Pitch transposed = A.transpose(i);
            //System.out.println(transposed.toString());
            assertEquals(transposed.toString(), expected[i+12]);
        }
    }
    
    @Test
    public void testRest() {
        assertEquals(REST_1.toString(), "Rest [duration=1.0]");
    }

}
