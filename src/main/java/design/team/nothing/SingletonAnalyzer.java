package design.team.nothing;

import java.util.Collection;
import java.util.Properties;

import soot.SootClass;
import soot.SootMethod;

public class SingletonAnalyzer extends AbstractAnalyzer {
	private Pattern pattern;
	
	public SingletonAnalyzer(){
		this.pattern = new SingletonPattern();
	}

	@Override
	public Data analyze(Data data) {
		Collection<SootClass> sootClasses = data.get("classes", Collection.class);
		for(SootClass sc : sootClasses){
			if (!this.applyFilters(sc) && !data.get("properties", Properties.class).getProperty("classlist").contains(sc.getName())){
				continue;
			}
			int singletonTraits = 0;
			if(sc.isAbstract() || sc.isInterface()){
				continue;
			}
			//get static field of its own type
//			for (SootField f : sc.getFields()){
//				if (f.getType().toString().equals(sc.getName()) && f.isStatic()){
//					System.out.println("FOUND A SINGLETON, BOYS");
//					singletonTraits++;
//					break;
//				}
//			}
			//get static method returning itself
			for (SootMethod m : sc.getMethods()){
				if (m.isConstructor() && !m.isPrivate()){
					singletonTraits--;
				}
				if (m.getReturnType().toString().equals(sc.getName()) && m.isStatic()){
					singletonTraits++;
					break;
				}
			}
			if (singletonTraits==1){
				this.pattern.addClass("singleton", sc);
				System.out.println(sc.getName());
			}
		}
		data.put("singleton", this.pattern);
		
		return data;
	}

}
