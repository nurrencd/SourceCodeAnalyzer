package ourStuff;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import soot.Scene;
import soot.SootClass;

public class Data {
	private Map<String, Object> map;
	public Data() {
		this.map = new HashMap<String, Object>();
	}
	
	public void put(String key, Object value) {
		this.map.put(key, value);
	}
	
	public <T> T get(String key, Class<T> clazz) {
		Object o = this.map.get(key);
		return clazz.cast(o);
	}
	
	
}
