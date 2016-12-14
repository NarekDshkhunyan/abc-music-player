package abc.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import abc.sound.*;
import lib6005.parser.ParseTree;

public class HeaderParser {
                           
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
                     } else if (newMeter.equals("C")) {
                         meter = "4/4";
                     } else {
                         meter = "2/2";
                     }
                     if (!lengthDefined) {
                         String[] splitMeter = meter.split("/");
                         if (Double.parseDouble(splitMeter[0])/Double.parseDouble(splitMeter[1]) < 0.75) {
                             length = "1/16";
                         } else {
                             length = "1/8";
                         }
                     }
                     break; 
                 }
                 
                 case TEMPOFIELD: {
                     tempoDefined = true;
                     ParseTree<AbcGrammar> tempoNode = currentChild.childrenByName(AbcGrammar.TEMPO).get(0);
                     String tempo = tempoNode.getContents();
                     String[] splitTempo = tempo.split("=");
                     tempoBaseNote = splitTempo[0].trim();
                     bpm = Integer.parseInt(splitTempo[1].trim());
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
                     List<ParseTree<AbcGrammar>> midtunefields = currentChild.childrenByName(AbcGrammar.MIDTUNEFIELD);
                     //List<ParseTree<AbcGrammar>> elements = currentChild.childrenByName(AbcGrammar.ELEMENT);
                     if (midtunefields.isEmpty()) {
                         List<String> voiceLines = new ArrayList<>(voices.get(currentVoice));
                         voiceLines.add(currentChild.getContents());
                         voices.put(currentVoice, voiceLines);
                     } else {
                         String voice = midtunefields.get(0).childrenByName(AbcGrammar.VOICE).get(0).childrenByName(AbcGrammar.TEXT).get(0)
                                         .getContents().trim();
                         currentVoice = voice;
                         if (!voices.containsKey(voice)) {
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
         if (voices.get("").isEmpty()) {
             voices.remove("");
         }
         return new Header(title, index, key, composer, meter, length, tempoBaseNote, bpm, voices);
     } 
}