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
//		for (SootClass c : data.classes) {
//			data.relationships
//		}
		data.classes.forEach( (c)-> 
			{data.relationships.addAll(this.getRelationships(c));});
		return data;
	}

	private Collection<Relationship> getRelationships(SootClass c) {
		Collection<Relationship> relationships = new ArrayList<Relationship>();
		Collection<SootClass> interfaces = c.getInterfaces();

		for (SootClass sc : interfaces) {
			relationships.add(new Relationship(c, sc, Relationship.RelationshipType.IMPLEMENTATION));
		}
		SootClass superclass = c.getSuperclass();
		if (superclass != null) {
			relationships.add(new Relationship(c, superclass, Relationship.RelationshipType.INHERITANCE));
		}

		return relationships;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);

	}

}
