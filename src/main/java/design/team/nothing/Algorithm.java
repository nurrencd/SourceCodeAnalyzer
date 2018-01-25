package design.team.nothing;

import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public interface Algorithm {
	public Collection<SootMethod> resolve(SootMethod m, Unit u, Scene s);
}
