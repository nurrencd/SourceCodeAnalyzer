package design.team.nothing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import soot.Body;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.internal.JAssignStmt;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

public class SequenceDiagramAnalyzer extends AbstractAnalyzer {
	private final int DEFAULT_DEPTH = 5;
	private final String DEPTH_KEY = "depth";

	private Data data;
	private Unit unit;
	private String mSig;
	private int maxDepth;
	private ArrowGenerationStrategy strategy;

	public SequenceDiagramAnalyzer(String mSig, ArrowGenerationStrategy strategy) {
		this.maxDepth = DEFAULT_DEPTH;
		this.mSig = mSig;
		this.strategy = strategy;
	}

	@Override
	public Data analyze(Data data) {
		this.data = data;
		Properties prop = data.get("properties", Properties.class);
		Scene scene = data.get("scene", Scene.class);
		if (prop.containsKey(DEPTH_KEY)) {
			this.maxDepth = Integer.parseInt(prop.getProperty(DEPTH_KEY));
		}
		SootMethod entry = scene.getMethod(this.mSig);
		StringBuilder codeBuilder = new StringBuilder();
		codeBuilder.append("@startuml\n");
		codeBuilder.append(recursiveBuilder(entry, 0, scene));
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
		StringBuilder sb = new StringBuilder();
		if (depth >= this.maxDepth) { // Default based on specs
			return "";
		}
		
		if (m.hasActiveBody()) {
			
			genConcreteMethod(m, depth, scene, sb);
			
		}else {
			AggregateAlgorithm aa = new AggregateAlgorithm();
			Properties prop = this.data.get("properties", Properties.class);
			String resolutionString = prop.getProperty("resolutionstrategy");
			String[] algorithmString = prop.getProperty("algorithms").split(" ");
			List<Algorithm> algorithms = new ArrayList<>();
			try {
				System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! " + resolutionString);
				ResolutionStrategy resolutionStrat = (ResolutionStrategy) Class.forName(resolutionString).newInstance();
				aa.setResolutionStrategy(resolutionStrat);
				for(String str : algorithmString){
					aa.addAlgorithm((Algorithm) Class.forName(str).newInstance());
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<SootMethod> chosenOne = aa.resolve(m, this.unit, scene);
//			genAbstractMethod(m, depth, scene, sb);
			if(chosenOne!=null){
				genConcreteMethod(chosenOne.get(0), depth, scene, sb);
			}
		}
		
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
		if (!rightMethod.hasActiveBody()){
			SootMethod chosenOne = resolveAbstractMethod(scene, rightMethod);
//			genAbstractMethod(m, depth, scene, sb);
			if(chosenOne!=null){
				rightMethod = chosenOne;
				rightClass = chosenOne.getDeclaringClass();
			}
		}
		sb.append(this.genCallArrow(m, rightMethod));
		sb.append(recursiveBuilder(rightMethod, depth+1,scene));
		sb.append('\n');
		sb.append(this.genReturnArrow(rightMethod, m));
	}

	private SootMethod resolveAbstractMethod(Scene scene, SootMethod rightMethod) {
		AggregateAlgorithm aa = new AggregateAlgorithm();
		Properties prop = this.data.get("properties", Properties.class);
		String resolutionString = prop.getProperty("resolutionstrategy");
		String[] algorithmString = prop.getProperty("algorithms").split(" ");
		List<Algorithm> algorithms = new ArrayList<>();
		try {
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! " + resolutionString);
			ResolutionStrategy resolutionStrat = (ResolutionStrategy) Class.forName(resolutionString).newInstance();
			aa.setResolutionStrategy(resolutionStrat);
			for(String str : algorithmString){
				aa.addAlgorithm((Algorithm) Class.forName(str).newInstance());
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<SootMethod> chosenOne = aa.resolve(rightMethod, this.unit, scene);
		
		if (chosenOne == null) {
			return null;
		}
		return chosenOne.get(0);
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
				this.unit = u;
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
	
	public String genCallArrow(SootMethod from, SootMethod to) {
		return this.strategy.genCallArrow(from, to);
	}
	
	public String genReturnArrow(SootMethod from, SootMethod to) {
		return this.strategy.genReturnArrow(from, to);
	}

}
