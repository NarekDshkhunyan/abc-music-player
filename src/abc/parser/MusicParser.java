package abc.parser;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import abc.sound.*;
import lib6005.parser.ParseTree;

public class MusicParser {
    
                         
     public static Header buildHeader(ParseTree<AbcGrammar> tree) {        
         assert tree.getName() == AbcGrammar.ROOT;
         
         String index = "";
         String title = "";
         String key = "";
         String composer = Music.DEFAULT_COMPOSER;
         String length = Music.DEFAULT_LENGTH;
         String meter = Music.DEFAULT_METER;
         String currentVoice = "";
         int bpm = Music.DEFAULT_TEMPO_BPM;
         String tempoBaseNote = length;
         Map<String, List<String>> voices = new HashMap<>();
         voices.put(currentVoice, new ArrayList<>());
         
         boolean lengthDefined = false;
         boolean tempoDefined = false;
         
         Queue<ParseTree<AbcGrammar>> queue = new LinkedList<>(tree.children());
         while (queue.size() > 0) {
             ParseTree<AbcGrammar> currentChild = queue.remove();
             
             switch (currentChild.getName()) {
                 
                 case HEADER: {
                     for (ParseTree<AbcGrammar> child : currentChild) {
                         if (child.getName() != AbcGrammar.COMMENT) {
                             queue.add(child);
                         }
                     }
                     break;
                 }
                 
                 case MUSIC: {
                     queue.addAll(currentChild.children());
                     break;
                 }
                 
                 case OTHERS: {
                     for (ParseTree<AbcGrammar> child : currentChild) {
                         if (child.getName() != AbcGrammar.COMMENT) {
                             queue.add(child);
                         }
                     }
                     break;
                 }
                 
                 case NUMBER: {
                     List<ParseTree<AbcGrammar>> digits = currentChild.childrenByName(AbcGrammar.DIGIT);
                     for (ParseTree<AbcGrammar> digit : digits) {
                         index = index + digit.getContents().toString();
                     }
                     break;                    
                 }
                 
                 case TITLE: {
                     String text = currentChild.childrenByName(AbcGrammar.TEXT).get(0).getContents();
                     title = text;
                     break;
                 }
                 
                 case COMPOSER: {
                     String text = currentChild.childrenByName(AbcGrammar.TEXT).get(0).getContents();
                     composer = text;
                     break;                    
                 }
                 
                 case DEFAULTLENGTH: {
                     length = currentChild.childrenByName(AbcGrammar.NOTELENGTHSTRICT).get(0).getContents();
                     lengthDefined = true;
                     if (!tempoDefined) {
                         tempoBaseNote = length;
                     }
                     break;
                 }
                 
                 case METERFIELD: {
                     String newMeter = currentChild.childrenByName(AbcGrammar.METER).get(0).getContents().trim();
                     if (!(newMeter.equals("C") || newMeter.equals("C|"))) {
                         meter = newMeter;
                         if (!lengthDefined) {
                             String[] splitMeter = meter.split("/");
                             if (Double.parseDouble(splitMeter[0])/Double.parseDouble(splitMeter[1]) < 0.75) {
                                 length = "1/16";
                             } else {
                                 length = "1/8";
                             }
                         }
                     }
                     break;                     
                 }
                 
                 case TEMPOFIELD: {
                     tempoDefined = true;
                     ParseTree<AbcGrammar> tempoNode = currentChild.childrenByName(AbcGrammar.TEMPO).get(0);
                     String tempo = tempoNode.getContents();
                     String[] splitTempo = tempo.split("=");
                     tempoBaseNote = splitTempo[0];
                     bpm = Integer.parseInt(splitTempo[1]);
                     break;                     
                 }
                 
                 case VOICE: {
                     String voice = currentChild.childrenByName(AbcGrammar.TEXT).get(0).getContents().trim();
                     if (!voices.containsKey(voice)) {
                         voices.put(voice, new ArrayList<>());
                         currentVoice = voice;   
                     }
                     break;
                 }
                 
                 case LINE: {
                     //System.out.println(currentChild.getContents());
                     List<ParseTree<AbcGrammar>> midtunefields = currentChild.childrenByName(AbcGrammar.MIDTUNEFIELD);
                     List<ParseTree<AbcGrammar>> elements = currentChild.childrenByName(AbcGrammar.ELEMENT);
                     if (midtunefields.isEmpty()) {
                         List<String> voiceLines = new ArrayList<>(voices.get(currentVoice));
                         voiceLines.add(currentChild.getContents());
                         voices.put(currentVoice, voiceLines);
                     } else {
                         String voice = midtunefields.get(0).childrenByName(AbcGrammar.VOICE).get(0).childrenByName(AbcGrammar.TEXT).get(0)
                                         .getContents().trim();
                         currentVoice = voice;
                         if (!voices.containsKey(voice)) {
                             System.out.println(voice);
                             voices.put(voice, new ArrayList<>());
                         }
                     }
                     break;
                 }
                 
                 case KEYFIELD: {
                     ParseTree<AbcGrammar> keyNode = currentChild.childrenByName(AbcGrammar.KEY).get(0);
                     key = keyNode.getContents();
                     break;
                 }
                 
                 default: {
                     break;
                 }
             
             }
         }
         return new Header(title, index, key, composer, meter, length, tempoBaseNote, bpm, voices);
     }

  
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
      * @param increase boolean, if true: increase the pitch by semitone.
      *                          if false: decrease the pitch by semitone.
      *                          
      * @return the new modified pitch.
      */
     private Pitch modifyPitch(Pitch pitch, int semitone, boolean increase){
         Pitch newPitch;
         if (increase){
             newPitch = pitch.transpose(semitone);     
         }
         newPitch = pitch.transpose(-1*semitone);
           
         return newPitch;    
     }
     
     
     /**
      * builds an abstract syntax tree of a piece of Music from a ParseTree
      * @param tree the parse tree that is constructed according to the specified abc ntoation grammar
      * @return a Music that the parse tree represents
      */
     static Music buildMusic(ParseTree<AbcGrammar> tree, Header header) {
         
         String keySignature = header.getKey(); //Gets key signature of String
            
         //Mapping between scales with sharp key signatures and affected pitches. ([Major key, Minor key]: [sharp Notes]).
         Map<List<String>, List<Pitches>> sharpKeySignatures = new HashMap<>();
         
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>()); //0 sharps
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("G", "Em")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF))); //F#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("D", "Bm")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC))); //F#, C#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("A", "F#m")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG))); //F#, C#, G#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("E", "C#m")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD)));  //F#, C#, G#, D#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("B", "G#m")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA)));//F#, C#, G#, D#, A#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("F#", "D#m")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA, Pitches.SHARPE))); //F#, C#, G#, D#, A#, E#
         sharpKeySignatures.put(new ArrayList<>(Arrays.asList("C#", "A#m")), 
                 new ArrayList<>(Arrays.asList(Pitches.SHARPF, Pitches.SHARPC, Pitches.SHARPG, Pitches.SHARPD, Pitches.SHARPA, Pitches.SHARPE, Pitches.SHARPB))); //F#, C#, G#, D#, A#, E#, B#
         
         //Mapping between scales with flat key signatures and affected pitches. ([Major key, Minor key] : [flat Pitches]).
         Map<List<String>, List<Pitches>> flatKeySignatures = new HashMap<>();
         
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("C", "Am")), new ArrayList<>());   //0 flats
         
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("F", "Dm")), 
                 new ArrayList<>(Arrays.asList(Pitches.FLATB)));   //Bb
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Bb", "Gm")), 
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE))); //Bb, Eb
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Eb", "Cm")), 
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA))); //Bb, Eb, Ab
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Ab", "Fm")),
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD))); //Bb, Eb, Ab, Db
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Db", "Bbm")),
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG))); //Bb, Eb, Ab, Db, Gb
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Gb", "Ebm")),
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG, Pitches.FLATC))); //Bb, Eb, Ab, Db, Gb, Cb
         flatKeySignatures.put(new ArrayList<>(Arrays.asList("Cb", "Abm")),
                 new ArrayList<>(Arrays.asList(Pitches.FLATB, Pitches.FLATE, Pitches.FLATA, Pitches.FLATD, Pitches.FLATG, Pitches.FLATC, Pitches.FLATF))); //Bb, Eb, Ab, Db, Gb, Cb, Fb
           
     
     return null; 
     }
}
