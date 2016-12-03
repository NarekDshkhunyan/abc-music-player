line: PLUS EOF;
PLUS: '+';

abc-tune ::= abc-header abc-music

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Header

; ignore WHITESPACE between terminals in the header

abc-header ::= field-number comment* field-title other-fields* field-key

field-number ::= "X:" DIGIT+ end-of-line
field-title ::= "T:" text end-of-line
other-fields ::= field-composer | field-default-length | field-meter | field-tempo | field-voice | comment
field-composer ::= "C:" text end-of-line
field-default-length ::= "L:" note-length-strict end-of-line
field-meter ::= "M:" meter end-of-line
field-tempo ::= "Q:" tempo end-of-line
field-voice ::= "V:" text end-of-line
field-key ::= "K:" key end-of-line

key ::= keynote mode-minor?
keynote ::= basenote key-accidental?
key-accidental ::= "#" | "b"
mode-minor ::= "m"

meter ::= "C" | "C|" | meter-fraction
meter-fraction ::= DIGIT+ "/" DIGIT+

tempo ::= meter-fraction "=" DIGIT+

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Music

; WHITESPACE is explicit in the body, don't automatically ignore it

abc-music ::= abc-line+
abc-line ::= element* NEWLINE | mid-tune-field | comment
element ::= note-element | tuplet-element | barline | nth-repeat | WHITESPACE 

note-element ::= note | multi-note

;; note is either a pitch or a rest
note ::= note-or-rest note-length?
note-or-rest ::= pitch | rest
pitch ::= accidental? basenote octave?
octave ::= "'"+ | ","+
note-length ::= (DIGIT+)? ("/" (DIGIT+)?)?
note-length-strict ::= DIGIT+ "/" DIGIT+

;; "^" is sharp, "_" is flat, and "=" is neutral
accidental ::= "^" | "^^" | "_" | "__" | "="

basenote ::= "C" | "D" | "E" | "F" | "G" | "A" | "B"
        | "c" | "d" | "e" | "f" | "g" | "a" | "b"

rest ::= "z"

;; tuplets
tuplet-element ::= tuplet-spec note-element+
tuplet-spec ::= "(" DIGIT 

;; chords
multi-note ::= "[" note+ "]"

barline ::= "|" | "||" | "[|" | "|]" | ":|" | "|:"
nth-repeat ::= "[1" | "[2"

; A voice field might reappear in the middle of a piece
; to indicate the change of a voice
mid-tune-field ::= field-voice

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; General

comment ::= "%" text NEWLINE
end-of-line ::= comment | NEWLINE

DIGIT ::= [0-9]
NEWLINE ::= "\n" | "\r" "\n"?
WHITESPACE ::= " " | "\t"