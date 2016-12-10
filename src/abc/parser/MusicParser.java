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
import abc.sound.*;
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
            
//                case ROOT:{
//                    break;
//                
//                }
            
                case MUSIC: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        queue.add(child);
                    }
                    break;
                }
                
                case LINE: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        if (child.getName() != MusicGrammar.COMMENT) {
                            queue.add(child);
                        }
                    }
                    break;
                }
                
                // parse element from here on
                case ELEMENT: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        //System.out.println(child);
                        if (child.getName() == MusicGrammar.NOTEELEMENT || child.getName() == MusicGrammar.TUPLETELEMENT) {
                            //System.out.println(child);
                            queue.add(child);
                        } else if (child.getName() == MusicGrammar.BARLINE) {         // TODO
                            continue;
                        } else if (child.getName() == MusicGrammar.NTHREPEAT) {       // TODO
                            continue;
                        } else {                                                      // Just a whitespace
                            continue;
                        }
                    }
                    break;
                }
                
                case NOTEELEMENT: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        //System.out.println(child);
                        queue.add(child);
                    }
                    break;
                }
                
                case NOTE: {
                    //System.out.println(currentChild);
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        if (child.getName() == MusicGrammar.NOTEORREST) {
                            queue.add(child);
                        } else {                            // TODO notelength LCD calculations
                            if (child.getContents().length() == 0) {
                                double notelength = 0;    //  just a placeholder for now          
                            } else {
                                String[] ratio = child.getContents().split("/");
                                double notelength = (ratio[0].isEmpty()) ? 1.0/Integer.parseInt(ratio[1])   // if it's /2 do 1/2, if it's 3/2 keep that way
                                                                         : Double.parseDouble(ratio[0])/Double.parseDouble(ratio[1]);
                                //System.out.println(notelength);
                                //System.out.println(child.getContents());
                            }
                        }
                    }
                    break;
                }
                
                case NOTEORREST: {
                    //System.out.println(currentChild.getName());
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        if (child.getName() == MusicGrammar.PITCH) {
                            //System.out.println("Pitch: " + child);
                            queue.add(child);
                        } else if (child.getName() == MusicGrammar.REST) {
                            //System.out.println("Rest: " + child);
                            queue.add(child);
                        }
                    }
                    break;
                }
                
                case PITCH: {
                    //System.out.println(currentChild);
                    
                    // Create a pitch first
                    Pitch pitch = null;
                    char basenote  = currentChild.childrenByName(MusicGrammar.BASENOTE).get(0).getContents().charAt(0);
                    if ( Character.isUpperCase(basenote)) {
                        pitch = new Pitch(basenote);                            // so if char is 'C', make pitch C 
                    } else {
                        pitch = new Pitch(Character.toUpperCase(basenote));     // TODO something is wrong here, for example g is printed as G'
                        pitch = pitch.transpose(12);                            // so if char is 'c', make pitch c
                    }
                    
                    // Then take care of accidentals
                    if ( !currentChild.childrenByName(MusicGrammar.ACCIDENTAL).isEmpty()) {
                        String accidental  = currentChild.childrenByName(MusicGrammar.ACCIDENTAL).get(0).getContents();
                        if (accidental.equals("^")) {
                            pitch = pitch.transpose(1);
                        } else if (accidental.equals("^^")) {
                            pitch = pitch.transpose(2);
                        } else if (accidental.equals("_")) {
                            pitch = pitch.transpose(-1);
                        } else if (accidental.equals("__")) {
                            pitch = pitch.transpose(-2);
                        } else {                                    // neutral
                            continue;
                        }
                    }
                    
                    // Then take care of octaves
                    if ( !currentChild.childrenByName(MusicGrammar.OCTAVE).isEmpty()) {
                        String octave  = currentChild.childrenByName(MusicGrammar.OCTAVE).get(0).getContents();      
                        int semitoneChange = octave.charAt(0) == ',' ? -12*octave.length() : 12*octave.length();
                        pitch = pitch.transpose(semitoneChange);
                    }
                    
                    //System.out.println("Contructed pitch: " + pitch.toString());
                    break;
                }
                
                case REST: {
                    double duration = 1.0;                  // TODO Did we ever figure out if this stays float or int?
                    Rest rest = new Rest(duration);
                    break;
                }
                
                case NOTELENGTH: {
                    break;
                }
                
                case MULTINOTE: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        //System.out.println(child);
                        queue.add(child);
                    }
                    break;
                }
                
                case TUPLETELEMENT: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        //System.out.println(child);
                    }
                    break;
                }
                
                // parse midtune field from here on
//                case MIDTUNEFIELD: {
//                    break;
//                }
                
//                case VOICE: {
//                    break;
//                }
                
                default: {
                    break;
                }
            }    
    }
        
    return null;
    }
    
}
