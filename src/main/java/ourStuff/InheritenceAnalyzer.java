package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.SootClass;

public class InheritenceAnalyzer extends AbstractAnalyzer {
	
	public InheritenceAnalyzer() {
		filters = new ArrayList<Filter>();
	}
	
	public Data analyze(Data data) {
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		classes.forEach((c) -> {
			rels.addAll(this.getInheritance(c));
		});
		return data;
	}
	
	private Collection<Relationship> getInheritance(SootClass c){
		Collection<Relationship> rels = new ArrayList<Relationship>();
		if(c.hasSuperclass()){
			SootClass superclass = c.getSuperclass();
			//if (!this.applyFilters(superclass)) {
				Relationship r = new Relationship(c, superclass, Relationship.RelationshipType.INHERITANCE);
				rels.add(r);
			//}
		}
		return rels;
	}

}
