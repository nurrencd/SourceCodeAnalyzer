package ourStuff;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.bytebuddy.jar.asm.Type;
import soot.Body;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.NewExpr;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;
import soot.jimple.internal.JNewExpr;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

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
		codeBuilder.append("@startuml\n");
		codeBuilder.append(recursiveBuilder2(entry, 0, data.scene));
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
		System.out.println(depth + " of " + this.maxDepth);
		if (depth >= this.maxDepth) { // Default based on specs
			return "";
		}
		
		CallGraph cg = scene.getCallGraph();
		StringBuilder sb = new StringBuilder();
		SootClass scFrom = m.getDeclaringClass();
		Hierarchy th = scene.getActiveHierarchy();
		Collection<SootMethod> possibleMethods = th.resolveAbstractDispatch(m.getDeclaringClass(), m);

		Collection<Unit> units = possibleMethods.iterator().next().getActiveBody().getUnits();
		
		sb.append("activate " + scFrom.getName());
		sb.append("\n");
		System.out.println(possibleMethods.iterator().next().getName() + ": " + units.size());
		String base = m.getName() + ": ";
		for (Unit u : units) {
			Iterator<Edge> edges = cg.edgesOutOf(u);
			
			while (edges.hasNext()) {

				Edge e = edges.next();
				System.out.print(possibleMethods.iterator().next().getName() + " " + e.getTgt().method().getDeclaringClass().getName() + "\n");
				SootMethod meth = e.getTgt().method();
				SootClass scTo = meth.getDeclaringClass();
				if (!this.applyFilters(scTo)) {
					sb.append(scFrom.getName() + " -> " + scTo.getName() + ": " + meth.getSubSignature());
					sb.append("\n");
					sb.append(recursiveBuilder(meth, depth + 1, scene));
					sb.append("\n");
					sb.append(scTo.getName() + " --> " + scFrom.getName());
					sb.append("\n");
				}
			}
		}
		sb.append("deactivate " + scFrom.getName());
		sb.append("\n");
		return sb.toString();
	}
	
	private String recursiveBuilder2(SootMethod m, int depth, Scene scene) {
//		System.out.println(depth + " of " + this.maxDepth);
		StringBuilder sb = new StringBuilder();
		if (depth >= this.maxDepth) { // Default based on specs
			return "";
		}
		
		sb.append("activate " + m.getDeclaringClass().getName());
		sb.append("\n");
		if (m.hasActiveBody()) {
			
			Body b = m.retrieveActiveBody();
			
			UnitGraph ug = new ExceptionalUnitGraph(b);
			for (Unit u : ug) {
//				System.out.println(u.getClass());
				if (u instanceof JAssignStmt) {
					Value leftOp = ((JAssignStmt) u).getLeftOp();
					Value rightOp = ((JAssignStmt) u).getRightOp();
					//System.out.println(rightOp.getClass());
					if (rightOp instanceof InvokeExpr) {
						SootMethod rightMethod = ((InvokeExpr) rightOp).getMethod();
						SootClass rightClass = rightMethod.getDeclaringClass();
						if (this.applyFilters(rightClass)) {
							continue;
						}
						sb.append(m.getDeclaringClass().getName() + " --> " + rightClass.getName() + " : " + rightMethod.getSubSignature());
						sb.append('\n');
						sb.append(recursiveBuilder2(rightMethod, depth+1,scene));
						sb.append('\n');
						sb.append(m.getDeclaringClass().getName() + " <- " + rightClass.getName());
						sb.append('\n');
					}

				}
				else if (u instanceof InvokeStmt) {
					SootMethod rightMethod = ((InvokeStmt) u).getInvokeExpr().getMethod();
					SootClass rightClass = rightMethod.getDeclaringClass();
					if (this.applyFilters(rightClass)) {
						continue;
					}
					sb.append(m.getDeclaringClass().getName() + " --> " + rightClass.getName() + " : " + rightMethod.getSubSignature());
					sb.append('\n');
					sb.append(recursiveBuilder2(rightMethod, depth+1,scene));
					sb.append('\n');
					sb.append(m.getDeclaringClass().getName() + " <- " + rightClass.getName());
					sb.append('\n');
				}
			}
			
		}else {
			Hierarchy th = scene.getActiveHierarchy();
			Collection<SootMethod> possibleMethods = th.resolveAbstractDispatch(m.getDeclaringClass(), m);
			SootMethod possibleMethod;
			Iterator<SootMethod> iter = possibleMethods.iterator();
			Body b = iter.next().getActiveBody();
			UnitGraph ug = new ExceptionalUnitGraph(b);
			for (Unit u : ug) {
				System.out.println(u.getClass());
				if (u instanceof JAssignStmt) {
					Value leftOp = ((JAssignStmt) u).getLeftOp();
					Value rightOp = ((JAssignStmt) u).getRightOp();
					//System.out.println(rightOp.getClass());
					if (rightOp instanceof InvokeExpr) {
						SootMethod rightMethod = ((InvokeExpr) rightOp).getMethod();
						SootClass rightClass = rightMethod.getDeclaringClass();
						if (this.applyFilters(rightClass)) {
							continue;
						}
						sb.append(m.getDeclaringClass().getName() + " --> " + rightClass.getName() + " : " + rightMethod.getSubSignature());
						sb.append('\n');
						sb.append(recursiveBuilder2(rightMethod, depth+1,scene));
						sb.append('\n');
						sb.append(m.getDeclaringClass().getName() + " <- " + rightClass.getName());
						sb.append('\n');
					}

				}
				else 
				if (u instanceof InvokeStmt) {
					SootMethod rightMethod = ((InvokeStmt) u).getInvokeExpr().getMethod();
					SootClass rightClass = rightMethod.getDeclaringClass();
					if (this.applyFilters(rightClass)) {
						continue;
					}
					sb.append(m.getDeclaringClass().getName() + " --> " + rightClass.getName() + " : " + rightMethod.getSubSignature());
					sb.append('\n');
					sb.append(recursiveBuilder2(rightMethod, depth+1,scene));
					sb.append('\n');
					sb.append(m.getDeclaringClass().getName() + " <- " + rightClass.getName());
					sb.append('\n');
				}
			}
		}
		
		sb.append("deactivate " + m.getDeclaringClass().getName());
		sb.append("\n");
		return sb.toString();
	}

}
