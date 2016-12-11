// A subset of abc 2.1 in BNF format

root ::= header music;

// Header

// ignore WHITESPACE between terminals in the header


@skip WHITESPACE{

	header ::= number comment* title others* keyfield;
	
	number ::= "X:" DIGIT+ eol;
	title ::= "T:" text eol;
	others ::= composer | defaultlength | meterfield | tempofield | voice | comment;
	composer ::= "C:" text eol;
	defaultlength ::= "L:"  notelengthstrict eol;
	meterfield ::= "M:" meter eol;
	tempofield ::= "Q:" tempo eol;
	voice ::= "V:" text eol;
	keyfield ::= "K:" key eol;
	
	key ::= keynote modeminor?;
	keynote ::= basenote keyaccidental?;
	keyaccidental ::= "#" | "b";
	modeminor ::= "m";
	
	meter ::= "C" | "C|" | meterfraction;
	meterfraction ::= DIGIT+ "/" DIGIT+;
	
	tempo ::= meterfraction "=" DIGIT+;
}

// Music

// WHITESPACE is explicit in the body, don't automatically ignore it

music ::= line+;
line ::= element* NEWLINE | midtunefield | comment;
element ::= noteelement | tupletelement | barline | nthrepeat | WHITESPACE;

noteelement ::= note | multinote;

// note is either a pitch or a rest;
note ::= noteorrest notelength?;
noteorrest ::= pitch | rest;
pitch ::= accidental? basenote octave?;
octave ::= "'"+ | ","+;
notelength ::= (DIGIT+)? ("/" (DIGIT+)?)?;
notelengthstrict ::= DIGIT+ "/" DIGIT+;

// "^" is sharp, "_" is flat, and "=" is neutral
accidental ::= "^" | "^^" | "_" | "__" | "=";

basenote ::= "C" | "D" | "E" | "F" | "G" | "A" | "B" | "c" | "d" | "e" | "f" | "g" | "a" | "b";

rest ::= "z";

// tuplets
tupletelement ::= tupletspec noteelement+;
tupletspec ::= "(" DIGIT;

// chords
multinote ::= "[" note+ "]";

barline ::= "|" | "||" | "[|" | "|]" | ":|" | "|:";
nthrepeat ::= "[1" | "[2";

// A voice field might reappear in the middle of a piece
// to indicate the change of a voice
midtunefield ::= voice;


// General

comment ::= "%" (text)? NEWLINE;
eol ::= comment | NEWLINE;

text ::= [^%\n\r]*;

DIGIT ::= [0-9];
ALPHABET ::= [a-zA-Z];
NEWLINE ::= "\n" | "\r" "\n"?;
WHITESPACE ::= " " | "\t";