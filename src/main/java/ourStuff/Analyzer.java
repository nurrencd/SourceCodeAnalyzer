package ourStuff;

import java.util.Collection;

public interface Analyzer {
	public Data analyze(Data data);
	
	public void addFilter(Filter filter);
}
