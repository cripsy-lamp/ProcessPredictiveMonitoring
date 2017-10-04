package ltl2aut.cup_parser;

import java.io.StringReader;

import ltl2aut.formula.Formula;

public class CupParser {
	public static Formula parse(final String ltl) throws Exception {
		final Scanner scanner = new Scanner(new StringReader(ltl));
		final ParserCup parser = new ParserCup(scanner);
		return (Formula) parser.parse().value;
	}
}
