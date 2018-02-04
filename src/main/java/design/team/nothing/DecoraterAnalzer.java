package design.team.nothing;

import java.util.Collection;
import java.util.Iterator;

import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class DecoraterAnalzer extends AbstractAnalyzer{

	@Override
	public Data analyze(Data data) {
		PatternRenderer pr = new DecoraterRenderer();
		Pattern pattern = new Pattern(pr);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		CallGraph cg = scene.getCallGraph();
		for(SootClass sc : classes){
			if(sc.hasSuperclass()){
				SootClass superSC = sc.getSuperclass();
				boolean hasItself = false;
				for(SootField sf: sc.getFields()){
					if(sf.getType().toString().equals(superSC.getName())){
						hasItself = true;
					}
				}
				if(!hasItself){
					continue;
				}
				boolean constructorFound = true;
				boolean isDecorater = true;
				for(SootMethod sm: sc.getMethods()){
					if(sm.isConstructor()){
						boolean validConstructor = false;
						for(Type parameterType : sm.getParameterTypes()){
							if(parameterType.toString().equals(superSC.getName())){
								validConstructor = true;
							}
						}
						if(constructorFound){
							constructorFound = validConstructor;							
						}
					}else{
						Iterator<Edge> edges = cg.edgesOutOf(sm);
						boolean edgeFound = false;
						while(edges.hasNext()){
							Edge edge = edges.next();
							if(edge.tgt().method().getDeclaringClass().getName().equals(superSC.getName())){
								edgeFound = true;
							}
						}
						if(isDecorater){
							isDecorater = edgeFound;
						}
					}
				}
				if(isDecorater && constructorFound){
					pattern.addClass("decorator", sc);
					pattern.addClass("component", superSC);
					for (Relationship r : relationships) {
						if (r.to.getName().equals(superSC.getName())
								&& r.from.getName().equals(sc.getName())){
							pattern.addRelationship("decorates", r);
						}
						
					}
				}
			}
		}
		return data;
	}

}
