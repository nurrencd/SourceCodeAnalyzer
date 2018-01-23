package design.team.nothing;

import java.util.Collection;
import java.util.List;

import soot.SootClass;

public interface Pattern {
	public Collection<SootClass> getAppliedClasses(String s);
	public Collection<Relationship> getAppliedRelationships(String s);
	public String getDeclarationModification();
	public String getRelationshipModification();
	public void addClass(String s, SootClass sootClass);
	public void addRelationship(String s, Relationship r);
	public Collection<String> getClassKeys();
	public Collection<String> getRelationshipKeys();
}
