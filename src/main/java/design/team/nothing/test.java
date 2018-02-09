package design.team.nothing;

import soot.SootMethod;

public class test implements ArrowGenerationStrategy {

	@Override
	public String genCallArrow(SootMethod from, SootMethod to) {
		return "meme";
	}

	@Override
	public String genReturnArrow(SootMethod from, SootMethod to) {
		return "Supreme";
	}


}
