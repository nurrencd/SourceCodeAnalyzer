package OurStuff;

import java.util.Collection;

import soot.SootClass;

public interface Compiler {

	String compile(Filter filter, Collection<SootClass> classCollection);

}
