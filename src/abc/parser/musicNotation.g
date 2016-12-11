root ::= music;

// Music

// WHITESPACE is explicit in the body, don't automatically ignore it

music ::= line+;
line ::= element* NEWLINE | midtunefield | comment;
element ::= noteelement | tupletelement | barline | nthrepeat | WHITESPACE;

@skip WHITESPACE{
voice ::= "V:" text eol;
}

noteelement ::= note | multinote;

// note is either a pitch or a rest;
note ::= noteorrest notelength?;
noteorrest ::= pitch | rest;
pitch ::= accidental? basenote octave?;
octave ::= "'"+ | ","+;
notelength ::= numerator? division?;
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

comment ::= "%" text? NEWLINE;
eol ::= comment | NEWLINE;
//text ::= WHITESPACE* (DIGIT | ALPHABET | '.')+ (WHITESPACE | DIGIT | ALPHABET | '.')*;
text ::= [^%\n\r]*;

DIGIT ::= [0-9];
numerator ::= DIGIT+;
denominator ::= DIGIT+;
division ::= "/" denominator?;
ALPHABET ::= [a-zA-Z];
NEWLINE ::= "\n" | "\r" "\n"?;
WHITESPACE ::= " " | "\t";
