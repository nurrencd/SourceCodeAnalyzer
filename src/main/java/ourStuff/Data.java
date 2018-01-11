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
		relationships = new CustomCollection<>();
		config = new HashMap<>();
	}
	
	private class CustomCollection<Relationship> implements Collection {

		private HashMap<Integer, Relationship> hash;

		public CustomCollection(){
			this.hash = new HashMap<>();
		}

		@Override
		public boolean add(Object e) {
			// TODO Auto-generated method stub
			Relationship r = (Relationship) e;
			hash.put(r.hashCode(), r);
			return true;
		}

		@Override
		public boolean addAll(Collection c) {
			// TODO Auto-generated method stub
			for (Object o : c){
				Relationship r = (Relationship) o;
				hash.put(r.hashCode(), r);
			}
			return true;
		}

		@Override
		public void clear() {
			// TODO Auto-generated method stub
			hash = new HashMap<>();
		}

		@Override
		public boolean contains(Object o) {
			// TODO Auto-generated method stub
			return hash.containsValue(o);
		}

		@Override
		public boolean containsAll(Collection c) {
			for (Object o : c){
				if (!hash.containsValue(o)){
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return hash.isEmpty();
		}

		@Override
		public Iterator iterator() {
			// TODO Auto-generated method stub
			return hash.values().iterator();
		}

		@Override
		public boolean remove(Object o) {
			// TODO Auto-generated method stub
			Relationship r = (Relationship) o;
			return hash.remove(r.hashCode(), r);
		}

		@Override
		public boolean removeAll(Collection c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean retainAll(Collection c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return hash.size();
		}

		@Override
		public Object[] toArray() {
			// TODO Auto-generated method stub
			return hash.values().toArray();
		}

		@Override
		public Object[] toArray(Object[] a) {
			// TODO Auto-generated method stub
			return hash.values().toArray(a);
		}
		
	}
}
