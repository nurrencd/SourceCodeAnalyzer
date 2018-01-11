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
		data.classes.forEach((c) -> {
			data.relationships.putAll(this.getInheritance(c));
		});
		return data;
	}
	
	private Map<Integer, Relationship> getInheritance(SootClass c){
		boolean ignore;
		Map<Integer, Relationship> rels = new HashMap<Integer, Relationship>();
		if(c.hasSuperclass()){
			SootClass superclass = c.getSuperclass();
			
			ignore = false;
			for (Filter f : this.filters) {
				
				if (f.ignore(superclass)) {
					ignore = true;
				}
			}
			if (!ignore) {
				Relationship r = new Relationship(c, superclass, Relationship.RelationshipType.INHERITANCE);
				rels.put(r.hashCode(), r);
			}
		}
		return rels;
	}

}
