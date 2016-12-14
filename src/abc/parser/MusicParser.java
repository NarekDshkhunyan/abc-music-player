package abc.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Queue;

import abc.sound.*;
import lib6005.parser.ParseTree;

public class MusicParser {
    
    private static Map<List<String>, List<String>> sharpKeySignatures = getSharpKeySignatures();
    private static Map<List<String>, List<String>> flatKeySignatures = getFlatKeySignatures();
      
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of pitches that get affected by the key signatures.
     * Mapping between scales with sharp key signatures and affected pitches. ([Major key, Minor key]: [Affected Pitches]).
     * @return sharpKeySignatures key signatures and their corresponding sharp pitches
     */
    private static Map<List<String>, List<String>> getSharpKeySignatures() {
        
        Map<List<String>, List<String>> sharpKeySignaturesMapping = new HashMap<>();
      
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>()); //0 sharps
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("G", "Em")), 
                new ArrayList<>(Arrays.asList("F", "f"))); //F#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("D", "Bm")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c"))); //F#, C#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("A", "F#m")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c", "G", "g"))); //F#, C#, G#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("E", "C#m")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c", "G", "g", "D", "d")));  //F#, C#, G#, D#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("B", "G#m")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c", "G", "g", "D", "d", "A", "a")));//F#, C#, G#, D#, A#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F#", "D#m")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c", "G", "g", "D", "d", "A", "a", "E", "e"))); //F#, C#, G#, D#, A#, E#
        sharpKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C#", "A#m")), 
                new ArrayList<>(Arrays.asList("F", "f", "C", "c", "G", "g", "D", "d", "A", "a", "E", "e", "B", "b"))); //F#, C#, G#, D#, A#, E#, B#
    
        return sharpKeySignaturesMapping;
    
    }
    
    /**
     * Builds a mapping between list of strings, which correspond to key signatures,
     * and a list of Pitches that get affectes by the key signatures.
     * Mapping between scales with flat key signatures and affected pitches. ([Major key, Minor key] : [Affected Pitches]).
     * @return flatKeySignatures key signatures and their corresponding flat pitches
     */
    private static Map<List<String>, List<String>> getFlatKeySignatures() {
        
        Map<List<String>, List<String>> flatKeySignaturesMapping = new HashMap<>();
        
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>());   //0 flats 
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("F", "Dm")), 
                new ArrayList<>(Arrays.asList("B" , "b")));   //Bb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Bb", "Gm")), 
                new ArrayList<>(Arrays.asList("B", "b", "E", "e"))); //Bb, Eb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Eb", "Cm")), 
                new ArrayList<>(Arrays.asList("B", "b", "E", "e", "A", "a"))); //Bb, Eb, Ab
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Ab", "Fm")),
                new ArrayList<>(Arrays.asList("B", "b", "E", "e", "A", "a", "D", "d"))); //Bb, Eb, Ab, Db
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Db", "Bbm")),
                new ArrayList<>(Arrays.asList("B", "b", "E", "e", "A", "a", "D", "d", "G", "g"))); //Bb, Eb, Ab, Db, Gb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Gb", "Ebm")),
                new ArrayList<>(Arrays.asList("B", "b", "E", "e", "A", "a", "D", "d", "G", "g", "C", "c"))); //Bb, Eb, Ab, Db, Gb, Cb
        flatKeySignaturesMapping.put(new ArrayList<>(Arrays.asList("Cb", "Abm")),
                new ArrayList<>(Arrays.asList("B", "b", "E", "e", "A", "a", "D", "d", "G", "g", "C", "c", "F", "f"))); //Bb, Eb, Ab, Db, Gb, Cb, Fb
          
    return flatKeySignaturesMapping; 
    }
    
    /**
     * Builds an abstract syntax tree of a piece of Music from a ParseTree
     * @param tree the parse tree that is constructed according to the specified abc notation grammar
     * @return a Music that the parse tree represents
     */
    public static Music buildMusic(ParseTree<MusicGrammar> tree, Header header) {
        
        String keySignature = header.getKey(); //Gets key signature of String
        String length = header.getLength();
        String tempoBaseNote = header.getTempoBaseNote();
        Map<String, Integer> charToAccidental = new HashMap<>();  
        
        double durationOfDefaultNote = Music.DEFAULT_DURATION_OF_DEFAULT_NOTE;
        
        if (!length.equals(tempoBaseNote)) {
            String[] lengthFrac = length.split("/");
            double lengthDouble = Double.parseDouble(lengthFrac[0])/Double.parseDouble(lengthFrac[1]);
            String[] tempoFrac = tempoBaseNote.split("/");
            double tempoDouble = Double.parseDouble(tempoFrac[0])/Double.parseDouble(tempoFrac[1]);
            double durationMultiplier = lengthDouble/tempoDouble;
            durationOfDefaultNote = durationOfDefaultNote*durationMultiplier;
            
        }
        
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
                        if (child.getName() != MusicGrammar.WHITESPACE) {
                            queue.add(child);
                        }
                    }
                    break;
                }
                
                case NOTEELEMENT: {
                    ParseTree<MusicGrammar> child = currentChild.children().get(0);
                    if (child.getName() == MusicGrammar.NOTE) {
                        Music noteToAdd = parseNoteOrRest(child, durationOfDefaultNote, OptionalDouble.of(1.0), keySignature, charToAccidental);
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
                                multinote = parseNoteOrRest(note, durationOfDefaultNote, OptionalDouble.of(1.0), keySignature, charToAccidental);
                            } else {
                                multinote = Music.addVoice(parseNoteOrRest(note, durationOfDefaultNote, OptionalDouble.of(1.0), keySignature, charToAccidental), multinote);
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
                    String spec = specTree.childrenByName(MusicGrammar.DIGIT).get(0).getContents();
                    OptionalDouble noteLengthMultiplier = OptionalDouble.of(1.0);
                    if (spec.equals("2")) {
                        noteLengthMultiplier = OptionalDouble.of(3.0/2.0);
                    } else if (spec.equals("3")) {
                        noteLengthMultiplier = OptionalDouble.of(2.0/3.0);
                    } else {
                        noteLengthMultiplier = OptionalDouble.of(3.0/4.0);                        
                    }
                    Music tuplet = null;
                    for (ParseTree<MusicGrammar> child : currentChild.childrenByName(MusicGrammar.NOTEELEMENT)) {
                        Music nextElement = null;
                        if (child.getName() == MusicGrammar.NOTE) {
                            nextElement = parseNoteOrRest(child, durationOfDefaultNote, noteLengthMultiplier, keySignature, charToAccidental);                            
                        } else {
                            for (ParseTree<MusicGrammar> multinoteChild : child.childrenByName(MusicGrammar.NOTE)) {
                                if (nextElement == null) {
                                     nextElement = parseNoteOrRest(multinoteChild, durationOfDefaultNote, noteLengthMultiplier, keySignature, charToAccidental);
                                } else {
                                    nextElement = Music.addVoice(parseNoteOrRest(multinoteChild, durationOfDefaultNote, noteLengthMultiplier, keySignature, charToAccidental), nextElement);
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
                    charToAccidental = new HashMap<>();
                    
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
     * @param durationOfDefaultNote duration the default note of the piece
     * @param tupletMeasure if note is part of a tuplet, include its measure
     * @param keySignature keySignature of the piece the note is a part of
     * @return a piece of music representing the note or rest found at note
     */
    private static Music parseNoteOrRest(ParseTree<MusicGrammar> note, double durationOfDefaultNote, OptionalDouble tupletMeasure, String keySignature, Map<String, Integer> charToAccidentals) {
        
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
            return new Rest(durationOfDefaultNote * noteLengthMultiplier);
        } else {
            ParseTree<MusicGrammar> pitchTree = pitches.get(0);
            char baseNote = pitchTree.childrenByName(MusicGrammar.BASENOTE).get(0).getContents().charAt(0);
            String baseNoteOctave = Character.toString(baseNote);
            
            String accidental = "";
            if (!pitchTree.childrenByName(MusicGrammar.ACCIDENTAL).isEmpty()) {
                accidental = pitchTree.childrenByName(MusicGrammar.ACCIDENTAL).get(0).getContents();
            }
            
            List<ParseTree<MusicGrammar>> octaves = pitchTree.childrenByName(MusicGrammar.OCTAVE);
            int semitonesUp = 0;
            if (octaves.size() > 0) {
                String octave = octaves.get(0).getContents();
                semitonesUp = octave.contains("'") ? octave.length() : -1*octave.length();
            }
            
            semitonesUp = (Character.isLowerCase(baseNote)) ? semitonesUp + 1 : semitonesUp;
            
            if (semitonesUp > 0){
                for (int i = 0; i < semitonesUp; i++){
                    baseNoteOctave += "'"; 
                } 
            }else{
                for (int i = 0; i < Math.abs(semitonesUp); i++){
                    baseNoteOctave += ","; 
                }   
            }
            
            Pitch pitch = new Pitch(Character.toUpperCase(baseNote)).transpose(semitonesUp*12);
            
            Pitch newPitch = applyAccidentals(baseNote, pitch, accidental, baseNoteOctave, charToAccidentals);
            
            if (!charToAccidentals.containsKey(baseNoteOctave)){
                pitch = applyKeySignature(baseNote, pitch, keySignature); 
            }else{
                pitch = newPitch;
            }
      
            return new Note(pitch, durationOfDefaultNote * noteLengthMultiplier * tupletMeasureDouble);
        }
            }  
        
            
        /**
         * Updates the pitch as necessary given the key signature
         * 
         * @param baseNote character representation of the note
         * @param pitch the pitch to modify
         * @param keySignature the key signature that might affect the pitch
         * @return pitch updated pitch
         */
        private static Pitch applyKeySignature(char baseNote, Pitch pitch, String keySignature) {
            
            /* If base note is among affected sharp notes,
             * transpose the current pitch up by one semitone
             */
            for (List<String> key: sharpKeySignatures.keySet()){
                if (key.contains(keySignature)){
                    List<String> affectedSharpNotes = MusicParser.sharpKeySignatures.get(key);
                    if (affectedSharpNotes.contains(Character.toString(baseNote))){
                        pitch = pitch.transpose(1);   
                    }
                }  
            }
            
            /* If base note is among affected flat notes,
             * transpose the current pitch down by one semitone
             */
            for (List<String> key: flatKeySignatures.keySet()){
                if(key.contains(keySignature)){
                    List<String> affectedFlatNotes = MusicParser.flatKeySignatures.get(key);
                    if (affectedFlatNotes.contains(Character.toString(baseNote))){
                        pitch = pitch.transpose(-1);
                    }   
                }    
            }
           
            return pitch;
        }
               
        /**
         * Update the pitch as necessary given the accidentals and modifies charToAccidental
         * 
         * @param baseNote character representation of the note
         * @param pitch the pitch to modify
         * @param accidental single or multiple sharps or flats that take effect for the duration of the bar
         * @return pitch updated pitch
         */
        private static Pitch applyAccidentals(char baseNote, Pitch pitch, String accidental, String baseNoteOctave, Map<String, Integer> charToAccidental) {
            
            // Keep track of modified accidentals throughout a bar and key signatures
            if (accidental.length() > 0) {
                if (accidental.charAt(0) == '^') {
                    pitch = pitch.transpose(accidental.length()*1); 
                    charToAccidental.put(baseNoteOctave, accidental.length());
                    
                } else if (accidental.charAt(0) == '_') {
                   pitch = pitch.transpose(accidental.length()*-1);
                   charToAccidental.put(baseNoteOctave, -1*accidental.length());  
                   
                } else if (accidental.charAt(0) == '=') {
                    charToAccidental.put(baseNoteOctave, 0);
                }
            } else {
                if (charToAccidental.containsKey(baseNoteOctave)) {
                    pitch = pitch.transpose(charToAccidental.get(baseNoteOctave));
                }
            }
                   
            return pitch;
            }
        }
    