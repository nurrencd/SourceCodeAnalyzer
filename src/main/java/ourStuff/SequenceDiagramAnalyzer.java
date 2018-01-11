package ourStuff;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.ValueBox;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class SequenceDiagramAnalyzer extends AbstractAnalyzer {
	private final int DEFAULT_DEPTH = 5;
	private final String DEPTH_KEY = "-d";

	private String mSig;
	private int maxDepth;

	public SequenceDiagramAnalyzer(String mSig) {
		this.maxDepth = DEFAULT_DEPTH;
		this.mSig = mSig;
	}

	@Override
	public Data analyze(Data data) {
		if (data.config.containsKey(DEPTH_KEY)) {
			this.maxDepth = Integer.parseInt(data.config.get(DEPTH_KEY).get(0));
		}
		SootMethod entry = data.scene.getMethod(this.mSig);
		StringBuilder codeBuilder = new StringBuilder();
		codeBuilder.append("@startuml");
		codeBuilder.append(recursiveBuilder(entry, 0, data.scene));
		codeBuilder.append("@enduml");
		System.out.println(codeBuilder.toString());
		try {
			FileCreator fc = new FileCreator();
			fc.getSVG(codeBuilder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String recursiveBuilder(SootMethod m, int depth, Scene scene) {
		System.out.println(depth);
		if (depth > this.maxDepth) { // Default based on specs
			return "";
		}
		CallGraph cg = scene.getCallGraph();
		Iterator<Edge> edges = cg.edgesOutOf(m);
		StringBuilder sb = new StringBuilder();
		SootClass scFrom = m.getDeclaringClass();

		sb.append("activate " + scFrom.getName());
		sb.append("\n");
		while (edges.hasNext()) {
			Edge e = edges.next();
			SootMethod meth = e.getTgt().method();
			SootClass scTo = meth.getDeclaringClass();
			if (!this.applyFilters(scTo)) {
				sb.append(scFrom.getName() + " -> " + scTo.getName() + ": " + meth.getName());
				sb.append("\n");
				sb.append(recursiveBuilder(meth, depth + 1, scene));
				sb.append("\n");
				sb.append(scTo.getName() + " --> " + scFrom.getName());
				sb.append("\n");
			}
		}
		sb.append("deactivate " + scFrom.getName());
		sb.append("\n");
		return sb.toString();
	}

}
