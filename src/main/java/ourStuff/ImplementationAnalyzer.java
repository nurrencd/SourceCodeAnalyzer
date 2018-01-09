package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		return relationships;
	}


}
