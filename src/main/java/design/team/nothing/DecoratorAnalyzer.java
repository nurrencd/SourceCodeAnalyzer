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

public class DecoratorAnalyzer extends AbstractAnalyzer{

	private SootClass savedField;

	@Override
	public Data analyze(Data data) {
		PatternRenderer pr = new DecoratorRenderer();
		Pattern pattern = new Pattern(pr);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		CallGraph cg = scene.getCallGraph();
		for(SootClass sc : classes){
			if(sc.hasSuperclass()){
				SootClass superSC = sc.getSuperclass();
				if(checkDecorator(cg, sc, superSC, scene)){
//					System.out.println("FOUND A DECORATORRRRRRRRRRRRRRRRRRRRR");
					pattern.addClass("decorator", sc);
					pattern.addClass("component", savedField);
					for (Relationship r : relationships) {
						System.out.println("SavedField: " + this.savedField.getName());
						if (r.to.getName().equals(savedField.getName())
								&& r.from.getName().equals(sc.getName())
								&&r.type==Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE){
							pattern.addRelationship("decorates", r);
						}
						
					}
				}
			}
			for (SootClass interfaze : sc.getInterfaces()) {
				if(checkDecorator(cg, sc, interfaze, scene)){
					pattern.addClass("decorator", sc);
					pattern.addClass("component", savedField);
					for (Relationship r : relationships) {
						if (r.to.getName().equals(this.savedField.getName())
								&& r.from.getName().equals(sc.getName())
								&&r.type==Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE){
							pattern.addRelationship("decorates", r);
						}
						
					}
				}
			}
		}
		data.put("decorator", pattern);
		return data;
	}
	
	public boolean checkDecorator(CallGraph cg, SootClass sc, SootClass superSC, Scene scene) {
		boolean hasItself = false;
		for(SootField sf: sc.getFields()){
			if(isSubclassOf(superSC, scene.getSootClass(sf.getType().toString()))){
				savedField = scene.getSootClass(sf.getType().toString());
				System.out.println("Decorator has a field of itself!");
				System.out.println("savedField: " + savedField.getName());
				hasItself = true;
			}
		}
		if(!hasItself){
			return false;
		}
		boolean constructorFound = true;
		boolean isDecorater = true;
		for(SootMethod sm: sc.getMethods()){
			if(sm.isConstructor()){
				boolean validConstructor = false;
//				System.out.println("START CONSTRUCTOR CHECK");
				for(Type parameterType : sm.getParameterTypes()){
					if(isSubclassOf(scene.getSootClass(parameterType.toString()), savedField)){
						validConstructor = true;
						System.out.println("Found a VALID CONSTRUCTOR");
					}
				}
//				System.out.println("END CONSTRUCTOR CHECK");
				if(constructorFound){
					constructorFound = validConstructor;							
				}
			}else{
				if (!superSC.declaresMethod(sm.getSignature())) {
					continue;
				}
				Iterator<Edge> edges = cg.edgesOutOf(sm);
				boolean edgeFound = false;
				while(edges.hasNext()){
					Edge edge = edges.next();
					if(edge.tgt().method().getDeclaringClass().getName().equals(superSC.getName())){
						edgeFound = true;
					}
				}
				if (!edgeFound) {
					System.out.println("Edge not found in method: " + sm.getSignature());
				}
				if(isDecorater){
					isDecorater = edgeFound;
				}
			}
		}
		System.out.println("Is " + sc.getName() + " a decorator? " + isDecorater  + " " + constructorFound);
		return isDecorater && constructorFound;
	}
	
	private boolean isSubclassOf(SootClass child, SootClass parent) {
//		System.out.println("Child: " + child.getName() + ", Parent: " + parent.getName());
		if (child.getName().equals(parent.getName())) {
			return true;
		}
		if (!child.hasSuperclass()) {
			return false;
		}
		boolean isSubclass = isSubclassOf(child.getSuperclass(), parent);
		for (SootClass iterfs : child.getInterfaces()) {
			isSubclass = isSubclass || isSubclassOf(iterfs, parent);
			if (isSubclass) {
				return true;
			}
		}
		return isSubclass;
	}

}
