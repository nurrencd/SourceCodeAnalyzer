package ourStuff;

import soot.SootClass;

public class Relationship {
	public SootClass to, from;
	public RelationshipType type;
	
	public enum RelationshipType{
		INHERITANCE, IMPLEMENTATION, ONE_TO_ONE, ONE_TO_MANY, DEPENDENCY
	};
	
	public Relationship(SootClass from, SootClass to, RelationshipType r) {
		this.to = to;
		this.from = from;
		this.type = r;
	}
}
