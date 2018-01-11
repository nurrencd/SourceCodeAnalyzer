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

	String mSig;
	
	public SequenceDiagramAnalyzer(String mSig) {
		this.mSig = mSig;
	}
	
	@Override
	public Data analyze(Data data) {
		SootMethod entry = data.scene.getMethod(this.mSig);
		String result = recursiveBuilder(entry, 0, data.scene);
		System.out.println(result);
		try {
			FileCreator fc= new FileCreator();
			fc.getSVG(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private String recursiveBuilder(SootMethod m, int depth, Scene scene){
		if (depth > 5){ //Default based on specs
			return "";
		}
		CallGraph cg = scene.getCallGraph();
		Iterator<Edge> edges = cg.edgesOutOf(m);
		StringBuilder sb = new StringBuilder();
		SootClass scFrom = m.getDeclaringClass();
		
		sb.append("activate " + scFrom.getName());
		sb.append("\n");
		while (edges.hasNext()){
			Edge e = edges.next();
			SootMethod meth = e.getTgt().method();
			SootClass scTo = meth.getDeclaringClass();
			sb.append(scFrom.getName() + " -> " + scTo.getName() + ": " + meth.getName());
			sb.append("\n");
			sb.append(recursiveBuilder(meth, depth+1, scene));
			sb.append("\n");
			sb.append(scTo.getName() + " --> "  + scFrom.getName() );
			sb.append("\n");
		}
		sb.append("deactivate " + scFrom.getName());
		sb.append("\n");
		return sb.toString();
	}


}
