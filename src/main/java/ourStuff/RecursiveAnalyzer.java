package ourStuff;

import java.util.Collection;
import java.util.List;

import soot.Hierarchy;
import soot.SootClass;

public class RecursiveAnalyzer implements Analyzer {
	
	private List<Filter> filters;

	@Override
	public Data analyze(Data data) {
		Hierarchy h = data.scene.getActiveHierarchy();
		Collection<SootClass> superclasses;
		//get superClasses
		for (SootClass c1 : data.classes) {
			for (SootClass c2 : h.getSuperclassesOf(c1)) {
				if (!data.classes.contains(c2)){
					data.classes.add(c2);
				}
			}
		}
		//getSuperInterfaces
		for (SootClass c1 : data.classes) {
			for (SootClass c2 : h.getSuperinterfacesOf(c1)) {
				if (!data.classes.contains(c2)){
					data.classes.add(c2);
				}
			}
		}
		
		
		return null;
	}

	@Override
	public void addFilter(Filter filter) {
		

	}

}
