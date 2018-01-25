package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class ChainResolutionStrategy implements ResolutionStrategy {

	@Override
	public Collection<SootMethod> resolve(List<Algorithm> list, SootMethod sm, Unit u, Scene sc) {
		Collection<SootMethod> returnList = new ArrayList<SootMethod>();
		int i = 0;
		while (returnList.size() == 0 && i < list.size()) {
			returnList =  list.get(i).resolve(sm, u, sc);
		}
		return returnList;
	}

}
