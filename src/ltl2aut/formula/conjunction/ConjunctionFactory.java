package ltl2aut.formula.conjunction;

import java.util.Collection;

import ltl2aut.formula.Formula;

public interface ConjunctionFactory<T extends Conjunction> {
	public T instance();

	public T instance(Formula terms);

	public T instance(Collection<Formula> terms);
}
