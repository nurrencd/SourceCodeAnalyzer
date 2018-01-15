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
//		System.out.println(depth + " of " + this.maxDepth);
		StringBuilder sb = new StringBuilder();
		if (depth >= this.maxDepth) { // Default based on specs
			return "";
		}
		
		sb.append("activate " + m.getDeclaringClass().getName());
		sb.append("\n");
		if (m.hasActiveBody()) {
			
			genConcreteMethod(m, depth, scene, sb);
			
		}else {
			genAbstractMethod(m, depth, scene, sb);
		}
		
		sb.append("deactivate " + m.getDeclaringClass().getName());
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * @param m
	 * @param depth
	 * @param scene
	 * @param sb
	 */
	private void genAbstractMethod(SootMethod m, int depth, Scene scene, StringBuilder sb) {
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
					drawArrows(m, depth, scene, sb, rightMethod, rightClass);
				}

			}
			else 
			if (u instanceof InvokeStmt) {
				SootMethod rightMethod = ((InvokeStmt) u).getInvokeExpr().getMethod();
				SootClass rightClass = rightMethod.getDeclaringClass();
				if (this.applyFilters(rightClass)) {
					continue;
				}
				drawArrows(m, depth, scene, sb, rightMethod, rightClass);
			}
		}
	}

	/**
	 * @param m
	 * @param depth
	 * @param scene
	 * @param sb
	 * @param rightMethod
	 * @param rightClass
	 */
	private void drawArrows(SootMethod m, int depth, Scene scene, StringBuilder sb, SootMethod rightMethod,
			SootClass rightClass) {
		sb.append(m.getDeclaringClass().getName() + " --> " + rightClass.getName() + " : " + rightMethod.getSubSignature());
		sb.append('\n');
		sb.append(recursiveBuilder(rightMethod, depth+1,scene));
		sb.append('\n');
		sb.append(m.getDeclaringClass().getName() + " <- " + rightClass.getName());
		sb.append('\n');
	}

	/**
	 * @param m
	 * @param depth
	 * @param scene
	 * @param sb
	 */
	private void genConcreteMethod(SootMethod m, int depth, Scene scene, StringBuilder sb) {
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
					drawArrows(m, depth, scene, sb, rightMethod, rightClass);
				}

			}
			else if (u instanceof InvokeStmt) {
				SootMethod rightMethod = ((InvokeStmt) u).getInvokeExpr().getMethod();
				SootClass rightClass = rightMethod.getDeclaringClass();
				if (this.applyFilters(rightClass)) {
					continue;
				}
				drawArrows(m, depth, scene, sb, rightMethod, rightClass);
			}
		}
	}

}
