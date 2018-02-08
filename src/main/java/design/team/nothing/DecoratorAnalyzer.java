package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
		Collection<SootClass> classesToAdd = new HashSet<>();
		Collection<SootClass> classes = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		Scene scene = data.get("scene", Scene.class);
		CallGraph cg = scene.getCallGraph();
		generateDecorators(pattern, classesToAdd, classes, relationships, scene, cg);
		while (classesToAdd.size() != 0) {
			for (SootClass c : classesToAdd) {
				classes.add(c);
			}
			classesToAdd.clear();
			generateDecorators(pattern, classesToAdd, classes, relationships, scene, cg);
		}
		data.put("decorator", pattern);
		return data;
	}

	private void generateDecorators(Pattern pattern, Collection<SootClass> classesToAdd, Collection<SootClass> classes,
			Collection<Relationship> relationships, Scene scene, CallGraph cg) {
		for(SootClass sc : classes){
			if(sc.hasSuperclass()){
				SootClass superSC = sc.getSuperclass();
				if(checkDecorator(cg, sc, superSC, scene)){
//					System.out.println("FOUND A DECORATORRRRRRRRRRRRRRRRRRRRR");
					if (savedField.getName().equals("java.lang.Object")) {
						continue;
					}
					pattern.addClass("decorator", sc);
					if (!classes.contains(savedField)) {
						classesToAdd.add(savedField);
					}
					pattern.addClass("component", savedField);
					Collection<SootMethod> listSM = new HashSet<>();
					for(SootMethod sm: savedField.getMethods()){
						if (sm.isConstructor()) {
							continue;
						}
						
						if(!sc.declaresMethod(sm.getSubSignature())){
							listSM.add(sm);
						}
					}
					if(listSM.size() != 0){
						SootClass tempSootClass = addBadDecorator(listSM, sc);
						pattern.addClass("baddecorator", tempSootClass);
					}
					for (Relationship r : relationships) {
						System.out.println("SavedField: " + this.savedField.getName());
						if (r.to.getName().equals(savedField.getName())
								&& r.from.getName().equals(sc.getName())
								&& (r.type==Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE
								|| r.type==Relationship.RelationshipType.DEPENDENCY_ONE_TO_ONE)){
							pattern.addRelationship("decorates", r);
						}
						
					}
				}
			}
			for (SootClass interfaze : sc.getInterfaces()) {
				if(checkDecorator(cg, sc, interfaze, scene)){
					if (savedField.getName().equals("java.lang.Object")) {
						continue;
					}
					pattern.addClass("decorator", sc);
//					if (!scene.containsClass(savedField.getName())) {
//						SootClass added = scene.loadClassAndSupport(savedField.getName());
//						System.out.println("added: " + added.getName());
//						classes.add(added);
//					}
					if (!classes.contains(savedField)) {
						//SootClass added = scene.loadClassAndSupport(savedField.getName());
						//System.out.println("added: " + added.getName());
						classesToAdd.add(savedField);
					}
					pattern.addClass("component", savedField);
					for (Relationship r : relationships) {
						if (r.to.getName().equals(this.savedField.getName())
								&& r.from.getName().equals(sc.getName())
								&&(r.type==Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE
								|| r.type==Relationship.RelationshipType.DEPENDENCY_ONE_TO_ONE)){
							pattern.addRelationship("decorates", r);
						}
						
					}
				}
			}
		}
	}
	
	private SootClass addBadDecorator(Collection<SootMethod> listSM, SootClass sc) {
		SootClass sootClass = new SootClass(sc.getName());
		for(SootMethod sm : listSM){
			System.out.println(sc.getName() + ":  " + sm.getSubSignature());
			if (!sootClass.declaresMethod(sm.getSubSignature())) {
				SootMethod m = new SootMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType());
				sootClass.addMethod(m);
			}
		}
		
		return sootClass;
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
			if (sc.hasSuperclass()) {
				for(SootField sf: sc.getSuperclass().getFields()){
					if(isSubclassOf(superSC, scene.getSootClass(sf.getType().toString()))){
						savedField = scene.getSootClass(sf.getType().toString());
						System.out.println("Decorator has a field of itself!");
						System.out.println("savedField: " + savedField.getName());
						hasItself = true;
					}
				}
			}
			if (!hasItself) {
				return false;
			}
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
