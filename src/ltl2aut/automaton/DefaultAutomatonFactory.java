package ltl2aut.automaton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ltl2aut.AutomatonFactory;

public class DefaultAutomatonFactory implements AutomatonFactory {

	private final Automaton automaton;

	public DefaultAutomatonFactory() {
		super();
		automaton = new Automaton();
	}

	public void addAllTransition(final Object sourceState, final Object tragetState) {
		final State source = convertState(sourceState);
		final State target = convertState(tragetState);
		if (source != null && target != null) {
			new Transition(source, target);
		}
	}

	public void addNegativePropositionsTransition(final Object sourceState, final Object tragetState,
	        final Collection<String> propositions) {
		final State source = convertState(sourceState);
		final State target = convertState(tragetState);
		if (source != null && target != null) {
			new Transition(source, target, propositions);
		}
	}

	public void addPropositionTransition(final Object sourceState, final Object tragetState, final String proposition) {
		final State source = convertState(sourceState);
		final State target = convertState(tragetState);
		if (source != null && target != null) {
			new Transition(source, target, proposition);
		}
	}

	public void addState(final Object state) {
		final State st = convertState(state);
		if (st != null) {
			automaton.addState(st);
		}
	}

	public Object createState() {
		return new State();
	}

	public void finished() {
	}

	public Automaton getAutomaton() {
		return automaton;
	}

	public int getStateId(final Object state) {
		int id = -1;
		final State st = convertState(state);
		if (st != null) {
			id = st.getId();
		}
		return id;
	}

	public void initialState(final Object state) {
		final State st = convertState(state);
		if (st != null) {
			automaton.setInitial(st);
		}
	}

	public void removeOutgoingTransitions(final Object s) {
		final State source = convertState(s);
		if (source != null) {
			final List<Transition> transitions = new ArrayList<Transition>();
			for (final Transition t : source.getOutput()) {
				transitions.add(t);
			}
			for (final Transition t : transitions) {
				t.remove();
			}
		}
	}

	public void setExpandCount(final int expand) {
		automaton.setExpandCount(expand);
	}

	public void updateState(final Object state, final int id, final boolean accepting) {
		final State st = convertState(state);
		if (st != null) {
			st.setId(id);
			st.setAccepting(accepting);
		}
	}

	private State convertState(final Object o) {
		State state = null;
		if (o instanceof State) {
			state = (State) o;
		}
		return state;
	}

	public void addTransition(final Transition t) {
		// TODO Auto-generated method stub

	}

	public void addTransition(final Object sourceState, final Object tragetState, final TransitionLabel label) {
		final State source = convertState(sourceState);
		final State target = convertState(tragetState);
		if (source != null && target != null) {
			new Transition(source, target, label);
		}

	}

}
