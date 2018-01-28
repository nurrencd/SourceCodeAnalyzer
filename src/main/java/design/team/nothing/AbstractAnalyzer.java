package design.team.nothing;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public abstract class AbstractAnalyzer implements Analyzer{
	protected ArrayList<Filter> filters = new ArrayList<>();
	
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
	
	protected boolean applyFilters(SootClass c) {
		for (Filter f : this.filters) {
			if (f.ignore(c)) {
				return true;
			}
		}
		return false;
	}
	protected boolean applyFilters(SootMethod m) {
		for (Filter f : this.filters) {
			if (f.ignore(m)) {
				return true;
			}
		}
		return false;
	}
	protected boolean applyFilters(SootField sf) {
		for (Filter f : this.filters) {
			if (f.ignore(sf)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Pattern> getPatterns(Data data){
		List<Pattern> patterns = new ArrayList<Pattern>();
		for(Object o: data.getValues()){
			if(o instanceof Pattern){
				Pattern p = (Pattern) o;
				patterns.add(p);
			}
		}
		return patterns;
	}
	
}
