package ltl2aut.formula.conjunction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadedTreeFactory extends DefaultTreeFactory {
	private static final ThreadedTreeFactory instance = new ThreadedTreeFactory(Runtime
			.getRuntime().availableProcessors());
	private final ExecutorService threadPool;

	public ThreadedTreeFactory(final int threads) {
		threadPool = Executors.newFixedThreadPool(threads);
	}

	@Override
	public ConjunctionTreeNode createNode(final ConjunctionTreeNode left,
			final ConjunctionTreeNode right) {
		return new ThreadedConjunctionTreeNode(threadPool, left, right);
	}

	/**
	 * @return the instance
	 */
	public static TreeFactory<ConjunctionTreeNode, ConjunctionTreeLeaf> getInstance() {
		return instance;
	}

}
