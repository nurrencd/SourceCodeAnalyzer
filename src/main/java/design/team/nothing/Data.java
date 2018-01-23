package design.team.nothing;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	
	public List<Pattern> getPatterns(){
		List<Pattern> patterns = new ArrayList<Pattern>();
		for(Object o: this.map.values()){
			if(o instanceof Pattern){
				Pattern p = (Pattern) o;
				patterns.add(p);
			}
		}
		return patterns;
	}
	
}
