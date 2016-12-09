package abc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import abc.sound.Header;
import abc.sound.Music;
import abc.sound.Pitch;
import lib6005.parser.ParseTree;

public class MusicParser {
    
    private static Map<List<String>, List<Pitches>> sharpKeySignatures = getSharpKeySignatures();
    private static Map<List<String>, List<Pitches>> flatKeySignatures = getFlatKeySignatures();
    
    private enum Pitches{
        SHARPA(new Pitch('A'), 1),
        FLATA(new Pitch('A'), -1),
        SHARPB(new Pitch('B'), 1),
        FLATB(new Pitch('B'), -1),
        SHARPC(new Pitch('C'), 1),
        FLATC(new Pitch('C'), -1),
        SHARPD(new Pitch('D'), 1),
        FLATD(new Pitch('D'), -1),
        SHARPE(new Pitch('E'), 1),
        FLATE(new Pitch('E'), -1),
        SHARPF(new Pitch('F'), 1),
        FLATF(new Pitch('F'), -1),
        SHARPG(new Pitch('G'), 1),
        FLATG(new Pitch('G'), -1);
        
        private Pitch pitch;
        
        Pitches(Pitch pitch, int semitone){
            this.pitch = pitch.transpose(semitone);
        }             
    }
    
    /**
     * Modify the frequency of a note (pitch) by the number of semitones given. It could
     * either increase or decrease the frequency depending on the boolean increase.
     * 
     * @param pitch Pitch that we want to modify.
     * @param semitone int semitones is the number of semitones we want to modify pitch by.
     * @param change boolean, if true: increase the pitch by semitone.
     *                          if false: decrease the pitch by semitone.
     *                          
     * @return the new modified pitch.
     */
    private Pitch modifyPitch(Pitch pitch, int semitone, boolean change){
        Pitch newPitch;
        if (change){
            newPitch = pitch.transpose(semitone);     
        }
        newPitch = pitch.transpose(-1*semitone);
          
        return newPitch;    
    }
    
    
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of sharp pitches.
     * Mapping between scales with sharp key signatures and affected pitches. ([Major key, Minor key]: [sharp Pitches]).
     * @return sharpKeySignatures key signatures and their corresponding sharp pitches
     */
    private static Map<List<String>, List<Pitches>> getSharpKeySignatures() {
        
        Map<List<String>, List<Pitches>> sharpKeySignaturesMapping = new HashMap<>();
      
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>()); //0 sharps
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("G", "Em")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF))); //F#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("D", "Bm")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC))); //F#, C#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("A", "F#m")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG))); //F#, C#, G#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("E", "C#m")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD)));  //F#, C#, G#, D#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("B", "G#m")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA)));//F#, C#, G#, D#, A#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F#", "D#m")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA, Pitches.SHARPE))); //F#, C#, G#, D#, A#, E#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C#", "A#m")), 
                new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA, Pitches.SHARPE, Pitches.SHARPB))); //F#, C#, G#, D#, A#, E#, B#
    
        return sharpKeySignaturesMapping;
    
    }
    
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of flat pitches.
     * Mapping between scales with flat key signatures and affected pitches. ([Major key, Minor key] : [flat Pitches]).
     * @return flatKeySignatures key signatures and their corresponding flat pitches
     */
    private static Map<List<String>, List<Pitches>> getFlatKeySignatures() {
        
        Map<List<String>, List<Pitches>> flatKeySignaturesMapping = new HashMap<>();
        
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>());   //0 flats 
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F", "Dm")), 
                new ArrayList<>(Arrays.asList(Pitches.FLATB)));   //Bb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Bb", "Gm")), 
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE))); //Bb, Eb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Eb", "Cm")), 
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA))); //Bb, Eb, Ab
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Ab", "Fm")),
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD))); //Bb, Eb, Ab, Db
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Db", "Bbm")),
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG))); //Bb, Eb, Ab, Db, Gb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Gb", "Ebm")),
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG, Pitches.FLATC))); //Bb, Eb, Ab, Db, Gb, Cb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Cb", "Abm")),
                new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG, Pitches.FLATC, Pitches.FLATF))); //Bb, Eb, Ab, Db, Gb, Cb, Fb
          
    return flatKeySignaturesMapping; 
    }
    
    /**
     * builds an abstract syntax tree of a piece of Music from a ParseTree
     * @param tree the parse tree that is constructed according to the specified abc notation grammar
     * @return a Music that the parse tree represents
     */
    public static Music buildMusic(ParseTree<MusicGrammar> tree, Header header) {
                
        Map<List<String>, List<Pitches>> sharpKeySingatures = MusicParser.sharpKeySignatures;
        Map<List<String>, List<Pitches>> flatKeySignatures = MusicParser.flatKeySignatures;
        
        String keySignature = header.getKey(); //Gets key signature of String
        
        Map<String, List<String>> voices = header.getVoices();
        
        
        // start parsing the tree
        Queue<ParseTree<MusicGrammar>> queue = new LinkedList<>(tree.children());
        while (queue.size() > 0) {
            ParseTree<MusicGrammar> currentChild = queue.remove();
             
            
            switch (currentChild.getName()) {
            
                case ROOT:{
                    break;
                
                }
            
                case MUSIC: {
                    break;
                }
                
                case LINE: {
                    break;
                }
                
                // parse element from here on
                case ELEMENT: {
                    break;
                }
                
                case NOTEELEMENT: {
                    break;
                }
                
                case NOTE: {
                    break;
                }
                
                case NOTEORREST: {
                    break;
                }
                
                case PITCH: {
                    break;
                }
                
                case REST: {
                    break;
                }
                
                case NOTELENGTH: {
                    break;
                }
                
                case MULTINOTE: {
                    break;
                }
                
                case TUPLETELEMENT: {
                    break;
                }
                
                // parse midtune field from here on
                case MIDTUNEFIELD: {
                    break;
                }
                
                case VOICE: {
                    break;
                }
                
                default: {
                    break;
                }
            }    
    }
        
    return null;
    }
    
}
