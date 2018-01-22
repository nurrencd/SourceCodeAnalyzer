package ourStuff;

import java.util.Collection;

import soot.SootClass;

public interface Pattern {
	public Collection<SootClass> getAppliedClasses(String s);
	public Collection<Relationship> getAppliedRelationships(String s);
	public String getDeclarationModification();
	public String getPostDeclarationModification();
}
