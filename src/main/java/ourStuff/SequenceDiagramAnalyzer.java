package ourStuff;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.ValueBox;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class SequenceDiagramAnalyzer extends AbstractAnalyzer {

	@Override
	public Data analyze(Data data) {
		
		return null;
	}
	
	private String recursiveBuilder(SootMethod m, int depth, Scene scene){
		if (depth > 5){
			return "";
		}
		List<UnitBox> lines = m.getActiveBody().getAllUnitBoxes();
		CallGraph cg = scene.getCallGraph();
		Iterator<Edge> edges = cg.edgesOutOf(m);
		
		while (edges.hasNext()){
			Edge e = edges.next();
			SootMethod meth = e.getTgt().method();
			e.getTgt().method().getDeclaringClass();
			recursiveBuilder(meth, depth+1, scene);
		}
		
		return null;
	}


}
