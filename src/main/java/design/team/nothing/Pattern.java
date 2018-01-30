package design.team.nothing;

import java.util.Collection;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import soot.SootClass;

public class Pattern {
	private ListMultimap<String, SootClass> classes;
	private ListMultimap<String, Relationship> relationships;
	private PatternRenderer renderer;
	
	protected Pattern(PatternRenderer renderer) {
		this.classes = ArrayListMultimap.create();
		this.relationships = ArrayListMultimap.create();
		this.renderer = renderer;
	}
	public Collection<SootClass> getAppliedClasses(String s) {
		return classes.get(s);
	}

	public Collection<Relationship> getAppliedRelationships(String s) {
		return relationships.get(s);
	}
	
	public void addClass(String s, SootClass sootClass){
		classes.put(s, sootClass);
	}
	public void addRelationship(String s, Relationship r){
		relationships.put(s, r);
	}
	
	
	public Collection<String> getClassKeys(){
		return classes.keySet();
	}
	
	public Collection<String> getRelationshipKeys(){
		return relationships.keySet();
	}
	
	public String getDeclarationModification() {
		return this.renderer.getClassModification();
	}
	
	public String getRelationshipModification() {
		return this.renderer.getRelationshipModification();
	}
	

}
