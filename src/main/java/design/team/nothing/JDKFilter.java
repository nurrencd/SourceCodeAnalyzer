package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class JDKFilter implements Filter {

	@Override
	public boolean ignore(SootClass c) {
		
		return c.isJavaLibraryClass() || c.getName().contains("java") || c.getName().equals("void") 
				|| c.getName().contains("google") || c.getName().contains("soot") || c.getName().contains("org")
				|| c.getName().contains("core.") || c.getName().contains("h.ST");
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
