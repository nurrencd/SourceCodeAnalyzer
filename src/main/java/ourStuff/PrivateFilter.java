package ourStuff;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class PrivateFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		return false;
	}

	@Override
	public boolean ignore(SootMethod m) {
		return false;
	}

	@Override
	public boolean ignore(SootField f) {
		return false;
	}

}
