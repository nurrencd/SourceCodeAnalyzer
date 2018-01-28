package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public Collection<Object> getValues(){
		return this.map.values();
	}
	
}
