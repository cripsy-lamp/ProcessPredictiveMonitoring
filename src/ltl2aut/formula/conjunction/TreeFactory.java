package ltl2aut.formula.conjunction;

import ltl2aut.formula.Formula;

public interface TreeFactory<N, L extends N> {
	N createNode(ConjunctionTreeNode left, ConjunctionTreeNode right);

	L createLeaf(Formula f, char name);
}
