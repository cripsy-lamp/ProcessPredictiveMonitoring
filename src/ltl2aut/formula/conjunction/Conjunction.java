package ltl2aut.formula.conjunction;

import java.util.Collection;

import javax.naming.OperationNotSupportedException;

import ltl2aut.automaton.Automaton;
import ltl2aut.formula.Formula;

public interface Conjunction extends Iterable<Formula> {
	void add(Formula term) throws OperationNotSupportedException;

	void setAll(Collection<Formula> terms);

	void setAll(Formula terms);

	Automaton getAutomaton();

	void remove(Formula term) throws OperationNotSupportedException;
}
