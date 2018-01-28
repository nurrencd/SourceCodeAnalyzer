package design.team.nothing;

import java.util.Collection;
import java.util.Properties;

import design.team.nothing.Relationship.RelationshipType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;

public class InheritanceOverCompositionAnalyzer extends AbstractAnalyzer{

	Pattern pattern = new InheritanceOverCompositionPattern();
	
	
	@Override
	public Data analyze(Data data) {
		Collection<SootClass> sootClasses = data.get("classes", Collection.class);
		Collection<Relationship> relationships = data.get("relationships", Collection.class);
		for(SootClass sc : sootClasses){
			//String str = data.get("properties", Properties.class).getProperty("classlist");
//			if (this.applyFilters(sc) && (str == null || !str.contains(sc.getName()))){
//				continue;
//			}
			if (!data.get("properties", Properties.class).containsKey("classlist")){
				
				if (this.applyFilters(sc)){
					continue;
				}
			}else {
				String str = data.get("properties", Properties.class).getProperty("classlist");
				if (this.applyFilters(sc) &&  !str.contains(sc.getName())){
					continue;
				}
			}
			
			SootClass superSC;
			if (sc.hasSuperclass()){
				superSC = sc.getSuperclass();
			}else {
				continue;
			}

			if(superSC.hasSuperclass() /*&& !superSC.isAbstract()*/){
				System.out.println("found Class: " + sc.getName());
				
				for(Relationship r : (Collection<Relationship>) data.get("relationships", CustomCollection.class)){
					if(r.from.getShortName().equals(sc.getShortName()) && r.to.getShortName().equals(superSC.getShortName())){
						pattern.addRelationship("compositionOverInheritance", r);
						pattern.addClass("compositionOverInheritance", sc);
					}
				}
				
				Scene scene = data.get("scene", Scene.class);
				
	
			}
			
		}
		data.put("compositionOverInheritance", pattern);
		return data;
	}

}
