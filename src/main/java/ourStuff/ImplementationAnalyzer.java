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
			data.relationships.addAll(this.getImplementations(c));
		});
		return data;
	}

	private Collection<Relationship> getImplementations(SootClass c) {
		Collection<Relationship> relationships = new ArrayList<Relationship>();
		Collection<SootClass> interfaces = c.getInterfaces();
		//add interface relationships
		for (SootClass sc : interfaces) {
			if (!this.applyFilters(sc) && !sc.getShortName().contains("$")) {
				Relationship r = new Relationship(c, sc, Relationship.RelationshipType.IMPLEMENTATION);
				relationships.add(r);
			}
		}
		return relationships;
	}


}
