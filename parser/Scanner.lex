package ltl2aut.cup_parser;

import java_cup.runtime.*;
import static ltl2aut.cup_parser.ParserSym.*;
import java.io.IOException;

%%

%class Scanner
%cup
%unicode
%line
%column
%public

%{

	private Symbol symbol(int type) {
		return new Symbol(type, yyline, yycolumn);
	}

	private Symbol symbol(int type, Object value) {
		return new Symbol(type, yyline, yycolumn, value);
	}
%}

LineTerminator			= \r|\n|\r\n
InputCharacter			= [^\r\n]
WhiteSpace				= {LineTerminator} | [ \t\f]

/* comments */
Comment					= {TraditionalComment}
						| {EndOfLineComment}
						| {DocumentationComment}

TraditionalComment		= "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment		= "//" {InputCharacter}* {LineTerminator}
DocumentationComment	= "/**" {CommentContent} "*"+ "/"
CommentContent          = ( [^*] | \*+ [^/*] )*

%%

<YYINITIAL>{
	"("					{ return symbol(LPAREN); }
	")"					{ return symbol(RPAREN); }
	"true"				{ return symbol(TRUE); }
	"false"				{ return symbol(FALSE); }
	"="					{ return symbol(EQUALS); }
	"<->"				{ return symbol(EQUALS); }
	"!"					{ return symbol(NOT); }
	"/\\"				{ return symbol(AND); }
	"\\/"				{ return symbol(OR); }
	"&&"				{ return symbol(AND); }
	"||"				{ return symbol(OR); }
	"->"				{ return symbol(IMPLIES); }
	"\[\]"				{ return symbol(GLOBALLY); }
	"<>"				{ return symbol(FUTURE); }
	"X"					{ return symbol(NEXT); }
//	"Y"					{ return symbol(WNEXT); }
	"U"					{ return symbol(UNTIL); }
	"W"					{ return symbol(WUNTIL); }
	"V"					{ return symbol(RELEASES); }
	"M"					{ return symbol(WRELEASES); }
	"_O"					{ return symbol(NEXT); }
//	"Y"					{ return symbol(WNEXT); }
	"_U"					{ return symbol(UNTIL); }
	"_W"					{ return symbol(WUNTIL); }
	"_V"					{ return symbol(RELEASES); }
	"_M"					{ return symbol(WRELEASES); }
	[\"][^\"]*[\"]		{ String result = yytext(); return symbol(ID, result.substring(1, result.length() - 1)); } 
	[a-zA-Z_$][a-zA-Z0-9_]*
						{ return symbol(ID, yytext()); }
	/* comments */
	{Comment}			{ /* ignore */ }

	/* whitespace */
	{WhiteSpace}		{ /* ignore */ }
}

/* error fallback */
.|\n					{ throw new IOException("Illegal character <" + yytext() + "> at line " + yyline + ", position " + yycolumn); }