package design.team.nothing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.Chain;

public class AdapterAnalyzer extends AbstractAnalyzer{

	Collection<SootClass> classesToAdd = new HashSet<>();
	
	@Override
	public Data analyze(Data data) {
		PatternRenderer pr = new AdapterRenderer();
		Pattern pattern = new Pattern(pr);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		String str = data.get("properties", Properties.class).getProperty("exclude");
		Scene scene = data.get("scene", Scene.class);
		CallGraph cg = scene.getCallGraph();
		for(SootClass sc : classes){
			if (this.applyFilters(sc)) {
				continue;
			}
			if (sc.getInterfaceCount() == 0) {
				continue;
			}
			Collection<SootClass> possible = this.getAdaptingClass(sc, scene);
			
			candidateLoop:
			for (SootClass candidate : possible) {
				if (this.applyFilters(candidate)) {
					continue;
				}
				for (SootClass interfaze : sc.getInterfaces()) {
					if (this.applyFilters(interfaze)) {
						continue;
					}
					boolean adaptedInterface = true;
					for (SootMethod m : interfaze.getMethods()) {
						
						SootMethod localMethod = sc.getMethodUnsafe(m.getSubSignature());
						if (localMethod == null) {
							continue;
						}
						if (localMethod.getExceptions().size() >= 1) {
							continue;
						}
						Iterator<Edge> edges = cg.edgesOutOf(localMethod);
						
						boolean edgeFound = true;
						while (edges.hasNext()) {
							Edge e = edges.next();
							if (e.tgt().method().getDeclaringClass().getName().equals(candidate.getName())) {
								edgeFound = true;
							}
						}
						if (!edgeFound) {
							adaptedInterface = false;
						}
					}
					if (adaptedInterface) {
						if (!scene.containsClass(interfaze.getName())) {
							scene.loadClassAndSupport(interfaze.getName());
							classesToAdd.add(interfaze);
						}
						pattern.addClass("target", interfaze);
						
						pattern.addClass("adapter", sc);
						if (!classes.contains(candidate.getName())) {
							scene.loadClassAndSupport(candidate.getName());
							classesToAdd.add(candidate);
						}
						pattern.addClass("adaptee", candidate);
						for (Relationship r : relationships) {
							if (r.to.getName().equals(candidate.getName())
									&& r.from.getName().equals(sc.getName())){
								pattern.addRelationship("adapts", r);
							}
							
						}
						break candidateLoop;
					}
				}
			}
		}
		for (SootClass c : classesToAdd) {
			classes.add(c);
		}
		data.put("adapter", pattern);
		return data;
	}

	private Collection<SootClass> getAdaptingClass(SootClass sc, Scene scene) {
		Collection<String> constructor = new HashSet();
		Collection<SootClass> possible = new HashSet();
		for (SootMethod m : sc.getMethods()) {
			if (m.isConstructor()) {
				for (Type t : m.getParameterTypes()) {
					constructor.add(t.toString());
				}
			}
		}
		for (SootField f : sc.getFields()) {
			if (constructor.contains(f.getType().toString())) {
				possible.add(scene.getSootClass(f.getType().toString()));
			}
		}
		return possible;
	}

}
