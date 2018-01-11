package ourStuff;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import edu.rosehulman.jvm.sigevaluator.GenericType;
import edu.rosehulman.jvm.sigevaluator.MethodEvaluator;
import ourStuff.Relationship.RelationshipType;
import soot.Body;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.tagkit.Tag;
import soot.util.Chain;
//TODO: Add filter checks
public class DependencyAnalyzer extends AbstractAnalyzer{

	@Override
	public Data analyze(Data data) {
		for (SootClass c : data.classes) {
			for (SootMethod m : c.getMethods()){
				Tag tag = m.getTag("SignatureTag");
				if (tag != null){
					MethodEvaluator eval = new MethodEvaluator(tag.toString());
					List<GenericType> generic = eval.getParameterTypes();
					for (GenericType gt : generic){
						getTypeDependencies(data, c, gt);
					}
					GenericType returnType = eval.getReturnType();
					getTypeDependencies(data, c, returnType);
				}
				
				if (m.hasActiveBody()){
					Chain<Local> locals = m.getActiveBody().getLocals();
					for (Local l : locals) {
						CallGraph cg = data.scene.getCallGraph();
						Iterator<Edge> edges = cg.edgesOutOf(m);
						while (edges.hasNext()){
							Edge edge = edges.next();
							if (edge.isInstance()){
								SootClass clazz = edge.tgt().getDeclaringClass();
								if (!this.applyFilters(clazz)){
									Relationship r = new Relationship(c, clazz, RelationshipType.DEPENDENCY_ONE_TO_ONE);
									data.relationships.add(r);
								}
							}else {
								SootMethod method = edge.tgt();
								Type retType = method.getReturnType();
								if (retType != null){
									SootClass retClass = data.scene.getSootClass(retType.toString());
									if (!this.applyFilters(retClass)){
										Relationship r = new Relationship(c, retClass, RelationshipType.DEPENDENCY_ONE_TO_ONE);
										data.relationships.add(r);
									}
								}
							}
						}
						
					}
				}
			}
			//Need to get Local variables for A
		}
		return data;
	}

	private void getTypeDependencies(Data data, SootClass c, GenericType gt) {
		//Fix Duplicate Code and Container One to One logic
		Collection<String> containerTypes = gt.getAllContainerTypes();
		Collection<String> elementTypes = gt.getAllElementTypes();
		elementTypes.forEach((String s) -> {
			System.out.println(s + "\n");
		});
		for (String s : containerTypes) {
			SootClass container = data.scene.getSootClass(s);
			if (container != null) {
				boolean ignore = false;
				for (Filter f : this.filters) {
					if (f.ignore(container)) {
						ignore = true;
					}
				}
				if (!ignore)
					data.relationships.add(new Relationship(c, container, RelationshipType.DEPENDENCY_ONE_TO_ONE));
			}
		}
		for (String s : elementTypes) {
			SootClass element = data.scene.getSootClassUnsafe(s);
			if (element != null) {
				//TODO: Abstract to AbstractAnalyzerClass
				boolean ignore = false;
				for (Filter f : this.filters) {
					if (f.ignore(element)) {
						ignore = true;
					}
				}
				if (!ignore){
					RelationshipType r = RelationshipType.DEPENDENCY_ONE_TO_MANY;
					if (containerTypes.size()==0) {
						r = RelationshipType.DEPENDENCY_ONE_TO_ONE;
					}
					data.relationships.add(new Relationship(c, element, r));
				}
			}
		}
	}

	
	
}
