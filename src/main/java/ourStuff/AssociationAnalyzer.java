package ourStuff;

import java.util.Collection;
import java.util.Iterator;

import edu.rosehulman.jvm.sigevaluator.FieldEvaluator;
import edu.rosehulman.jvm.sigevaluator.GenericType;
import ourStuff.Relationship.RelationshipType;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.Tag;
import soot.util.Chain;
//TODO: Add filter checks
public class AssociationAnalyzer extends AbstractAnalyzer{

	@Override
	public Data analyze(Data data) {

		Collection<SootClass> collection= data.classes;
		Iterator<SootClass> it = collection.iterator();
		while(it.hasNext()){
			SootClass c = it.next();
			Chain<SootField> chField = c.getFields();
			for(SootField sf :  chField){

				Tag sig = sf.getTag("SignatureTag");
				if (sig==null) {
					continue;
				}
				FieldEvaluator fe = new FieldEvaluator(sig.toString());
				GenericType gt = fe.getType();
				Collection<String> containerTypes = gt.getAllContainerTypes();
				Collection<String> elementTypes = gt.getAllElementTypes();
				
				for (String s : containerTypes) {
					SootClass container = data.scene.getSootClass(s);
					if (container != null) {
						boolean ignore = false;
						for (Filter f : this.filters) {
							if (f.ignore(container)) {
								ignore = true;
							}
						}
						if (!ignore){
							Relationship r = new Relationship(c, container, RelationshipType.ASSOCIATION_ONE_TO_ONE);
							data.relationships.put(r.hashCode(), r);
						}
					}
				}
				for (String s : elementTypes) {
					System.out.println(data.scene.containsClass(s));
					if (!data.scene.containsClass(s)){
						continue;
					}
					SootClass element = data.scene.getSootClassUnsafe(s);
					if (element != null) {
						
						boolean ignore = false;
						for (Filter f : this.filters) {
							if (f.ignore(element)) {
								ignore = true;
							}
						}
						if (!ignore){
							RelationshipType r = RelationshipType.ASSOCIATION_ONE_TO_MANY;
							if (containerTypes.size()==0) {
								r = RelationshipType.ASSOCIATION_ONE_TO_ONE;
							}
							Relationship rel = new Relationship(c, element, r);
							data.relationships.put(rel.hashCode(), rel);
						}
					}
				}
			}
		}
		return data;
	}

}
