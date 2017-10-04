package ltl2aut.automaton;

import java.util.Collection;

public class Transition {
	private State source;
	private State target;
	private final TransitionLabel label;
	
	Transition(State source, State target, TransitionLabel label) {
		this.source = (source);
		this.target = (target);
		source.addOutput(this);
		this.label = label;
	}

	Transition(final State source, final State target) {
		this(source, target, new TransitionLabel());
	}

	Transition(final State source, final State target, final Collection<String> propositions) {
		this(source, target, new TransitionLabel(propositions));
	}

	Transition(final State source, final State target, final String proposition) {
		this(source, target, new TransitionLabel(proposition));
	}

	public Collection<String> getNegativeLabels() {
		return label.getNegativeLabels();
	}

	public String getPositiveLabel() {
		return label.getaLiteral().getImage();
	}

	public State getSource() {
		return source;
	}

	public State getTarget() {
		return target;
	}

	public boolean isAll() {
		return label.isAll();
	}

	public boolean isNegative() {
		return label.isNegative();
	}

	public boolean isPositive() {
		return label.isPositive();
	}

	public boolean parses(final String img) {
		return label.parsesImage(img);
	}

	public synchronized void remove() {
		source.removeOutput(this);
		source = null;
		target = null;
}

	@Override
	public String toString() {
		// return source + " - " + label.getImage() + " -> " + target;
		return label.getImage();
	}

	TransitionLabel getLabel() {
		return label;
	}

}