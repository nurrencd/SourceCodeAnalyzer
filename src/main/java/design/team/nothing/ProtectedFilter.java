package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class ProtectedFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		return !(c.isProtected() || c.isPublic());
	}

	@Override
	public boolean ignore(SootMethod m) {
		return!(m.isProtected() || m.isPublic());
	}

	@Override
	public boolean ignore(SootField f) {
		return !(f.isProtected() || f.isPublic());
	}

}
