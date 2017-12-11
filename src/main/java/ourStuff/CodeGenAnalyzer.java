package ourStuff;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class CodeGenAnalyzer implements Analyzer {
	private List<Filter> filters;

	public CodeGenAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		return null;
	}

	private boolean filterClass(SootClass c) {
		String code;
		//if class gets filtered
		for (Filter filter : this.filters) {
			if (filter.ignore(c)) {
				return false;
			}
		}
		//filter things from soot classes
		for (SootMethod m : c.getMethods()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(m)) {
					c.removeMethod(m);
				}
			}
		}
		for (SootField f : c.getFields()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(f)) {
					c.removeField(f);
				}
			}
		}
		//done filtering
		
		
		return true;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}

}
