package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.SootClass;

public class ImplementationAnalyzer extends AbstractAnalyzer {

	public ImplementationAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		data.classes.forEach((c) -> {
			data.relationships.putAll(this.getImplementations(c));
		});
		return data;
	}

	private Map<Integer, Relationship> getImplementations(SootClass c) {
		Map<Integer, Relationship> relationships = new HashMap<Integer, Relationship>();
		Collection<SootClass> interfaces = c.getInterfaces();
		//add interface relationships
		boolean ignore = false;
		for (SootClass sc : interfaces) {
			for (Filter f : this.filters) {
				if (f.ignore(sc)) {
					ignore = true;
				}
			}
			if (!ignore && !sc.getShortName().contains("$")) {
				Relationship r = new Relationship(c, sc, Relationship.RelationshipType.IMPLEMENTATION);
				relationships.put(r.hashCode(), r);
			}
			ignore = false;
		}
		return relationships;
	}


}
