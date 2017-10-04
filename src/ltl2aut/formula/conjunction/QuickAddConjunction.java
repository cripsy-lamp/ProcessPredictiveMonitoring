package ltl2aut.formula.conjunction;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import ltl2aut.automaton.Automaton;
import ltl2aut.formula.Formula;

public class QuickAddConjunction implements Conjunction {
	private final UnsortedTreeConjunction base;
	protected char nextName = 'A';
	int badness = 0;

	public static ConjunctionFactory<? extends QuickAddConjunction> getFactory(
			final ConjunctionFactory<? extends UnsortedTreeConjunction> base) {
		return new ConjunctionFactory<QuickAddConjunction>() {
			public QuickAddConjunction instance(final Collection<Formula> terms) {
				return new QuickAddConjunction(base.instance(terms));
			}

			public QuickAddConjunction instance(final Formula terms) {
				return new QuickAddConjunction(base.instance(terms));
			}

			public QuickAddConjunction instance() {
				return new QuickAddConjunction(base.instance());
			}
		};
	}

	public QuickAddConjunction(final UnsortedTreeConjunction base) {
		this.base = base;
	}

	public QuickAddConjunction(final UnsortedTreeConjunction base,
			final Collection<Formula> formulas) {
		this.base = base;
		setAll(formulas);
	}

	public QuickAddConjunction(final UnsortedTreeConjunction base, final Formula f) {
		this.base = base;
		setAll(f);
	}

	public synchronized void add(final Formula term) throws OperationNotSupportedException {
		if (base.root == null) {
			base.add(term);
		} else {
			badness++;
			base.root = base.getTreeFactory().createNode(base.root,
					base.getTreeFactory().createLeaf(term, nextName++));
		}
	}

	public Automaton getAutomaton() {
		return base.getAutomaton();
	}

	public synchronized void remove(final Formula term) throws OperationNotSupportedException {
		if (badness > 0) {
			rebalance();
		}
		base.remove(term);
	}

	public synchronized void rebalance() {
		badness = 0;
		if (base.root == null) return;
		final Map<ConjunctionTreeNode, Automaton> oldCaches = CacheTools.getCached(base.root);
		Automaton rootCache = null;
		try {
			rootCache = base.root.cache.get();
		} catch (final NullPointerException e) {
			// rootCache remains null
		}
		final List<Formula> formulas = new ArrayList<Formula>();
		for (final Formula f : base) {
			formulas.add(f);
		}
		base.setAll(formulas);
		CacheTools.setCaches(oldCaches, base.root);
		if (rootCache != null) {
			base.root.cache = new SoftReference<Automaton>(rootCache);
		}
	}


	public synchronized void setAll(final Collection<Formula> terms) {
		base.setAll(terms);
	}

	public Iterator<Formula> iterator() {
		return base.iterator();
	}

	public synchronized void setAll(final Formula terms) {
		base.setAll(terms);
	}
}
