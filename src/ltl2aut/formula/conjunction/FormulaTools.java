package ltl2aut.formula.conjunction;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ltl2aut.LTL2Automaton;
import ltl2aut.automaton.Automaton;
import ltl2aut.automaton.State;
import ltl2aut.automaton.Transition;
import ltl2aut.formula.Formula;
import ltl2aut.ltl.LTLFormula;

public class FormulaTools {
	public static  boolean containsNext(final Formula f) {
		if (f == null) return false;
		if (f.getType() == LTLFormula.NEXT) return true;
		if (containsNext(f.getLeft())) return true;
		return containsNext(f.getRight());
	}
	
	public static Collection<String> getAtomicPropositions(Formula f) {
		Set<String> result = new HashSet<String>();
		getAtomicPropositions(result, f);
		return result;
	}

	private static void getAtomicPropositions(Set<String> result, Formula f) {
		if (f == null) return;
		if (f.getType() == Formula.PROPOSITION) result.add(f.getName());
		getAtomicPropositions(result, f.getLeft());
		getAtomicPropositions(result, f.getRight());
	}

	public static boolean acceptsAllInit(Map<ConjunctionTreeNode, Automaton> cache, Formula formula) {
		try {
			ConjunctionTreeNode leaf = new ConjunctionTreeLeaf(formula, 'a');
			Automaton automaton = cache.get(leaf);
			if (automaton == null) {
				automaton = LTL2Automaton.getInstance().translate(formula).op.determinize().op.minimize();
				cache.put(leaf, automaton);
			}
			State init = automaton.getInit();
			for (Transition t: init.getOutput()) {
				if (t.getTarget() == init && (t.isAll() || t.isNegative())) {
					return true;
				}
			}
		} catch (Exception e) {
		}
		return false;
	}
}
