package ltl2aut.formula;

import ltl2aut.ltl.Parser;

public class DefaultParser extends Parser<Formula> {

	public DefaultParser(final String ltl) {
		super(ltl, DefaultFormulaFactory.instance);
	}

}
