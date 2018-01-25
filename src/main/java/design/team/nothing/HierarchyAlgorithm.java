package design.team.nothing;

import java.util.Collection;
import java.util.List;

import soot.Hierarchy;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class HierarchyAlgorithm implements Algorithm{

	@Override
	public Collection<SootMethod> resolve(SootMethod m, Unit u, Scene s) {
		Hierarchy th = s.getActiveHierarchy();
		Collection<SootMethod> possibleMethods = th.resolveAbstractDispatch(m.getDeclaringClass(), m);
		return possibleMethods;
	}

}
