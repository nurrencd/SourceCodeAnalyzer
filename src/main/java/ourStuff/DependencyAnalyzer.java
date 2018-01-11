package ourStuff;

import java.util.Collection;
import java.util.List;

import edu.rosehulman.jvm.sigevaluator.GenericType;
import edu.rosehulman.jvm.sigevaluator.MethodEvaluator;
import ourStuff.Relationship.RelationshipType;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
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
				Chain<Local> locals = m.getActiveBody().getLocals();
				for (Local l : locals) {
					Type gt = l.getType();
					//do funky shit with the locals ;)
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
