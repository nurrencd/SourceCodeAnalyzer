package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import soot.SootClass;

public class ImplementationAnalyzer extends AbstractAnalyzer {

	public ImplementationAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		classes.forEach((c) -> {
			rels.addAll(this.getImplementations(c));
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
