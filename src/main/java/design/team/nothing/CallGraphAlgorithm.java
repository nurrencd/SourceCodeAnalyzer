package design.team.nothing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.Chain;

public class CallGraphAlgorithm implements Algorithm {

	@Override
	public List<SootMethod> resolve(SootMethod m, Unit u, Scene s) {
		List<SootMethod> returnList = new ArrayList<SootMethod>();
		CallGraph cg = s.getCallGraph();
		Iterator<Edge> iter = cg.edgesOutOf(u);
		while (iter.hasNext()) {
			SootMethod temp = iter.next().getTgt().method();
			returnList.add(temp);
		}

		return returnList;
	}

}
