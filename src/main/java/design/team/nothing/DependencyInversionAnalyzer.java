package design.team.nothing;

import java.util.Collection;
import java.util.Iterator;

import design.team.nothing.Relationship.RelationshipType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class DependencyInversionAnalyzer extends AbstractAnalyzer{

	@Override
	public Data analyze(Data data) {
		PatternRenderer renderer = new DependencyInversionRenderer();
		Pattern pattern = new Pattern(renderer);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		for (Relationship r : relationships) {
			if (r.type != RelationshipType.DEPENDENCY_ONE_TO_MANY && r.type != RelationshipType.DEPENDENCY_ONE_TO_ONE) {
				continue;
			}
			if (applyFilters(r.to)) {
				continue;
			}
			if (!(r.to.isAbstract() || r.to.isInterface())) {
				pattern.addClass("dependency", r.to);
				pattern.addClass("dependencyInversion", r.from);
				pattern.addRelationship("dependencyInversion", r);
			}
		}
//		for (SootClass c : classes) {
//			CallGraph cg = scene.getCallGraph();
//			for (SootMethod m : c.getMethods()) {
//				Iterator<Edge> it = cg.edgesOutOf(m);
//				while (it.hasNext()) {
//					Edge edge = it.next();
//					SootMethod to = edge.tgt().method();
//					SootClass toClass = to.getDeclaringClass();
//					System.out.println("Edge of " + c.getName() + " to " + toClass.getName());
//					if (!toClass.isConcrete() || toClass.isAbstract() || this.applyFilters(toClass)) {
//						continue;
//					}
//					
//					if (toClass.hasSuperclass() && toClass.getSuperclass().declaresMethod(to.getSubSignature())
//							&& !toClass.getSuperclass().getName().equals(c.getName())) {
//						System.out.println("DI Violation: " + c.getName() + " depends on " + toClass.getName());
//						boolean found = false;
//						for (Relationship r : relationships) {
//							if (r.to.getName().equals(toClass.getName())
//									&& r.from.getName().equals(c.getName())){
//								found = true;
//								pattern.addRelationship("dependencyInversion", r);
//							}
//						}
//						if (found == true) {
//							pattern.addClass("dependency", toClass);
//							pattern.addClass("dependencyInversion", c);
//						}
//						
//					}
//				}
//			}
		//}
		data.put("dependencyInversion", pattern);
		return data;
	}

	
	
	
}
