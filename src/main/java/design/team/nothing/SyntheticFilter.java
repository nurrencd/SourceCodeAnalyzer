package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class SyntheticFilter implements Filter{

	@Override
	public boolean ignore(SootClass c) {
		// TODO Auto-generated method stub
		return c.getName().contains("$");
	}

	@Override
	public boolean ignore(SootMethod m) {
		// TODO Auto-generated method stub
		return m.getName().contains("$");
	}

	@Override
	public boolean ignore(SootField f) {
		// TODO Auto-generated method stub
		return f.getName().contains("$");
	}

}
