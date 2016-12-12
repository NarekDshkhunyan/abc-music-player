package abc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Queue;

import javax.swing.plaf.OptionPaneUI;

import abc.sound.*;
import lib6005.parser.ParseTree;

public class MusicParser {
    
    // TODO: We have to incorporate accidentals into note creation (using key signatures and any in piece changes)
    // TODO: incorporate length changes for tuplets
    
    private static Map<List<String>, List<String>> sharpKeySignatures = getSharpKeySignatures();
    private static Map<List<String>, List<String>> flatKeySignatures = getFlatKeySignatures();
    
    private final static int FLAT_SEMITONE = -1;
    private final static int SHARP_SEMITONE = 1;
    
    private enum Pitches{
        SHARPA(new Pitch('A'), SHARP_SEMITONE),
        FLATA(new Pitch('A'), FLAT_SEMITONE),
        SHARPB(new Pitch('B'), SHARP_SEMITONE),
        FLATB(new Pitch('B'), FLAT_SEMITONE),
        SHARPC(new Pitch('C'), SHARP_SEMITONE),
        FLATC(new Pitch('C'), FLAT_SEMITONE),
        SHARPD(new Pitch('D'), SHARP_SEMITONE),
        FLATD(new Pitch('D'), FLAT_SEMITONE),
        SHARPE(new Pitch('E'), SHARP_SEMITONE),
        FLATE(new Pitch('E'), FLAT_SEMITONE),
        SHARPF(new Pitch('F'), SHARP_SEMITONE),
        FLATF(new Pitch('F'), FLAT_SEMITONE),
        SHARPG(new Pitch('G'), SHARP_SEMITONE),
        FLATG(new Pitch('G'), FLAT_SEMITONE);
        
        private Pitch pitch;
        private double semitone;
        
        Pitches(Pitch pitch, int semitone){
            this.pitch = pitch.transpose(semitone);
        }    
        
        private double getSemitone(){
            return this.semitone;  
        }    
    }  
    
    
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of sharp pitches.
     * Mapping between scales with sharp key signatures and affected pitches. ([Major key, Minor key]: [sharp Pitches]).
     * @return sharpKeySignatures key signatures and their corresponding sharp pitches
     */
    private static Map<List<String>, List<String>> getSharpKeySignatures() {
        
        Map<List<String>, List<String>> sharpKeySignaturesMapping = new HashMap<>();
      
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>()); //0 sharps
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("G", "Em")), 
                new ArrayList<>(Arrays.asList("F"))); //F#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("D", "Bm")), 
                new ArrayList<>(Arrays.asList("F", "C"))); //F#, C#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("A", "F#m")), 
                new ArrayList<>(Arrays.asList("F", "C", "G"))); //F#, C#, G#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("E", "C#m")), 
                new ArrayList<>(Arrays.asList("F", "C", "G", "D")));  //F#, C#, G#, D#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("B", "G#m")), 
                new ArrayList<>(Arrays.asList("F", "C", "G", "D", "A")));//F#, C#, G#, D#, A#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F#", "D#m")), 
                new ArrayList<>(Arrays.asList("F", "C", "G", "D", "A", "E"))); //F#, C#, G#, D#, A#, E#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C#", "A#m")), 
                new ArrayList<>(Arrays.asList("F", "C", "G", "D", "A", "E", "B"))); //F#, C#, G#, D#, A#, E#, B#
    
        return sharpKeySignaturesMapping;
    
    }
    
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of flat pitches.
     * Mapping between scales with flat key signatures and affected pitches. ([Major key, Minor key] : [flat Pitches]).
     * @return flatKeySignatures key signatures and their corresponding flat pitches
     */
    private static Map<List<String>, List<String>> getFlatKeySignatures() {
        
        Map<List<String>, List<String>> flatKeySignaturesMapping = new HashMap<>();
        
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>());   //0 flats 
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F", "Dm")), 
                new ArrayList<>(Arrays.asList("B")));   //Bb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Bb", "Gm")), 
                new ArrayList<>(Arrays.asList("B", "E"))); //Bb, Eb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Eb", "Cm")), 
                new ArrayList<>(Arrays.asList("B", "E", "A"))); //Bb, Eb, Ab
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Ab", "Fm")),
                new ArrayList<>(Arrays.asList("B", "E", "A", "D"))); //Bb, Eb, Ab, Db
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Db", "Bbm")),
                new ArrayList<>(Arrays.asList("B", "E", "A", "D", "G"))); //Bb, Eb, Ab, Db, Gb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Gb", "Ebm")),
                new ArrayList<>(Arrays.asList("B", "E", "A", "D", "G", "C"))); //Bb, Eb, Ab, Db, Gb, Cb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Cb", "Abm")),
                new ArrayList<>(Arrays.asList("B", "E", "A", "D", "G", "C", "F"))); //Bb, Eb, Ab, Db, Gb, Cb, Fb
          
    return flatKeySignaturesMapping; 
    }
    
    /**
     * builds an abstract syntax tree of a piece of Music from a ParseTree
     * @param tree the parse tree that is constructed according to the specified abc notation grammar
     * @return a Music that the parse tree represents
     */
    public static Music buildMusic(ParseTree<MusicGrammar> tree, Header header) {
                        
        Map<List<String>, List<String>> sharpKeySingatures = MusicParser.sharpKeySignatures;
        Map<List<String>, List<String>> flatKeySignatures = MusicParser.flatKeySignatures;
        
        String keySignature = header.getKey(); //Gets key signature of String
        Map<String, List<String>> modifiedAccidentals = new HashMap<>(); //Map to keep track of accidental modifications
       
        String meter = header.getMeter();
        String length = header.getLength();
        String baseNote = header.getTempoBaseNote();
        int bpm = header.getTempoBPM();
        boolean repeatBlock = false;
        boolean afterFirstEndingBeforeSecondEnding = false;
        boolean pastSecondEndingBeforeEndRepeat = false;
                
        List<Music> majorSections = new ArrayList<>();
        Music music = new Rest(0); // current major section music
        Music firstEnding = new Rest(0);
        Music secondEnding = new Rest(0);

        
        // start parsing the tree
        Queue<ParseTree<MusicGrammar>> queue = new LinkedList<>(tree.children());
        while (queue.size() > 0) {
            ParseTree<MusicGrammar> currentChild = queue.remove();
            
            switch (currentChild.getName()) {
                case MUSIC: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        queue.add(child);
                    }
                    break;
                }
                
                case LINE: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        if (child.getName() != MusicGrammar.COMMENT || child.getName() != MusicGrammar.NEWLINE) {
                            queue.add(child);
                        }
                    }
                    break;
                }
                
                // parse element from here on
                case ELEMENT: {
                    for (ParseTree<MusicGrammar> child : currentChild) {
                        //System.out.println(child.getName());
                        if (child.getName() != MusicGrammar.WHITESPACE) {
                            queue.add(child);
                        }
                    }
                    break;
                }
                
                case NOTEELEMENT: {
                    ParseTree<MusicGrammar> child = currentChild.children().get(0);
                    if (child.getName() == MusicGrammar.NOTE) {
                        Music noteToAdd = parseNoteOrRest(child, OptionalDouble.of(1.0), keySignature);
                        if (!repeatBlock) {
                            music = Music.concat(music, noteToAdd);                        
                        } else {
                            if (afterFirstEndingBeforeSecondEnding) {
                                firstEnding = Music.concat(firstEnding, noteToAdd);
                            } else if (pastSecondEndingBeforeEndRepeat) {
                                secondEnding = Music.concat(secondEnding, noteToAdd);
                            } else {
                                firstEnding = Music.concat(firstEnding, noteToAdd);
                                secondEnding = Music.concat(secondEnding, noteToAdd);
                            }
                        }                        
                    } else {
                        Music multinote = null;
                        for (ParseTree<MusicGrammar> note : child.childrenByName(MusicGrammar.NOTE)) {
                            if (multinote == null) {
                                multinote = parseNoteOrRest(note, OptionalDouble.of(1.0), keySignature);
                            } else {
                                multinote = Music.addVoice(parseNoteOrRest(note, OptionalDouble.of(1.0), keySignature), multinote);
                            }
                        }
                        if (!repeatBlock) {
                            music = Music.concat(music, multinote);
                        } else {
                            if (afterFirstEndingBeforeSecondEnding) {
                                firstEnding = Music.concat(firstEnding, multinote);
                            } else if (pastSecondEndingBeforeEndRepeat) {
                                secondEnding = Music.concat(secondEnding, multinote);
                            } else {
                                firstEnding = Music.concat(firstEnding, multinote);
                                secondEnding = Music.concat(secondEnding, multinote);
                            }
                        }                        
                    }
                    break;
                }
                
                case TUPLETELEMENT: {
                    ParseTree<MusicGrammar> specTree = currentChild.childrenByName(MusicGrammar.TUPLETSPEC).get(0);
                    //System.out.println("SPEC-TREE: " + specTree);
                    String spec = specTree.childrenByName(MusicGrammar.DIGIT).get(0).getContents();
                    OptionalDouble noteLengthMultiplier = OptionalDouble.of(1.0);
                    if (spec.equals("2")) {
                        noteLengthMultiplier = OptionalDouble.of(3.0/2.0);
                    } else if (spec.equals("3")) {
                        noteLengthMultiplier = OptionalDouble.of(2.0/3.0);
                    } else {
                        noteLengthMultiplier = OptionalDouble.of(3.0/4.0);                        
                    }
                    // TODO: Incorporate multiplier
                    Music tuplet = null;
                    for (ParseTree<MusicGrammar> child : currentChild.childrenByName(MusicGrammar.NOTEELEMENT)) {
                        //System.out.println("MY CHILD: "+ child);
                        Music nextElement = null;
                        if (child.getName() == MusicGrammar.NOTE) {
                            nextElement = parseNoteOrRest(child, noteLengthMultiplier, keySignature);                            
                        } else {
                            for (ParseTree<MusicGrammar> multinoteChild : child.childrenByName(MusicGrammar.NOTE)) {
                                if (nextElement == null) {
                                     nextElement = parseNoteOrRest(multinoteChild, noteLengthMultiplier, keySignature);
                                } else {
                                    nextElement = Music.addVoice(parseNoteOrRest(multinoteChild, noteLengthMultiplier, keySignature), nextElement);
                                }
                            }                            
                        }
                        if (tuplet == null) {
                            tuplet = nextElement;
                        } else {
                            tuplet = Music.concat(tuplet, nextElement);
                        }
                    }
                    
                    if (!repeatBlock) {
                        music = Music.concat(music, tuplet);
                    } else {
                        if (afterFirstEndingBeforeSecondEnding) {
                            firstEnding = Music.concat(firstEnding, tuplet);
                        } else if (pastSecondEndingBeforeEndRepeat) {
                            secondEnding = Music.concat(secondEnding, tuplet);
                        } else {
                            firstEnding = Music.concat(firstEnding, tuplet);
                            secondEnding = Music.concat(secondEnding, tuplet);
                        }
                    }
                    break;
                }
                
                case BARLINE: {
                    String bar = currentChild.getContents();
                    
                    if (bar.equals("|]")) {
                        majorSections.add(music);
                        music = new Rest(0);
                    } else if (bar.equals("|:")) {
                        repeatBlock = true;
                        firstEnding = new Rest(0);
                        secondEnding = new Rest(0);
                    } else if (bar.equals(":|")) {
                        if (!repeatBlock) {
                            // this means that we started parsing at a major section and so entire section has to be repeated
                            music = Music.concat(music, music);
                        } else {
                            // this means we were inside a repeat block: first concatenate the two endings
                            Music repeatBlockMusic = Music.concat(firstEnding, secondEnding);
                            // now append repeatBlock to rest of the major block
                            music = Music.concat(music, repeatBlockMusic);
                            firstEnding = new Rest(0);
                            secondEnding = new Rest(0);
                        }
                        repeatBlock = false;
                        pastSecondEndingBeforeEndRepeat = false;
                    }
                    // TODO: Figure out what to do with regular bar lines that denote resets in any alterations to key signature
                    break;
                }
                
                case NTHREPEAT: {
                  
                    String nthRepeat = currentChild.getContents();
                    
                    if (nthRepeat.equals("[1")) {
                        afterFirstEndingBeforeSecondEnding = true;
                        if (!repeatBlock) {
                            firstEnding = Music.concat(music, firstEnding);
                            secondEnding = Music.concat(music, secondEnding);
                            music = new Rest(0);
                            repeatBlock = true;
                        }
                    } else {
                        afterFirstEndingBeforeSecondEnding = false;
                        if (repeatBlock) {
                            pastSecondEndingBeforeEndRepeat = true;
                        }
                    }
                    break;
                }
                
                
                default: {
                    break;
                }
            }    
        }
        majorSections.add(music);
        Music finalMusic = new Rest(0);
        for (Music majorSection : majorSections) {
            finalMusic = Music.concat(finalMusic, majorSection);
        }
        return finalMusic;
    }
    
    /**
     * parses a noteorrest into a Music that represents the underlying note or piece
     * @param note a parsetree node that represents a note non-terminal
     * @return a piece of music representing the note or rest found at note
     */
    private static Music parseNoteOrRest(ParseTree<MusicGrammar> note, OptionalDouble tupletMeasure, String keySignature) {
        System.out.println("KEY: " + keySignature);
        
        double tupletMeasureDouble = tupletMeasure.isPresent() ? tupletMeasure.getAsDouble() : 1.0;
        
        ParseTree<MusicGrammar> noteOrRest = note.childrenByName(MusicGrammar.NOTEORREST).get(0);
        List<ParseTree<MusicGrammar>> pitches = noteOrRest.childrenByName(MusicGrammar.PITCH);
        List<ParseTree<MusicGrammar>> rests = noteOrRest.childrenByName(MusicGrammar.REST);
         
        // Get the multiplier for the note's duration
        // Weird bug here

        List<ParseTree<MusicGrammar>> noteLengths = note.childrenByName(MusicGrammar.NOTELENGTH);
        double noteLengthMultiplier = 1.0;

        if (!noteLengths.isEmpty()) {
           ParseTree<MusicGrammar> noteLength = noteLengths.get(0);
           List<ParseTree<MusicGrammar>> numerators = noteLength.childrenByName(MusicGrammar.NUMERATOR);
           if (numerators.size() > 0) {
               String numerator = numerators.get(0).getContents();
               noteLengthMultiplier = noteLengthMultiplier * Double.parseDouble(numerator);
           }
           List<ParseTree<MusicGrammar>> divisions = noteLength.childrenByName(MusicGrammar.DIVISION);
           if (divisions.size() > 0) {
               double divisor = 1.0;
               ParseTree<MusicGrammar> division = divisions.get(0);
               List<ParseTree<MusicGrammar>> denominators = division.childrenByName(MusicGrammar.DENOMINATOR);
               if (denominators.size() > 0) {
                   String denominator = denominators.get(0).getContents();
                   divisor = Integer.parseInt(denominator);
               } else {
                   divisor = 2.0;
               }
               noteLengthMultiplier = noteLengthMultiplier/divisor;
           }           
        }

        if (rests.size() > 0) {
            return new Rest(Music.DEFAULT_DURATION_OF_DEFAULT_NOTE * noteLengthMultiplier);
        } else {
            ParseTree<MusicGrammar> pitchTree = pitches.get(0);
            char baseNote = pitchTree.childrenByName(MusicGrammar.BASENOTE).get(0).getContents().charAt(0);
            List<ParseTree<MusicGrammar>> accidentals = pitchTree.childrenByName(MusicGrammar.ACCIDENTAL);
            List<ParseTree<MusicGrammar>> octaves = pitchTree.childrenByName(MusicGrammar.OCTAVE);
            int semitonesUp = 0;
            if (octaves.size() > 0) {
                String octave = octaves.get(0).getContents();
                semitonesUp = octave.contains("'") ? octave.length() : -1*octave.length();
            }
            
            semitonesUp = (Character.isLowerCase(baseNote)) ? semitonesUp + 1 : semitonesUp;
            Pitch pitch = new Pitch(Character.toUpperCase(baseNote)).transpose(semitonesUp*12);
            //System.out.println("ORIGINAL: " + pitch);
            
            for (List<String> key: sharpKeySignatures.keySet()){
                if (key.contains(keySignature)){
                    List<String> affectedSharpNotes = MusicParser.sharpKeySignatures.get(key);
                    if (affectedSharpNotes.contains(Character.toString(baseNote))){
                        //System.out.println("BEFORE TRANSPOSING: " + pitch.toString());
                        pitch = pitch.transpose(1);
                        //System.out.println("AFTER TRANSPOSING: " + pitch.toString());   
                    }
                }  
            }
            
            for (List<String> key: flatKeySignatures.keySet()){
                if(key.contains(keySignature)){
                    List<String> affectedFlatNotes = MusicParser.flatKeySignatures.get(key);
                    if (affectedFlatNotes.contains(Character.toString(baseNote))){
                        //System.out.println("BEFORE TRANSPOSING: " + pitch.toString());
                        pitch = pitch.transpose(-1);
                        //System.out.println("AFTER TRANSPOSING: " + pitch.toString());
                    }   
                }    
            }
            
            // TODO: Handle accidentals. 
            // Specifically, we need to be able to keep track of modified accidentals throughout a bar and key signatures
            if (accidentals.size() > 0) {
                String accidental = accidentals.get(0).getContents();
                
            }
            return new Note(pitch, Music.DEFAULT_DURATION_OF_DEFAULT_NOTE * noteLengthMultiplier * tupletMeasureDouble);
        }
    }    
}
