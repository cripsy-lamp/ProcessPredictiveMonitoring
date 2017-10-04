package ltl2aut.automaton;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;

public class Automaton implements Iterable<State> {

	public class Operations {
		public DeterministicAutomaton determinize() {
			return NDFAOperations.getInstance().determinize(Automaton.this);
		}

		public Automaton intersect(final Automaton b) {
			return NDFAOperations.getInstance().intersect(Automaton.this, b);
		}

// public Automaton union(final Automaton b) {
// return NDFAOperations.getInstance().union(Automaton.this, b);
// }

		public boolean isAll() {
			return NDFAOperations.getInstance().isAll(Automaton.this);
		}

		public boolean isEmpty() {
			return NDFAOperations.getInstance().isEmpty(Automaton.this);
		}

// public Automaton minimize() {
// return NDFAOperations.getInstance().minimize(Automaton.this);
// }

		public Automaton reduce() {
			return NDFAOperations.getInstance().reduce(Automaton.this);
		}

		public Automaton renumber() {
			number();
			return Automaton.this;
		}

// public Automaton reverse() {
// return NDFAOperations.getInstance().reverse(Automaton.this);
// }
	}

	protected State init;

	protected int expandCount;

	public final Operations op = new Operations();
	protected int scount;

	public Automaton() {
		super();
		init = null;
		expandCount = 0;
		scount = 0;
	}

	public void addState(final State n) {
		scount++;
	}

	public void removeState(final State n) {
		if (n == init) {
			init = null;
		}
		scount--;
	}

	public int getExpandCount() {
		return expandCount;
	}

	public State getInit() {
		return init;
	}

	public int getStateCount() {
		return scount;
	}

	public int getTransitionCount() {
		int count = 0;
		for (final State s : this) {
			count += s.getOutputSize();
		}
		return count;
	}

	public void setExpandCount(final int expandCount) {
		this.expandCount = expandCount;
	}

	private synchronized void number() {
		int count = 0;
		for (final State s : this) {
			s.setId(count++);
		}
	}

	void setInitial(final State state) {
		init = state;
	}

	public Iterator<State> iterator() {
		return new Iterator<State>() {
			Set<State> states = setup(new HashSet<State>());
			Deque<State> unprocessed = setup(new LinkedList<State>());

			public boolean hasNext() {
				return !unprocessed.isEmpty();
			}

			private <T extends Collection<State>> T setup(final T set) {
				if (init != null) {
					set.add(init);
				}
				return set;
			}

			public State next() {
				if (!hasNext()) { throw new NoSuchElementException(); }
				final State state = unprocessed.removeFirst();
				for (final Transition t : state.getOutput()) {
					if (states.add(t.getTarget())) {
						unprocessed.addLast(t.getTarget());
					}
				}
				return state;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	public Iterable<Transition> transitions() {
		return new Iterable<Transition>() {
			public Iterator<Transition> iterator() {
				return new Iterator<Transition>() {
					Iterator<State> it = Automaton.this.iterator();
					Iterator<Transition> i;

					private void fill() {
						while (i == null || !i.hasNext()) {
							if (it.hasNext()) {
								i = it.next().getOutput().iterator();
							} else {
								return;
							}
						}
					}

					public boolean hasNext() {
						fill();
						return i != null && i.hasNext();
					}

					public Transition next() {
						fill();
						if (i == null) { throw new NoSuchElementException(); }
						return i.next();
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}

}