package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class SingleResolutionStrategy implements ResolutionStrategy {

	@Override
	public Collection<SootMethod> resolve(List<Algorithm> list, SootMethod sm, Unit u, Scene sc) {
		Collection<SootMethod> returnList = new ArrayList<>();
		if (list.size() > 0) {
			returnList = list.get(0).resolve(sm, u, sc);
		}
		return returnList;
		
	}

}
