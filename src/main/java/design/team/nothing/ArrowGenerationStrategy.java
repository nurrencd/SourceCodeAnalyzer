package design.team.nothing;

import soot.SootMethod;

public interface ArrowGenerationStrategy {

	public String genCallArrow(SootMethod from, SootMethod to);
	
	public String genReturnArrow(SootMethod from, SootMethod to);
	
}
