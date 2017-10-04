package ltl2aut;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import ltl2aut.automaton.Automaton;
import ltl2aut.automaton.DOTExporter;
import ltl2aut.automaton.DefaultAutomatonFactory;
import ltl2aut.automaton.DeterministicAutomaton;
import ltl2aut.cup_parser.CupParser;
import ltl2aut.formula.Formula;
import ltl2aut.ltl.LTLFormula;

public class LTL2Automaton {
	private static LTL2Automaton instance = new LTL2Automaton();

	private static int f = 0;

	private static final boolean traceGeneration = false;

	public static LTL2Automaton getInstance() {
		return LTL2Automaton.instance;
	}

	protected LTL2Automaton() {
		super();
	}

	public Formula parseFormula(final String ltl, final boolean negate) throws Exception {
		if (negate) {
			return CupParser.parse("!(" + ltl + ")");
		} else {
			return CupParser.parse(ltl);
		}
	}

	public Automaton translate(final Formula formula) throws Exception {
		return translate(formula, true, true, true, false, true, null);
	}

	public void translate(final Formula formula, final AutomatonFactory factory, final boolean singleProperty)
	        throws Exception {
		final Graph graph = createGraph(formula, factory, singleProperty);
		graph.createAutomaton();
	}

	public Automaton translate(final Formula ltl, final boolean singleProperty, final boolean useAutomatonProperties,
	        final boolean minimizeSubAutomata, final boolean deep, final boolean deterministic,
	        final Map<String, DeterministicAutomaton> cache) throws Exception {
		if (!useAutomatonProperties) { return translateSimple(ltl, singleProperty); }
		if (deterministic) { return translateDeterministicAutomatonOperation(ltl, minimizeSubAutomata, deep, cache); }
		return translateNondeterministicAutomatonOperation(ltl, minimizeSubAutomata);
	}

	public Automaton translate(final String ltl) throws Exception {
		return translate(parseFormula(ltl, false));
	}

	public DeterministicAutomaton translateDeterministicAutomatonOperation(final Formula formula,
	        final boolean minimizeSubAutomata, final boolean deep, final Map<String, DeterministicAutomaton> cache)
	        throws Exception {
		final int myF = LTL2Automaton.f++;
		if (LTL2Automaton.traceGeneration) {
			System.out.println("Translating f" + myF);
		}
		final DeterministicAutomaton automaton = translateDeterministic(formula, minimizeSubAutomata, deep, cache, "");
		if (LTL2Automaton.traceGeneration) {
			export(automaton, myF, "");
		}
		if (LTL2Automaton.traceGeneration) {
			export(automaton.op.complete(), myF, "c");
		}
		final DeterministicAutomaton minimized = automaton.op.minimize();
		if (LTL2Automaton.traceGeneration) {
			export(minimized, myF, "m");
		}
		final DeterministicAutomaton result = minimized; // minimized.op.reduce();
		if (LTL2Automaton.traceGeneration) {
			export(result, myF, "r");
		}
		return result;
	}

	public Automaton translateNondeterministicAutomatonOperation(final Formula formula,
	        final boolean minimizeSubAutomata) throws Exception {
		final int myF = LTL2Automaton.f++;
		if (LTL2Automaton.traceGeneration) {
			System.out.println("Translating f" + myF);
		}
		final Automaton automaton = translateNondeterministic(formula, minimizeSubAutomata, "");
		if (LTL2Automaton.traceGeneration) {
			export(automaton, myF, "");
		}
		final Automaton minimized = automaton;// automaton.op.minimize();
		if (LTL2Automaton.traceGeneration) {
			export(minimized, myF, "m");
		}
		final Automaton result = minimized; // minimized.op.reduce();
		if (LTL2Automaton.traceGeneration) {
			export(result, myF, "r");
		}
		return result;
	}

	public Automaton translateSimple(final Formula formula, final boolean singleProperty) throws Exception {
		final AutomatonFactory factory = new DefaultAutomatonFactory();
		translate(formula, factory, singleProperty);
		return factory.getAutomaton();
	}

	private Graph createGraph(final Formula formula, final AutomatonFactory factory, final boolean singleProperty) {
		Graph graph = null;
		if (singleProperty) {
			graph = new SinglePropertyGraph(formula, factory);
		} else {
			graph = new Graph(formula, factory);
		}

		return graph;
	}

	private void export(final Automaton a, final int f, final String postfix) {
		try {
			final FileWriter w = new FileWriter("f" + f + postfix + ".dot");
			DOTExporter.exportToDot(a, "f" + f + postfix, w);
			w.close();
		} catch (final IOException e) {
		}
	}

	private Automaton minimize(final Automaton a, final boolean minimizeSubAutomata) {
	//	if (minimizeSubAutomata) { return a.op.minimize(); }
		return a;
	}

	private DeterministicAutomaton minimize(final DeterministicAutomaton a, final boolean minimizeSubAutomata) {
		if (minimizeSubAutomata) { return a.op.minimize(); }
		return a;
	}

	private DeterministicAutomaton translateDeterministic(final Formula formula, final boolean minimizeSubAutomata,
	        final boolean deep, final Map<String, DeterministicAutomaton> cache, final String indent) {
		final int myF = LTL2Automaton.f++;
		if (deep && formula.getType() == LTLFormula.NOT) {
			if (LTL2Automaton.traceGeneration) {
				System.out.println(indent + "NOT f" + myF);
			}
			DeterministicAutomaton a = translateDeterministic(formula.getLeft(), minimizeSubAutomata, deep, cache,
			        indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "");
			}
			a = minimize(a, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "m");
			}
			return a.op.negate();
		}
		if (deep && formula.getType() == LTLFormula.OR) {
			if (LTL2Automaton.traceGeneration) {
				System.out.println(indent + "OR f" + myF);
			}
			DeterministicAutomaton a = translateDeterministic(formula.getLeft(), minimizeSubAutomata, deep, cache,
			        indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "a");
			}
			a = minimize(a, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "am");
			}
			if (a.op.isAll()) { return a; }
			DeterministicAutomaton b = translateDeterministic(formula.getRight(), minimizeSubAutomata, deep, cache,
			        indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "b");
			}
			b = minimize(b, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "bm");
			}
			if (a.op.isEmpty()) { return b; }
			if (b.op.isAll()) { return b; }
			if (b.op.isEmpty()) { return a; }
			return a.op.union(b);
		}
		if (formula.getType() == LTLFormula.AND) {
			if (LTL2Automaton.traceGeneration) {
				System.out.println(indent + "AND f" + myF);
			}
			DeterministicAutomaton a = translateDeterministic(formula.getLeft(), minimizeSubAutomata, deep, cache,
			        indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "a");
			}
			a = minimize(a, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "am");
			}
			if (a.op.isEmpty()) { return a; }
			DeterministicAutomaton b = translateDeterministic(formula.getRight(), minimizeSubAutomata, deep, cache,
			        indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "b");
			}
			b = minimize(b, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "bm");
			}
			if (a.op.isAll()) { return b; }
			if (b.op.isEmpty()) { return b; }
			if (b.op.isAll()) { return a; }
			return a.op.intersect(b);
		}
		if (LTL2Automaton.traceGeneration) {
			System.out.println(indent + "PRIM " + formula);
		}
		String key = null;
		if (cache != null) {
			key = formula.toString();
			final DeterministicAutomaton result = cache.get(key);
			if (result != null) { return result; }
		}
		// System.out.println(indent + "PRIM");
		final AutomatonFactory factory = new DefaultAutomatonFactory();
		final Graph graph = createGraph(formula, factory, true);
		graph.createAutomaton();
		final DeterministicAutomaton result = factory.getAutomaton().op.determinize();
		if (cache != null) {
			cache.put(key, result);
		}
		return result;
	}

	private Automaton translateNondeterministic(final Formula formula, final boolean minimizeSubAutomata,
	        final String indent) {
		final int myF = LTL2Automaton.f++;
		if (formula.getType() == LTLFormula.AND) {
			if (LTL2Automaton.traceGeneration) {
				System.out.println(indent + "AND f" + myF);
			}
			Automaton a = translateNondeterministic(formula.getLeft(), minimizeSubAutomata, indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "a");
			}
			a = minimize(a, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(a, myF, "am");
			}
			if (a.op.isEmpty()) { return a; }
			Automaton b = translateNondeterministic(formula.getRight(), minimizeSubAutomata, indent + "  ");
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "b");
			}
			b = minimize(b, minimizeSubAutomata);
			if (LTL2Automaton.traceGeneration) {
				export(b, myF, "bm");
			}
			if (a.op.isAll()) { return b; }
			if (b.op.isEmpty()) { return b; }
			if (b.op.isAll()) { return a; }
			return a.op.intersect(b);
		}
		if (LTL2Automaton.traceGeneration) {
			System.out.println(indent + "PRIM " + formula);
		}
		// System.out.println(indent + "PRIM");
		final AutomatonFactory factory = new DefaultAutomatonFactory();
		final Graph graph = createGraph(formula, factory, true);
		graph.createAutomaton();
		return factory.getAutomaton();
	}
}