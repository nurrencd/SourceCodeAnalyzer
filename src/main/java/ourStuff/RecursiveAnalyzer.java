package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import soot.Hierarchy;
import soot.SootClass;

public class RecursiveAnalyzer extends AbstractAnalyzer {

	public Data analyze(Data data) {
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		Collection<SootClass> classes = data.get("classes", Collection.class);
		
		Collection<SootClass> classesToAdd = new ArrayList<>();
		//get superClasses
		for (SootClass c1 : classes) {
			if (c1.hasSuperclass() && !classes.contains(c1.getSuperclass())){

//				if (!this.applyFilters(c1.getSuperclass())){
//					continue;
//				}
				classesToAdd.add(c1.getSuperclass());
			}
		}
		//getSuperInterfaces
		for (SootClass c1 : classes) {
			for (SootClass c2 : c1.getInterfaces()) {
//				if (!this.applyFilters(c2)){
//					continue;
//				}
				if (c2.getShortName().contains("$")){
					continue;
				}
				if (!classes.contains(c2)){
//					System.out.println("Interface: " + c1.getShortName() + "   " + c2.getShortName());
					classesToAdd.add(c2);
				}
			}
		}
		classes.addAll(classesToAdd);
		if (classesToAdd.size() > 0){
			data = analyze(data);
		}
		
		return data;
	}

}
