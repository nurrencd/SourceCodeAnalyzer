package ourStuff;

import java.util.ArrayList;

public abstract class AbstractAnalyzer implements Analyzer{
	protected ArrayList<Filter> filters = new ArrayList<>();
	
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}
}
