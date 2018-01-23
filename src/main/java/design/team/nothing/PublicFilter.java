package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class PublicFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		return !c.isPublic();
	}

	@Override
	public boolean ignore(SootMethod m) {
		return !m.isPublic();
	}

	@Override
	public boolean ignore(SootField f) {
		return !f.isPublic();
	}

}
