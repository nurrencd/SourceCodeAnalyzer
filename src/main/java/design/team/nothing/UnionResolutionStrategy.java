package design.team.nothing;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class UnionResolutionStrategy implements ResolutionStrategy {

	@Override
	public Collection<SootMethod> resolve(List<Algorithm> list, SootMethod sm, Unit u, Scene sc) {
		Set<SootMethod> classSet = new HashSet<SootMethod>();
		for (Algorithm a : list) {
			classSet.addAll(a.resolve(sm, u, sc));
		}
		return classSet;
	}

}
