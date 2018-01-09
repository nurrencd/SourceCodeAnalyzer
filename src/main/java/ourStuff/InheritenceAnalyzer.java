package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.SootClass;

public class InheritenceAnalyzer extends AbstractAnalyzer {
	
	public InheritenceAnalyzer() {
		filters = new ArrayList<Filter>();
	}
	
	public Data analyze(Data data) {
		data.classes.forEach((c) -> {
			data.relationships.addAll(this.getInheritance(c));
		});
		return data;
	}
	
	private Collection<Relationship> getInheritance(SootClass c){
		boolean ignore;
		Collection<Relationship> rels = new ArrayList<Relationship>();
		if(c.hasSuperclass()){
			SootClass superclass = c.getSuperclass();
			
			ignore = false;
			for (Filter f : this.filters) {
				
				if (f.ignore(superclass)) {
					ignore = true;
				}
			}
			if (!ignore) {
				rels.add(new Relationship(c, superclass, Relationship.RelationshipType.INHERITANCE));
			}
		}
		return rels;
	}

}
