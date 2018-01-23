package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public interface Filter {
	public boolean ignore(SootClass c);
	public boolean ignore(SootMethod m);
	public boolean ignore(SootField f);
	
}
