package design.team.nothing;

import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public interface ResolutionStrategy {
	public Collection<SootMethod> resolve(List<Algorithm> list, SootMethod sm, Unit u, Scene sc);
}
