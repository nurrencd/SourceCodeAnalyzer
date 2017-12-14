package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.SootClass;

public class RelationshipAnalyzer implements Analyzer {
	private List<Filter> filters;

	public RelationshipAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		// System.out.println("Relationship
		// drcccrcrcrcrcrrcrcdrcdrcdrcdrcdrcd---------");
		data.classes.forEach((c) -> {
			data.relationships.addAll(this.getRelationships(c));
		});
		return data;
	}

	private Collection<Relationship> getRelationships(SootClass c) {
		Collection<Relationship> relationships = new ArrayList<Relationship>();
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
				relationships.add(new Relationship(c, sc, Relationship.RelationshipType.IMPLEMENTATION));
			}
			ignore = false;
		}
		// add super class relationship
		if(c.hasSuperclass()){
			SootClass superclass = c.getSuperclass();
			
			ignore = false;
			for (Filter f : this.filters) {
				
				if (f.ignore(superclass)) {
					ignore = true;
				}
			}
			if (!ignore) {
				relationships.add(new Relationship(c, superclass, Relationship.RelationshipType.INHERITANCE));
			}
		}

		return relationships;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);

	}

}
