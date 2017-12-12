package ourStuff;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class JDKFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		if (c.isJavaLibraryClass()) {
			System.out.println("JDKFilter is ignoring " + c.getName());
		}
		return c.isJavaLibraryClass();
	}

	@Override
	public boolean ignore(SootMethod m) {
		return !m.isJavaLibraryMethod();
	}

	@Override
	public boolean ignore(SootField f) {
		return false;
	}

}