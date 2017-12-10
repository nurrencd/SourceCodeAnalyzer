package ourStuff;

import soot.SootClass;

public class Relationship {
	public SootClass to, from;
	public enum relationshipType{
		INHERITANCE, IMPLEMENTATION, ASSOCIATION, DEPENDENCY
	};
}
