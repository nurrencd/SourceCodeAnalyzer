package design.team.nothing;

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
		Relationship arrowCheckAssMany = new Relationship(r.from, r.to, 
				Relationship.RelationshipType.ASSOCIATION_ONE_TO_MANY);
		Relationship arrowCheckAssOne = new Relationship(r.from, r.to, 
				Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE);
		Relationship arrowCheckDepMany = new Relationship(r.from, r.to, 
				Relationship.RelationshipType.DEPENDENCY_ONE_TO_MANY);
		Relationship arrowCheckDepOne = new Relationship(r.from, r.to, 
				Relationship.RelationshipType.DEPENDENCY_ONE_TO_ONE);
		switch(r.type){
		case ASSOCIATION_ONE_TO_MANY:
			if (hash.containsKey(arrowCheckAssOne.hashCode())){
				hash.remove(arrowCheckAssOne.hashCode());
			}
			hash.put(r.hashCode(), r);
			return true;
//			break;
		case ASSOCIATION_ONE_TO_ONE:
			if (!hash.containsKey(arrowCheckAssMany.hashCode())){
				return false;
			}
			hash.put(r.hashCode(), r);
			return true;
//			break;
		case DEPENDENCY_ONE_TO_MANY:
			if (hash.containsKey(arrowCheckDepOne.hashCode())){
				hash.remove(arrowCheckDepOne.hashCode());
			}
			hash.put(r.hashCode(), r);
			return true;
//			break;
		case DEPENDENCY_ONE_TO_ONE:
			if (hash.containsKey(arrowCheckDepMany.hashCode()) || hash.containsKey(arrowCheckDepOne.hashCode())){
				return false;
			}
			hash.put(r.hashCode(), r);
			break;
		case IMPLEMENTATION:
			hash.put(r.hashCode(), r);
			break;
		case INHERITANCE:
			hash.put(r.hashCode(), r);
			break;
		default:
			break;
			
		}
//		if (r.type == Relationship.RelationshipType.ASSOCIATION_ONE_TO_ONE){
//		
//		}
//		if (r.type == Relationship.RelationshipType.DEPENDENCY_ONE_TO_MANY 
//				|| r.type == Relationship.RelationshipType.DEPENDENCY_ONE_TO_ONE) {
//			
//			if (hash.containsKey(arrowCheckAssMany.hashCode()) || hash.containsKey(arrowCheckAssOne.hashCode())){
//				return false;
//			}
//		}
//		
//		hash.put(r.hashCode(), r);
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
