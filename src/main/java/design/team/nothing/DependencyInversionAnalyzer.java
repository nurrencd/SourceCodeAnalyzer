package design.team.nothing;

import java.util.Collection;
import java.util.Iterator;

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
		for (SootClass c : classes) {
			CallGraph cg = scene.getCallGraph();
			for (SootMethod m : c.getMethods()) {
				Iterator<Edge> it = cg.edgesOutOf(m);
				while (it.hasNext()) {
					Edge edge = it.next();
					SootMethod to = edge.tgt().method();
					SootClass toClass = to.getDeclaringClass();
					if (!toClass.isConcrete() || toClass.isAbstract() || this.applyFilters(toClass)) {
						continue;
					}
					
					if (toClass.hasSuperclass() && toClass.getSuperclass().declaresMethod(to.getSubSignature())
							&& !toClass.getSuperclass().getName().equals(c.getName())) {
						System.out.println("DI Violation: " + c.getName() + " depends on " + toClass.getName());
						pattern.addClass("dependency", toClass);
						pattern.addClass("dependencyInversion", c);
						for (Relationship r : relationships) {
							if (r.to.getName().equals(toClass.getName())
									&& r.from.getName().equals(c.getName())){
								pattern.addRelationship("dependencyInversion", r);
							}
							
						}
						
					}
				}
			}
		}
		data.put("dependencyInversion", pattern);
		return data;
	}

	
	
	
}
