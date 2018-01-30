package design.team.nothing;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class InheritanceOverCompositionAnalyzer extends AbstractAnalyzer{

	PatternRenderer patternRenderer = new InheritanceOverCompositionRenderer();
	
	
	@Override
	public Data analyze(Data data) {
		Pattern pattern = new Pattern(patternRenderer);
		Collection<SootClass> sootClasses = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		for(SootClass sc : sootClasses){
			if (!data.get("properties", Properties.class).containsKey("classlist")){
				
				if (this.applyFilters(sc)){
					continue;
				}
			}else {
				String str = data.get("properties", Properties.class).getProperty("classlist");
				if (this.applyFilters(sc) &&  !str.contains(sc.getName())){
					continue;
				}
			}
			CallGraph cg = scene.getCallGraph();
			Collection<SootMethod> methods = sc.getMethods();
			for (SootMethod m : methods) {
				if (m.hasActiveBody()) {
					Iterator<Edge> edges = cg.edgesOutOf(m);
					Edge e;
					while (edges.hasNext()) {
						e = edges.next();
						SootClass target = e.getTgt().method().getDeclaringClass();
						if (target.getName().equals("java.lang.Object")) {
							System.out.println("Eric, you were wrong!");
							continue;
						}
						if (target.getName().equals(sc.getSuperclass().getName())) {
							pattern.addClass("compositionOverInheritance", sc);
							System.out.println("Adding COI class");
							for (Relationship r : relationships) {
								if (r.to.getName().equals(sc.getSuperclass().getName())
										&&r.from.getName().equals(sc.getName())){
									pattern.addRelationship("compositionOverInheritance", r);
									System.out.println("Adding COI relationship");
								}
								
							}
						}
					}
				}
			}
			
		}
		data.put("compositionOverInheritance", pattern);
		return data;
	}

}
