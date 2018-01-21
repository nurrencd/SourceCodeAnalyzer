package ourStuff;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import soot.Scene;
import soot.SootClass;

public class Data {
	public Collection<SootClass> classes;
	public Collection<Relationship> relationships;
	public ListMultimap<String, Object> config;
	public Properties prop;
	public Scene scene;
	public Path path;
	
	public Data() {
		classes = new ArrayList<>();
		relationships = new CustomCollection<>();
		config = ArrayListMultimap.create();
	}
	
	public void put(String key, Object value) {
		if (key.equals("properties")) {
			this.prop = (Properties) value;
			return;
		}
		config.put(key, value);
	}
	
	public <T> T get(String key, Class<T> clazz) {
		if (key.equals("properties")) {
			return clazz.cast(this.prop);
		}
		Object o = config.get(key);
		return clazz.cast(o);
	}
	
	
}
