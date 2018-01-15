package ourStuff;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class CustomCollection<T> implements Collection<T> {

	private HashMap<Integer, Relationship> hash;

	public CustomCollection() {
		this.hash = new HashMap<>();
	}

	@Override
	public boolean add(Object e) {
		// TODO Auto-generated method stub
		Relationship r = (Relationship) e;
		if (r.type == Relationship.RelationshipType.DEPENDENCY_ONE_TO_MANY 
				|| r.type == Relationship.RelationshipType.DEPENDENCY_ONE_TO_ONE) {
			Relationship arrowCheckMany = new Relationship(r.from, r.to, 
					Relationship.RelationshipType.ASSOCIATION_ONE_TO_MANY);
			Relationship arrowCheckOne = new Relationship(r.from, r.to, 
					Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE);
			if (hash.containsKey(arrowCheckMany.hashCode()) || hash.containsKey(arrowCheckOne.hashCode())){
				return false;
			}
		}
		hash.put(r.hashCode(), r);
		return true;
	}

	@Override
	public boolean addAll(Collection c) {
		// TODO Auto-generated method stub
		for (Object o : c){
			this.add(o);
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
