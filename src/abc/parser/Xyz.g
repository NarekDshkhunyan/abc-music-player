root ::= header music

@skip WHITESPACE{
	header ::= number
	
	number ::= "X:" DIGIT+ eol
}


