package design.team.nothing;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class ExclusionFilter implements Filter{

	
	private String str;

	public ExclusionFilter(String str) {
		this.str = str;
	}
	
	@Override
	public boolean ignore(SootClass c) {
		if (str.contains("*")) {
			if (c.getName().contains(str.replace("*", ""))) {
				return true;
			}
		}else {
			if (c.getName().equals(str)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean ignore(SootMethod m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ignore(SootField f) {
		// TODO Auto-generated method stub
		return false;
	}

}
