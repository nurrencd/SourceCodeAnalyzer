package design.team.nothing;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import design.team.nothing.Relationship.RelationshipType;
import edu.rosehulman.jvm.sigevaluator.GenericType;
import edu.rosehulman.jvm.sigevaluator.MethodEvaluator;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.Tag;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.UnitGraph;

//TODO: Add filter checks
public class DependencyAnalyzer extends AbstractAnalyzer {

	@Override
	public Data analyze(Data data) {
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Properties p = data.get("properties", Properties.class);
		for (SootClass c : classes) {
			if (!p.containsKey("classlist")) {
				if (this.applyFilters(c)){
					continue;
				}
			}
			for (SootMethod m : c.getMethods()) {
				Tag tag = m.getTag("SignatureTag");
				if (tag != null) {
					getReturnType(data, c, tag);
				}
				getParameterTypes(data, c, m);

				if (m.hasActiveBody()) {
					Body b = m.retrieveActiveBody();
					UnitGraph ug = new ExceptionalUnitGraph(b);

					checkExpressionInterior(data, c, ug);
					//
					checkExpressionEdges(data, c, m);

				}
			}
			// Need to get Local variables for A
		}
		return data;
	}

	/**
	 * @param data
	 * @param c
	 * @param m
	 */
	private void checkExpressionEdges(Data data, SootClass c, SootMethod m) {
		Scene scene = data.get("scene", Scene.class);
		CallGraph cg = scene.getCallGraph();
		Iterator<Edge> edges = cg.edgesOutOf(m);
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		while (edges.hasNext()) {
			Edge edge = edges.next();
			if (!edge.isInstance()) {
				SootMethod method = edge.tgt();
				Type retType = method.getReturnType();
				if (retType != null) {
					SootClass retClass = scene.getSootClass(retType.toString());
					//if (!this.applyFilters(retClass)) {
						Relationship r = new Relationship(c, retClass, RelationshipType.DEPENDENCY_ONE_TO_ONE);
						rels.add(r);
					//}
				}
			}
		}
	}

	/**
	 * @param data
	 * @param c
	 * @param ug
	 */
	private void checkExpressionInterior(Data data, SootClass c, UnitGraph ug) {
		Scene scene = data.get("scene", Scene.class);
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		
		for (Unit u : ug) {
			if (u instanceof AssignStmt) {
				Value leftOp = ((AssignStmt) u).getLeftOp();
				Value rightOp = ((AssignStmt) u).getRightOp();
				String str = leftOp.getType().toString();
				if (scene.containsClass(str)) {
					SootClass clazz = scene.getSootClass(str);

					//if (!applyFilters(clazz)) {
						Relationship r = new Relationship(c, clazz, RelationshipType.DEPENDENCY_ONE_TO_ONE);
						rels.add(r);
					//}
				}
			}
		}
	}

	/**
	 * @param data
	 * @param c
	 * @param m
	 */
	private void getParameterTypes(Data data, SootClass c, SootMethod m) {
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		
		for (Type t : m.getParameterTypes()) {
			if (t.toString().contains("TT;")||t.toString().contains("<*")) {
				continue;
			}
			if (scene.containsClass(t.toString())) {
				SootClass container = scene.getSootClass(t.toString());
				if (container != null) {
					boolean ignore = false;
					for (Filter f : this.filters) {
						if (f.ignore(container)) {
							ignore = true;
						}
					}
					//if (!ignore) {
						Relationship r = new Relationship(c, container, RelationshipType.DEPENDENCY_ONE_TO_ONE);
						rels.add(r);
					//}
				}
			}
		}
	}

	/**
	 * @param data
	 * @param c
	 * @param tag
	 */
	private void getReturnType(Data data, SootClass c, Tag tag) {
		if (tag.toString().contains("TT;")||tag.toString().contains("<*")) {
			return;
		}
		MethodEvaluator eval = new MethodEvaluator(tag.toString());
		System.out.println(c.getName() + " " + tag.toString());
		List<GenericType> generic;
		try {
			generic = eval.getParameterTypes();
		}catch (Exception e){
			return;
		}
		for (GenericType gt : generic) {
			getTypeDependencies(data, c, gt);
		}
		GenericType returnType = eval.getReturnType();
		getTypeDependencies(data, c, returnType);
	}

	private void getTypeDependencies(Data data, SootClass c, GenericType gt) {
		// Fix Duplicate Code and Container One to One logic
		Collection<String> containerTypes = gt.getAllContainerTypes();
		Collection<String> elementTypes = gt.getAllElementTypes();
		Scene scene = data.get("scene", Scene.class);
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		for (String s : containerTypes) {
			SootClass container = scene.getSootClass(s);
			if (container != null) {
				//if (!this.applyFilters(container)) {
					Relationship r = new Relationship(c, container, RelationshipType.DEPENDENCY_ONE_TO_ONE);
					rels.add(r);
				//}
			}
		}
		for (String s : elementTypes) {
			SootClass element = scene.getSootClassUnsafe(s);
			if (element != null) {
				//if (!this.applyFilters(element)) {
					RelationshipType r = RelationshipType.DEPENDENCY_ONE_TO_MANY;
					if (containerTypes.size() == 0) {
						r = RelationshipType.DEPENDENCY_ONE_TO_ONE;
					}
					Relationship rel = new Relationship(c, element, r);
					rels.add(rel);
				//}
			}
		}
	}

}
