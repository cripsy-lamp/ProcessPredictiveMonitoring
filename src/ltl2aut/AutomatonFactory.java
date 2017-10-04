package ltl2aut;

import java.util.Collection;

import ltl2aut.automaton.Automaton;
import ltl2aut.automaton.Transition;
import ltl2aut.automaton.TransitionLabel;

public interface AutomatonFactory {
	public Automaton getAutomaton();

	void addAllTransition(Object sourceState, Object tragetState);

	void addNegativePropositionsTransition(Object sourceState, Object tragetState, Collection<String> propositions);

	void addPropositionTransition(Object sourceState, Object tragetState, String proposition);

	void addState(Object state);

	Object createState();

	void finished();

	int getStateId(Object state);

	void initialState(Object state);

	void removeOutgoingTransitions(Object s);

	void setExpandCount(int expand);

	void updateState(Object state, int id, boolean accepting);

	public void addTransition(Transition t);

	public void addTransition(Object source, Object target, TransitionLabel label);
}
