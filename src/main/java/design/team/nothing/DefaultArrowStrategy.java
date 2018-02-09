package design.team.nothing;

import soot.SootMethod;

public class DefaultArrowStrategy implements ArrowGenerationStrategy{

	@Override
	public String genCallArrow(SootMethod from, SootMethod to) {
		StringBuilder sb = new StringBuilder();
		sb.append("activate " + from.getDeclaringClass().getName());
		sb.append("\n");
		sb.append(from.getDeclaringClass().getName() + " -> " + to.getDeclaringClass().getName() + " : " + to.getSubSignature());
		sb.append('\n');
		return sb.toString();
	}
	
	@Override
	public String genReturnArrow(SootMethod from, SootMethod to) {
		StringBuilder sb = new StringBuilder();
		sb.append(to.getDeclaringClass().getName() + " <-- " + from.getDeclaringClass().getName());
		sb.append('\n');
		sb.append("deactivate " + from.getDeclaringClass().getName());
		sb.append("\n");
		return sb.toString();
	}

}
