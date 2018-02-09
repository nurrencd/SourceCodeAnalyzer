package design.team.nothing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import soot.Scene;
import soot.SootMethod;
import soot.Unit;

public class AggregateAlgorithm implements Algorithm{
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
	
	public List<SootMethod> resolve(SootMethod sm, Unit u, Scene scene) {
		Collection<SootMethod> potentialCandidates = this.strategy.resolve(this.algs, sm, u, scene);
		if (potentialCandidates.isEmpty()) {
			return null;
		}
		System.out.println(potentialCandidates);
		return Arrays.asList(potentialCandidates.iterator().next());
	}
	
	

}
