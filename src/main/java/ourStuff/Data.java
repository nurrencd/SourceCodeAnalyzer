package ourStuff;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import soot.Scene;
import soot.SootClass;

public class Data {
	public Collection<SootClass> classes;
	public Collection<Relationship> relationships;
	public Map<String, ArrayList<String>> config;
	public Scene scene;
	public Path path;
	public Map<String, String> stringMap;
	// ADD PRIVATE FILTER METHOD TO ANALYZERS TO CLEAN UP DA CODE
	
	public Data() {
		classes = new ArrayList<>();
		relationships = new ArrayList<>();
		config = new HashMap<>();
		stringMap = new HashMap<>();
	}
	
	public void addString(String key, String value) {
		this.stringMap.put(key, value);
	}
	
}
