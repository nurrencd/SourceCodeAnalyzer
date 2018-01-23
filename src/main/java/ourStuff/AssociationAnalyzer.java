package ourStuff;

import java.util.Collection;
import java.util.Iterator;

import edu.rosehulman.jvm.sigevaluator.FieldEvaluator;
import edu.rosehulman.jvm.sigevaluator.GenericType;
import ourStuff.Relationship.RelationshipType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.tagkit.Tag;
import soot.util.Chain;
//TODO: Add filter checks
public class AssociationAnalyzer extends AbstractAnalyzer{

	/**
	 *  analyze(Data data) returns Data class
	 *  Simply makes the association relationships for the data
	 */
	@Override
	public Data analyze(Data data) {
		Collection<SootClass> collection= (Collection<SootClass>)
				data.get("classes", Collection.class);
		Iterator<SootClass> it = collection.iterator();
		while(it.hasNext()){
			SootClass c = it.next();
			Chain<SootField> chField = c.getFields();
			
			for(SootField sf :  chField){
				String sfType = sf.getType().toString();
				Scene scene = data.get("scene", Scene.class);
				if(scene.containsClass(sfType)){
					SootClass to = scene.getSootClass(sfType);
					addRelationship(data, c, to, RelationshipType.ASSOCIATION_ONE_TO_ONE);
				}
				Tag sig = sf.getTag("SignatureTag");
				if (sig==null) {
					if(sf.getType().toString().endsWith("[]")){
						String sifType = sf.getType().toString();
						String modType = sifType.substring(0, sifType.length()-2);
						if(scene.containsClass(modType)){
							SootClass to = scene.getSootClass(modType);
							addRelationship(data, c, to, RelationshipType.ASSOCIATION_ONE_TO_MANY);
							
						}
					}
					continue;
				}
				
				fieldEval(data, sig, c);
			}
		}
		return data;
	}

	/**
	 * fieldEval adds appropriate relationships
	 * @param data
	 * @param sig
	 * @param c
	 */
	private void fieldEval(Data data, Tag sig, SootClass c){
		FieldEvaluator fe = new FieldEvaluator(sig.toString());
		GenericType gt = fe.getType();
		Collection<String> containerTypes = gt.getAllContainerTypes();
		Collection<String> elementTypes = gt.getAllElementTypes();
		Scene scene = data.get("scene", Scene.class);
		for (String s : containerTypes) {
			SootClass container = scene.getSootClass(s);
			if (container != null) {
				addRelationship(data, c, container, RelationshipType.ASSOCIATION_ONE_TO_ONE);
			}
		}
		for (String s : elementTypes) {
			if (!scene.containsClass(s)){
				continue;
			}
			SootClass element = scene.getSootClassUnsafe(s);
			if (element != null) {
				RelationshipType r = RelationshipType.ASSOCIATION_ONE_TO_MANY;
				if (containerTypes.size()==0) {
					r = RelationshipType.ASSOCIATION_ONE_TO_ONE;
				}
				addRelationship(data, c, element, r);
			}
		}
	}

	/**
	 * addRelationship() adds the relationship and applies filters
	 * @param data
	 * @param c
	 * @param container
	 * @param rType
	 */
	private void addRelationship(Data data, SootClass c, SootClass container, RelationshipType rType) {
		//if (!this.applyFilters(container)){
			//Need to check the 0th index of containerTypes to confirm a one to one relation
			Relationship r = new Relationship(c, container, rType);
			Collection<Relationship> rels = data.get("relationships", Collection.class);
			rels.add(r);
			
		//}
	}
}
