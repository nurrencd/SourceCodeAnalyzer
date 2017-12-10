package ourStuff;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class ProtectedFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		return !c.isProtected();
	}

	@Override
	public boolean ignore(SootMethod m) {
		return!m.isProtected();
	}

	@Override
	public boolean ignore(SootField f) {
		return !f.isProtected();
	}

}
