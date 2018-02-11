package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import design.team.nothing.Relationship.RelationshipType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class DecoratorAnalyzer extends AbstractAnalyzer{

	private SootClass savedField;
	private Pattern pattern;
	List<SootClass> bad = new ArrayList<>();

	@Override
	public Data analyze(Data data) {
		PatternRenderer pr = new DecoratorRenderer(bad);
		pattern = new Pattern(pr);
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
				if(checkDecorator(cg, sc, superSC, scene, relationships)){
//					System.out.println("FOUND A DECORATORRRRRRRRRRRRRRRRRRRRR");
					if (savedField.getName().equals("java.lang.Object")) {
						continue;
					}
					pattern.addClass("decorator", sc);
					boolean found = false;
					for (SootClass c : classes) {
						if (c.getName().equals(savedField.getName())) {
							found = true;
						}
					}
					if (!found) {
						classesToAdd.add(savedField);
					}
					pattern.addClass("component", savedField);
					Collection<SootMethod> listSM = new HashSet<>();
					for(SootMethod sm: savedField.getMethods()){
						if (sm.isConstructor()) {
							continue;
						}
						
						if (sm.isPrivate()) {
							continue;
						}
						
						if(!sc.declaresMethod(sm.getSubSignature())){
							listSM.add(sm);
						}
					}
					if(listSM.size() != 0){
						SootClass tempSootClass = addBadDecorator(listSM, sc);
						//pattern.addClass("baddecorator", tempSootClass);
						bad.add(tempSootClass);
					}
					for (Relationship r : relationships) {
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
				if(checkDecorator(cg, sc, interfaze, scene, relationships)){
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
								&& !(r.type != RelationshipType.IMPLEMENTATION 
								|| r.type == RelationshipType.INHERITANCE)){
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
			if (!sootClass.declaresMethod(sm.getSubSignature())) {
				SootMethod m = new SootMethod(sm.getName(), sm.getParameterTypes(), sm.getReturnType());
				sootClass.addMethod(m);
			}
		}
		
		return sootClass;
	}

	public boolean checkDecorator(CallGraph cg, SootClass sc, SootClass superSC, Scene scene, Collection<Relationship> relationships) {
		boolean hasItself = false;
		for(SootField sf: sc.getFields()){
			SootClass c;
			if((c = intersect(superSC, scene.getSootClass(sf.getType().toString()))) != null){
				savedField = c;
				hasItself = true;
			}
		}
		if(!hasItself){
			if (sc.hasSuperclass()) {
				for(SootField sf: sc.getSuperclass().getFields()){
					SootClass c;
					if((c = intersect(superSC, scene.getSootClass(sf.getType().toString()))) != null){
						savedField = c;
						hasItself = true;
					}
				}
			}
			if (!hasItself) {
				return false;
			}else {
				pattern.addClass("decorator", sc.getSuperclass());
				for (Relationship r : relationships) {
					if (r.to.getName().equals(this.savedField.getName())
							&& r.from.getName().equals(sc.getSuperclass().getName())
							&& !(r.type == RelationshipType.IMPLEMENTATION 
							|| r.type == RelationshipType.INHERITANCE)){
						pattern.addRelationship("decorates", r);
					}
				}
			}
		}
		boolean constructorFound = true;
		boolean isDecorater = true;
		for(SootMethod sm: sc.getMethods()){
			if(sm.isConstructor()){
				boolean validConstructor = true;
//				System.out.println("START CONSTRUCTOR CHECK");
				for(Type parameterType : sm.getParameterTypes()){
					SootClass c;
					if((c = intersect(scene.getSootClass(parameterType.toString()), savedField)) != null){
						validConstructor = true;
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
				if(isDecorater){
					isDecorater = edgeFound;
				}
			}
		}
		return isDecorater && constructorFound;
	}
	
	private boolean haveCommonParent(SootClass child, SootClass parent) {
		SootClass current = parent;
		while (!current.getName().equals("java.lang.Object")) {
			if (isSubclassOf(child, current)) {
				return true;
			}
			if (!current.hasSuperclass()) {
				break;
			}
			current = parent.getSuperclass();
		}
		
		return recursiveCheck(child, parent);
	}
	
	private boolean recursiveCheck(SootClass child, SootClass interfaze) {
		if (isSubclassOf(child, interfaze)) {
			return true;
		}
		
		for (SootClass c : interfaze.getInterfaces()) {
			if (recursiveCheck(child, c)) {
				return true;
			}
		}
		return false;
	}
	
	private Collection<SootClass> genSupers(SootClass c){
		Collection<SootClass> clazzes = new HashSet<SootClass>();
		clazzes.add(c);
		try {
			if (c.hasSuperclass()) {
				clazzes.addAll(genSupers(c.getSuperclass()));
			}
		} catch(Exception e) {
		}
		try {
			for (SootClass interfaze : c.getInterfaces()) {
				clazzes.addAll(genSupers(interfaze));
			}
		} catch(Exception e) {
		}
		
		return clazzes;
	}
	
	private SootClass intersect(SootClass c1, SootClass c2) {
		Collection<SootClass> clazzes1 = genSupers(c1);
		Collection<SootClass> clazzes2 = genSupers(c2);
		
		for (SootClass cl1 : clazzes1) {
			for (SootClass cl2 : clazzes2) {
				if (cl2.getName().equals(cl1.getName())) {
					if (!cl2.getName().equals("java.lang.Object")) {
						return cl2;
					}
				}
			}
		}
		return null;
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
