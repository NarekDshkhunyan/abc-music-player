package abc.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import abc.sound.*;
import lib6005.parser.GrammarCompiler;
import lib6005.parser.ParseTree;
import lib6005.parser.Parser;
import lib6005.parser.UnableToParseException;

/** This file contains a test suite on the parser used for music
 *
 */
public class MusicParserTests {

    /*
     * Testing strategies for parsers:
     *  buildHeader(tree) --> header
     *      tree contains length, doesn't contain length
     *      tree contains tempo, doesn't contain tempo
     *      tree contains meter, doesn't contain meter
     *      tree contains composer, doesn't contain composer
     *      tree contains voice field in actual header
     *      tree contains voices in body
     *      
     *  buildMusic(tree, header) --> music
     *      tree contains repeats and nth repeats
     *      tree contains tuplets
     *      tree contains chords
     *      tree contains tuplets
     *      tree contains accidentals
     *      header contains key signature besides C/Am
     */
    
    private static File musicGrammarFile = new File("src/abc/parser/musicNotation.g");
    private static File abcNotationGrammarFile = new File("src/abc/parser/abcNotation.g");
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // Tests for buildHeader
    
    // covers tree doesn't contain length, tempo, meter, composer
    // single voice
    @Test
    public void testBuildHeaderOnlyRequiredFieldsPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/sample1.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct index", "1", header.getIndex());
        assertEquals("expected correct title", "sample 1", header.getTitle());
        assertTrue("expected correct key", header.getKey().equals("C") || header.getKey().equals("Am"));
        assertEquals("expected default length", "1/8", header.getLength());
        assertEquals("expected default tempo", "1/8", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 100, header.getTempoBPM());
        assertTrue("expected correct meter", header.getMeter().equals("4/4") || header.getMeter().equals("C"));
    }
    
    // covers case where meter is present but no length
    @Test
    public void testBuildHeaderMeterPresentNoLength() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/paddy.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected default length", "1/8", header.getLength());
        assertEquals("expected default tempo", "1/8", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 200, header.getTempoBPM());
        assertEquals("expected correct meter", "6/8", header.getMeter());        
    }
    
    // covers case where length is present but Q is not specified
    @Test
    public void testBuildHeaderNoQButLPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/noQ.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected default length", "1/2", header.getLength());
        assertEquals("expected default tempo", "1/2", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 100, header.getTempoBPM());
        assertEquals("expected correct meter", "4/4", header.getMeter());                
    }
    
    // covers everything but voices being present in the header of the file
    @Test
    public void testBuildHeaderEverythingPresent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/piece1.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct index", "1", header.getIndex());
        assertEquals("expected correct title", "Piece No.1", header.getTitle());
        assertTrue("expected correct key", header.getKey().equals("C") || header.getKey().equals("Am"));
        assertEquals("expected default length", "1/4", header.getLength());
        assertEquals("expected default tempo", "1/4", header.getTempoBaseNote());
        assertEquals("expected correct tempo", 140, header.getTempoBPM());
        assertTrue("expected correct meter", header.getMeter().equals("4/4") || header.getMeter().equals("C"));
        
    }
    
    // covers case where voices are put in body
    @Test
    public void testBuildHeaderVoicesInBody() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/invention.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct number of voices", 2, header.getVoices().keySet().size());
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("1"));
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("2"));        
    }
    
    // covers buildHeader voices are first stated in header
    @Test
    public void testBuildHeaderVoicesInHeader() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/fur_elise.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        assertEquals("expected correct number of voices", 2, header.getVoices().keySet().size());
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("1"));
        assertTrue("expected correct voice in voices", header.getVoices().containsKey("2"));
    }
    
    // buildMusic tests

    // covers the parsing of a single note
    @Test
    public void testBuildMusicOnlySingleNotes() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/sample3.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get("1"));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music music = MusicParser.buildMusic(musicTree, header);
        assertEquals("expected correct music", Music.concat(new Rest(0), Music.concat(new Rest(0), new Note(new Pitch('C'), 192.0))), music);
    }
    
    // covers the parsing of a tuplet
    @Test
    public void testBuildMusicTupletDifferentLengths() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/tuplet.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get(""));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music music = MusicParser.buildMusic(musicTree, header);
        Music C = new Note(new Pitch('C'), 128);
        Music G = new Note(new Pitch('G'), 384);
        Music E = new Note(new Pitch('E'), 512);

        Music tuplet = Music.concat(Music.concat(C, E), G);
        assertEquals("expected correct music", Music.concat(new Rest(0), Music.concat(new Rest(0),tuplet)), music);        
    }
    
    // covers the parsing of a chord
    @Test
    public void testBuildMusicChord() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/sample2.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get(""));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music music = MusicParser.buildMusic(musicTree, header);
        Music C = new Note(new Pitch('C'), 192);
        Music E = new Note(new Pitch('E'), 192);

        Music chord = Music.addVoice(C, E);
        assertEquals("expected correctly parsed chord [EC]", Music.concat(new Rest(0), Music.concat(new Rest(0), chord)), music);        
    }
    
    // covers application of key signature
    @Test
    public void testBuildMusicKeySignature() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/key_signature_test.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get(""));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music music = MusicParser.buildMusic(musicTree, header);
        Music midA = new Note(new Pitch('A').transpose(-1), 192);
        Music highA = new Note(new Pitch('A').transpose(11), 192);

        Music bar = Music.concat(Music.concat(new Rest(0), midA), highA);
        assertEquals("expected correctly parsed key signature effect", Music.concat(new Rest(0), bar), music);               
    }
    
    @Test
    public void testBuildMusicAccidental() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/accidental_test.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get(""));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music midAFlat = new Note(new Pitch('A').transpose(-1), 192);
        Music midASharp = new Note(new Pitch('A').transpose(1), 192);
        Music midANatural = new Note(new Pitch('A'), 192);
        Music highA = new Note(new Pitch('A').transpose(11), 192);

        
        Music expected = Music.concat(Music.concat(Music.concat(Music.concat(Music.concat(Music.concat(Music.concat(Music.concat(new Rest(0), midAFlat), highA), midASharp), midASharp), highA), midANatural), midANatural), midAFlat);
        System.out.println(expected);

        Music music = MusicParser.buildMusic(musicTree, header);
        System.out.println(music);
        assertEquals("expected correctly parsed key signature effect", Music.concat(new Rest(0), expected), music);                       
    }
    
    @Test
    public void testBuildMusicRepeatMajorSection() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/repeats_test.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get("Repeat Major Section"));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music A = new Note(new Pitch('A'), 192.0);
        Music B = new Note(new Pitch('B'), 192.0);
        Music C = new Note(new Pitch('C'), 192.0);
        Music D = new Note(new Pitch('D'), 192.0);
        
        Music oneBlock =Music.concat(Music.concat(Music.concat(Music.concat(new Rest(0), A), C), D) , B);
        Music expected = Music.concat(new Rest(0), Music.concat(oneBlock, oneBlock));
        Music music = MusicParser.buildMusic(musicTree, header);
        assertEquals("expected correctly parsed repeat", expected, music);                       
    }
    
    @Test
    public void testBuildMusicRepeatSubsection() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/repeats_test.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get("Repeat Within Repeat Block"));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music A = new Note(new Pitch('A'), 192.0);
        Music B = new Note(new Pitch('B'), 192.0);
        Music C = new Note(new Pitch('C'), 192.0);
        Music D = new Note(new Pitch('D'), 192.0);
        Music E = new Note(new Pitch('E'), 192.0);
                
        Music repeatBlock = Music.concat(Music.concat(Music.concat(Music.concat(new Rest(0), C), D), C), E);
        Music repeat = Music.concat(repeatBlock, repeatBlock);
        Music expected =Music.concat(new Rest(0), Music.concat(Music.concat(Music.concat(new Rest(0), A), B), repeat));
        Music music = MusicParser.buildMusic(musicTree, header);
        assertEquals("expected correctly parsed repeat", expected, music);                               
    }
    
    @Test
    public void testBuildMusicRepeatDifferentEndings() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/repeats_test.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get("Two Endings"));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music A = new Note(new Pitch('A'), 192.0);
        Music B = new Note(new Pitch('B'), 192.0);
        Music C = new Note(new Pitch('C'), 192.0);
        Music D = new Note(new Pitch('D'), 192.0);
        Music E = new Note(new Pitch('E'), 192.0);
                
        Music firstRepeatBlock = Music.concat(Music.concat(Music.concat(Music.concat(new Rest(0), A), B), new Rest(0)), C);
        Music secondRepeatBlock = Music.concat(Music.concat(Music.concat(new Rest(0), A), B), new Rest(0));
        Music repeat = Music.concat(firstRepeatBlock, secondRepeatBlock);
        Music expected = Music.concat(new Rest(0), Music.concat(Music.concat(new Rest(0), repeat), D));
        Music music = MusicParser.buildMusic(musicTree, header);
        assertEquals("expected correctly parsed repeat", expected, music);                               
    }
    
    @Test
    public void testBuildMusicDefaultNoteAndTempoBaseNoteAreDifferent() throws UnableToParseException, IOException {
        File musicFile = new File("sample_abc/different_tempo.abc");
        Parser<AbcGrammar> headerParser = GrammarCompiler.compile(abcNotationGrammarFile, AbcGrammar.ROOT);
        ParseTree<AbcGrammar> headerTree = headerParser.parse(musicFile);
        Header header = HeaderParser.buildHeader(headerTree);
        String musicString = String.join("", header.getVoices().get(""));
        Parser<MusicGrammar> musicParser = GrammarCompiler.compile(musicGrammarFile, MusicGrammar.ROOT);
        ParseTree<MusicGrammar> musicTree = musicParser.parse(musicString);
        Music midAQuarterNote = new Note(new Pitch('A'), 192.0*2);
        Music midAHalfNote = new Note(new Pitch('A'), 192.0*4);

        
        Music expected = Music.concat(Music.concat(new Rest(0), midAQuarterNote), midAHalfNote);
        System.out.println(expected);

        Music music = MusicParser.buildMusic(musicTree, header);
        assertEquals("expected correctly parsed key signature effect", Music.concat(new Rest(0), expected), music);                               
    }
}
