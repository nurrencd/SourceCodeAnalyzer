package ourStuff;

import java.util.Collection;
import java.util.Collections;

import soot.SootClass;

public class InheritanceOverCompositionAnalyzer extends AbstractAnalyzer{

	Pattern pattern = new InheritanceOverCompositionPattern();
	
	
	@Override
	public Data analyze(Data data) {
		Collection<SootClass> sootClasses = data.get("classes", Collection.class);
		for(SootClass sc : sootClasses){
			SootClass superSC = sc.getSuperclass();
			if(superSC.hasSuperclass() && !superSC.isAbstract()){
				pattern.addClass("compositionOverInheritance", sc);
				for(Relationship r: (Collection<Relationship>) data.get("relationship", Collections.class)){
					if(r.from.getShortName().equals(sc.getShortName()) && r.to.getShortName().equals(superSC.getShortName())){
						pattern.addRelationship("compositionOverInheritance", r);
					}
				}
			}
		}
		data.put("compositionOverInheritance", pattern);
		return data;
	}

}
