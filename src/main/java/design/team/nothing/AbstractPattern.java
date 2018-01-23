package design.team.nothing;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import soot.SootClass;

public abstract class AbstractPattern implements Pattern {
	protected ListMultimap<String, SootClass> classes;
	protected ListMultimap<String, Relationship> relationships;
	
	protected AbstractPattern() {
		this.classes = ArrayListMultimap.create();
		this.relationships = ArrayListMultimap.create();
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
	

}
