package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class IntersectionResolutionStrategy implements ResolutionStrategy {

	@Override
	public Collection<SootMethod> resolve(List<Algorithm> list, SootMethod sm, Unit u, Scene sc) {

		Collection<SootMethod> toReturn = new ArrayList<>();
		if (list.size() > 0) {
			toReturn = list.get(0).resolve(sm, u, sc);
		}
		for (int i = 1; i < list.size(); i++) {
			toReturn =  intersection(toReturn, list.get(i).resolve(sm, u, sc));
		}
		return toReturn;
	}
	
	private Collection<SootMethod> intersection(Collection<SootMethod> l1, Collection<SootMethod> l2){
		List<SootMethod> lists = new ArrayList<SootMethod>();
		for (SootMethod m : l2) {
			if (l1.contains(m)) {
				lists.add(m);
			}
		}
		return lists;
	}

}
