package ourStuff;

import java.util.Collection;

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
	

}
