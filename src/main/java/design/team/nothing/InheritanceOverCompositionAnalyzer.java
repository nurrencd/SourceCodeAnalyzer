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
			if (!this.applyFilters(sc) && !data.get("properties", Properties.class).getProperty("classlist").contains(sc.getName())){
				continue;
			}
			if (!sc.hasSuperclass()){
				continue;
			}
			boolean valid = false;
			for (Relationship r : relationships){
				if (r.from.equals(sc) && r.type == RelationshipType.ASSOCIATION_ONE_TO_ONE && r.to.getPackageName().equals(sc.getPackageName())){
					System.out.println(r.to.getPackageName() + "-------------------------" + sc.getPackageName());
					valid = true;
					break;
				}
			}
			
			SootClass superSC = sc.getSuperclass();
			if (!valid){
				
				pattern.addClass("compositionOverInheritance", sc);
				for(Relationship r : (Collection<Relationship>) data.get("relationships", CustomCollection.class)){
					if(r.from.getShortName().equals(sc.getShortName()) && r.to.getShortName().equals(superSC.getShortName())){
						pattern.addRelationship("compositionOverInheritance", r);
					}
				}
			}
			
			
			//SootClass superSC = sc.getSuperclass();
			if(superSC.hasSuperclass() && !superSC.isAbstract()){
				System.out.println("found Class: " + sc.getName());
				
				for(Relationship r : (Collection<Relationship>) data.get("relationships", CustomCollection.class)){
					if(r.from.getShortName().equals(sc.getShortName()) 
							&& r.to.getShortName().equals(superSC.getShortName())
							&& (r.type == RelationshipType.DEPENDENCY_ONE_TO_MANY || r.type == RelationshipType.DEPENDENCY_ONE_TO_ONE)){
						pattern.addRelationship("compositionOverInheritance", r);
						pattern.addClass("compositionOverInheritance", sc);
					}
				}
//				if (!sc.getPackageName().equals(superSC.getPackageName())){
//					pattern.addClass("compositionOverInheritance", sc);
//					for(Relationship r : (Collection<Relationship>) data.get("relationships", CustomCollection.class)){
//						if(r.from.getShortName().equals(sc.getShortName()) && r.to.getShortName().equals(superSC.getShortName())){
//							pattern.addRelationship("compositionOverInheritance", r);
//						}
//					}
//				}
				
				Scene scene = data.get("scene", Scene.class);
				
				for (SootField f : sc.getFields()){
						
					
				}
				
				for (SootMethod m : sc.getMethods()){
					
//					for (SootMethod m2 : superSC.getMethods()){
//						if (m.getName().equals(m2.getName()) && !m2.isAbstract()){
//							pattern.addClass("compositionOverInheritance", sc);
//							for(Relationship r : (Collection<Relationship>) data.get("relationships", CustomCollection.class)){
//								if(r.from.getShortName().equals(sc.getShortName()) && r.to.getShortName().equals(superSC.getShortName())){
//									pattern.addRelationship("compositionOverInheritance", r);
//								}
//							}
//						}
//					}
				}
			}
			
		}
		data.put("compositionOverInheritance", pattern);
		return data;
	}

}
