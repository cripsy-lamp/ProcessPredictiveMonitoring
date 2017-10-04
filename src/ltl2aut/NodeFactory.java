package ltl2aut;

interface NodeFactory {

	void collapse();

	Node create();

	Node create(Node node);

	Node createInitial();

	int generateNextId();

	int getExpandCount();

	int getSize();

	boolean isCollapsed();

	void newExpand();
}
