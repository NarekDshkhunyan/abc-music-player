package abc.sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Header class represents the header of a piece of music in abc notation
 *
 */
public class Header {
    
    public static final String DEFAULT_METER = "4/4";
    
    public static final double DEFAULT_LENGTH = 1/8;
    
    public static final int DEFAULT_TEMPO_BPM = 100;
    
    public static final String DEFAULT_COMPOSER = "Unknown";
    
    private final String meter;
    private final String length;
    private final String tempoBaseNote;
    private final int tempoBPM;
    private final String composer ;
    private final String title;
    private final String index;
    private final String key;
    private final Map<String, List<String>> voices;
    
 
    
    public Header(String title, String index, String key, String composer, String meter, String length, String tempoBaseNote, int tempoBPM,
                    Map<String, List<String>> voices) {
        this.title = title;
        this.index = index;
        this.key = key;
        this.composer = composer;
        this.meter = meter;
        this.length = length;
        this.tempoBaseNote = tempoBaseNote;
        this.tempoBPM = tempoBPM;
        Map<String, List<String>> voicesCopy = new HashMap<>();
        for (Map.Entry<String, List<String>> kvPair : voices.entrySet()) {
            List<String> lines = new ArrayList<>();
            for (String line : kvPair.getValue()) {
                lines.add(line);
            }
            voicesCopy.put(kvPair.getKey(), lines);
        }
        this.voices = voicesCopy;
    }
    
    // TODO: Observers for each field
    
    private void checkRep() {
        assert title.trim().length() > 0; 
        assert index.trim().length() > 0; 
        assert key.trim().length() > 0; 
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("X: " + index + "\n");
        sb.append("T: " + title + "\n");
        sb.append("C: " + composer + "\n");
        sb.append("L: " + length + "\n");
        sb.append("M: " + meter + "\n");
        sb.append("Q: " + tempoBaseNote + "=" + tempoBPM + "\n");
        sb.append("K: " + key + "\n");
        sb.append("voices: " + voices.toString());
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((composer == null) ? 0 : composer.hashCode());
        result = prime * result + index.hashCode();
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((length == null) ? 0 : length.hashCode());
        result = prime * result + ((meter == null) ? 0 : meter.hashCode());
        result = prime * result + tempoBPM;
        result = prime * result + ((tempoBaseNote == null) ? 0 : tempoBaseNote.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((voices == null) ? 0 : voices.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof Header)) return false;
        Header thatHeader = (Header) thatObject;
        return this == thatHeader;
    }
    
    /** Returns the meter */
    public String getMeter() {
        return this.meter;
    }
    
    /** Returns the length */
    public String getLength() {
        return this.length;
    }
    
    /** Returns tempo base note */
    public String getTempoBaseNote() {
        return this.tempoBaseNote;
    }
    
    /** Returns tempo bpm */
    public int getTempoBPM() {
        return this.tempoBPM;
    }
    
    /** Returns the composer */
    public String getComposer() {
        return this.composer;
    }
    
    /** Returns the title */
    public String getTitle() {
        return this.title;
    }
    
    /** Returns the index */
    public String getIndex() {
        return this.index;
    }
    
    /** Returns the key */
    public String getKey() {
        return this.key;
    }
    
    /** Returns the voices */
    public Map<String, List<String>> getVoices() {
        return this.voices;
    }
    
}
