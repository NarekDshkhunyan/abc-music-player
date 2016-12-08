package abc.parser;

public class MusicGrammar {
    
    public enum AbcMusicGrammar {
        ROOT, 
        MUSIC,
        VOICE,
        LINE,
        ELEMENT,
        NOTEELEMENT,
        NOTE,
        NOTEORREST,
        PITCH,
        OCTAVE,
        NOTELENGTH,
        NOTELENGTHSTRICT,
        ACCIDENTAL,
        BASENOTE,
        REST,
        TUPLETELEMENT,
        TUPLETSPEC,
        MULTINOTE,
        BARLINE,
        NTHREPEAT,
        MIDTUNEFIELD,
        COMMENT,
        EOL,
        TEXT,
        DIGIT,
        ALPHABET,
        NEWLINE,
        WHITESPACE
    }

}
