package OurStuff;

import java.util.Collection;

import soot.SootClass;

public class BasicUMLCompiler implements Compiler{

	@Override
	public String compile(Filter filter, Collection<SootClass> classCollection) {
		filter.filter(classCollection);
		return null;
	}

}
