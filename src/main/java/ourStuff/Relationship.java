package ourStuff;

import soot.SootClass;

public class Relationship {
	public SootClass to, from;
	public RelationshipType type;
	
	public enum RelationshipType{
		INHERITANCE, 
		IMPLEMENTATION, 
		DEPENDENCY_ONE_TO_ONE, 
		DEPENDENCY_ONE_TO_MANY, 
		ASSOCIATION_ONE_TO_ONE, 
		ASSOCIATION_ONE_TO_MANY, 
		DEPENDENCY
	};
	
	public Relationship(SootClass from, SootClass to, RelationshipType r) {
		this.to = to;
		this.from = from;
		this.type = r;
	}
	
	@Override
	public int hashCode(){
		String str = this.to.getName() + this.from.getName() + this.type.toString();
//		System.out.println(this.to.getName() + this.from.getName() + this.type.toString() + "  " + str.hashCode());
		return str.hashCode();
	}
}
