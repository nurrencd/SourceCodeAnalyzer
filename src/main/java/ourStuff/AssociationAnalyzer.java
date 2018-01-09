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
						data.relationships.add(new Relationship(c, container, RelationshipType.ONE_TO_ONE));
					}
				}
				for (String s : elementTypes) {
					SootClass element = data.scene.getSootClass(s);
					if (element != null) {
						RelationshipType r = RelationshipType.ONE_TO_MANY;
						if (containerTypes.size()==0) {
							r = RelationshipType.ONE_TO_ONE;
						}
						data.relationships.add(new Relationship(c, element, r));
					}
				}
			}
		}
		return data;
	}

}
