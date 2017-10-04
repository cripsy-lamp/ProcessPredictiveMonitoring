package ltl2aut.formula.conjunction;

import ltl2aut.formula.Formula;

public class DefaultTreeFactory implements TreeFactory<ConjunctionTreeNode, ConjunctionTreeLeaf> {
	private final static DefaultTreeFactory instance = new DefaultTreeFactory();

	public ConjunctionTreeLeaf createLeaf(final Formula f, final char name) {
		return new ConjunctionTreeLeaf(f, name);
	}

	public ConjunctionTreeNode createNode(final ConjunctionTreeNode left,
			final ConjunctionTreeNode right) {
		return new ConjunctionTreeNode(left, right);
	}

	/**
	 * @return the instance
	 */
	public static TreeFactory<ConjunctionTreeNode, ConjunctionTreeLeaf> getInstance() {
		return instance;
	}

}
