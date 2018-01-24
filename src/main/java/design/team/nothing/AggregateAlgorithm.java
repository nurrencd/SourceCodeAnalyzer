package design.team.nothing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class AggregateAlgorithm {
	private List<Algorithm> algs;
	private ResolutionStrategy strategy;
	
	public AggregateAlgorithm() {
		this.algs = new ArrayList<Algorithm>();
	}
	public void addAlgorithm(Algorithm a) {
		this.algs.add(a);
	}
	
	public void setResolutionStrategy(ResolutionStrategy rs) {
		this.strategy = rs;
	}
	
	public SootMethod resolve(SootMethod sm, Unit u, Scene scene) {
		Collection<SootMethod> potentialCandidates = this.strategy.resolve(this.algs, sm, u, scene);
		if (potentialCandidates.isEmpty()) {
			return sm;
		}
		return potentialCandidates.iterator().next();
	}
	
	

}
