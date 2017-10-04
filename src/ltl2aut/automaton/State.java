package ltl2aut.automaton;

import java.util.ArrayList;
import java.util.Collection;

public class State {

	private final Collection<Transition> output;
	private boolean accepting;
	private int id = 0;

	public State() {
		this(0);
	}

	public State(final int id) {
		super();
		setId(id);
		setAccepting(false);

		output = new ArrayList<Transition>();
	}

	public synchronized int getId() {
		return id;
	}

	public Iterable<Transition> getOutput() {
		return output;
	}

	public int getOutputSize() {
		return output.size();
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + id;
		return hash;
	}

	public boolean isAccepting() {
		return accepting;
	}

	public synchronized void removeAllTransitions() {
		output.clear();
	}

	@Override
	public String toString() {
		return "S" + id;
	}

	synchronized void addOutput(final Transition e) {
		output.add(e);
	}

	synchronized void removeOutput(final Transition e) {
		output.remove(e);
	}

	void setAccepting(final boolean acc) {
		accepting = acc;
	}

	synchronized void setId(final int id) {
		this.id = id;
	}
}
