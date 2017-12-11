package ourStuff;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

import soot.Scene;
import soot.SootClass;

public class Data {
	public Collection<SootClass> classes;
	public Collection<Relationship> relationships;
	public Map<String, String> config;
	public Scene scene;
	public Path path;
	
}
