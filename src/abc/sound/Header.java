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
    private final String composer;
    private final String title;
    private final String index;
    private final String key;
    private final Map<String, List<String>> voices;
    
    // Abstraction Function:
    //  represents header information of a piece of music in abc notation where:
    //      X: index
    //      T: title
    //      C: composer
    //      M: meter
    //      L: length
    //      Q: tempoBaseNote = tempoBPM
    //      for every voice in voices.keySet()
    //      V: voice
    //      K: key
    
    // Rep Invariant;
    //  none of the non-primitive fields are null
    //  tempoBPM > 0
    
    // Safety from rep exposure:
    //  all fields are marked as private and final. All fields except for voices is either an immutable String or a primitive
    //  a deep copy of the voices in the constructor is made before assigning it to this.voices. The observer for
    //  voices makes a deep copy before returning it.
    
 
    
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
            List<String> lines = new ArrayList<>(kvPair.getValue());
            voicesCopy.put(kvPair.getKey(), lines);
        }
        this.voices = voicesCopy;
        checkRep();
    }
        
    private void checkRep() {
        assert title != null;
        assert index != null;
        assert key != null;
        assert composer != null;
        assert meter != null;
        assert length != null;
        assert tempoBaseNote != null;
        assert tempoBPM > 0;
        assert voices != null;
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
        for (String voice : voices.keySet()) {
            sb.append("V: " + voice);
        }
        sb.append("K: " + key + "\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((composer == null) ? 0 : composer.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Header other = (Header) obj;
        if (composer == null) {
            if (other.composer != null)
                return false;
        } else if (!composer.equals(other.composer))
            return false;
        if (index == null) {
            if (other.index != null)
                return false;
        } else if (!index.equals(other.index))
            return false;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (length == null) {
            if (other.length != null)
                return false;
        } else if (!length.equals(other.length))
            return false;
        if (meter == null) {
            if (other.meter != null)
                return false;
        } else if (!meter.equals(other.meter))
            return false;
        if (tempoBPM != other.tempoBPM)
            return false;
        if (tempoBaseNote == null) {
            if (other.tempoBaseNote != null)
                return false;
        } else if (!tempoBaseNote.equals(other.tempoBaseNote))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (voices == null) {
            if (other.voices != null)
                return false;
        } else if (voices != other.voices)
            return false;
        return true;
    }

    /** @return the meter */
    public String getMeter() {
        return this.meter;
    }
    
    /** @return the length */
    public String getLength() {
        return this.length;
    }
    
    /** @return tempo base note */
    public String getTempoBaseNote() {
        return this.tempoBaseNote;
    }
    
    /** @return tempo bpm */
    public int getTempoBPM() {
        return this.tempoBPM;
    }
    
    /** @return the composer */
    public String getComposer() {
        return this.composer;
    }
    
    /** @return the title */
    public String getTitle() {
        return this.title;
    }
    
    /** @return the index */
    public String getIndex() {
        return this.index;
    }
    
    /** @return the key */
    public String getKey() {
        return this.key;
    }
    
    /** @return voices */
    public Map<String, List<String>> getVoices() {
        Map<String, List<String>> voicesCopy = new HashMap<>();
        for (Map.Entry<String, List<String>> kvPair : voices.entrySet()) {
            List<String> lines = new ArrayList<>(kvPair.getValue());
            if (!lines.isEmpty()) {
                voicesCopy.put(kvPair.getKey(), lines);
            }
        }
        return voicesCopy;
    }
    
}
