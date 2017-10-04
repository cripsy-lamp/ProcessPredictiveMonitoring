package ltl2aut.formula.conjunction;

import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ltl2aut.LTL2Automaton;
import ltl2aut.automaton.Automaton;
import ltl2aut.formula.Formula;

public class ConjunctionTreeLeaf extends ConjunctionTreeNode {
	private Formula f;
	private final char name;

	public ConjunctionTreeLeaf(final Formula f, final char name) {
		this.name = name;
		setFormula(f);
	}

	@Override
	public int conjunctions() {
		return 1;
	}

	@Override
	public synchronized Automaton getAutomaton() {
		if (cache == null || cache.get() == null) {
			cache = new SoftReference<Automaton>(process(translate(f)));
		}
		final Automaton automaton = cache.get();
		if (automaton == null) return getAutomaton();
		return automaton;
	}

	/**
	 * @return the f
	 */
	public Formula getFormula() {
		return f;
	}

	@Override
	public int getHeight() {
		return 1;
	}

	@Override
	public Iterator<Formula> iterator() {
		return new Iterator<Formula>() {
			boolean done = false;

			public boolean hasNext() {
				return !done;
			}

			public Formula next() {
				if (done) throw new NoSuchElementException();
				done = true;
				return f;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	/**
	 * @param f
	 *            the f to set
	 */
	public void setFormula(final Formula f) {
		this.f = f;
		invalidate();
	}

	@Override
	public synchronized boolean equals(final Object o) {
		if (o == null) return false;
		if (!(o instanceof ConjunctionTreeLeaf)) return false;
		final ConjunctionTreeLeaf other = (ConjunctionTreeLeaf) o;
		if (f == null && other.f != null) return false;
		if (f != null && !f.equals(other.f)) return false;
		return true;
	}

	@Override
	public int hashCode() {
		return f == null ? 0 : f.hashCode();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		if (cache != null) {
			sb.append('<');
		}
		sb.append(name);
		if (cache != null) {
			final Automaton automaton = cache.get();
			if (automaton != null) {
				sb.append(">\t");
				sb.append(automaton.getStateCount());
				sb.append(" - ");
				sb.append(automaton.getTransitionCount());
				sb.append("\t\t - ");
				sb.append(Integer.toHexString(cache.hashCode()));
				sb.append(" - ");
				sb.append(Integer.toHexString(f.hashCode()));

			} else {
				sb.append("\t\t -            ");
				sb.append(Integer.toHexString(f.hashCode()));
			}
		} else {
			sb.append("\t\t -            ");
			sb.append(Integer.toHexString(f.hashCode()));
		}

		return sb.toString();

	}

	private Automaton translate(final Formula f) {
		try {
			return LTL2Automaton.getInstance().translate(f);
		} catch (final Exception e) {
			return null;
		}
	}
}
