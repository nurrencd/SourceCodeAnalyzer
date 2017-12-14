package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Hierarchy;
import soot.SootClass;

public class RecursiveAnalyzer implements Analyzer {
	
	private List<Filter> filters;

	@Override
	public Data analyze(Data data) {
		
		Collection<SootClass> classesToAdd = new ArrayList<>();
		//get superClasses
		for (SootClass c1 : data.classes) {
			if (c1.hasSuperclass() && !data.classes.contains(c1.getSuperclass())){
				System.out.println(c1.getShortName() + "   " + c1.getSuperclass().getShortName());
				classesToAdd.add(c1.getSuperclass());
			}
		}
		//getSuperInterfaces
		for (SootClass c1 : data.classes) {
			for (SootClass c2 : c1.getInterfaces()) {
				if (c2.getShortName().contains("$")){
					continue;
				}
				if (!data.classes.contains(c2)){
					System.out.println("Interface: " + c1.getShortName() + "   " + c2.getShortName());
					classesToAdd.add(c2);
				}
			}
		}
		data.classes.addAll(classesToAdd);
		if (classesToAdd.size() > 0){
			data = analyze(data);
		}
		
		return data;
	}

	@Override
	public void addFilter(Filter filter) {
		

	}

}
