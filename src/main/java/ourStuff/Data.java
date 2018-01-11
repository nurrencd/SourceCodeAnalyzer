package ourStuff;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import soot.Scene;
import soot.SootClass;

public class Data {
	public Collection<SootClass> classes;
	public Collection<Relationship> relationships;
	public Map<String, ArrayList<String>> config;
	public Scene scene;
	public Path path;
	// ADD PRIVATE FILTER METHOD TO ANALYZERS TO CLEAN UP DA CODE
	
	public Data() {
		classes = new ArrayList<>();
		relationships = new HashSet<>();
		config = new HashMap<>();
	}
	
	public void addRelationship(Relationship r){
		if (!relationships.contains(r)){
			relationships.add(r);
		}
	}

	
}
