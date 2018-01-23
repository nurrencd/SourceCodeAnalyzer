package ourStuff;

import java.util.Collection;
import java.util.Properties;

import soot.SootClass;
import soot.SootField;
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
			//get static field of its own type
			for (SootField f : sc.getFields()){
				if (f.getType().toString().equals(sc.getName()) && f.isStatic()){
					System.out.println("FOUND A SINGLETON, BOYS");
					singletonTraits++;
					break;
				}
			}
			//get static method returning itself
			for (SootMethod m : sc.getMethods()){
				if (m.getReturnType().toString().equals(sc.getName()) && m.isStatic()){
					System.out.println("FOUND A SINGLETON, BOYS");
					singletonTraits++;
					break;
				}
			}
			if (singletonTraits==2){
				this.pattern.addClass("singleton", sc);
			}
		}
		data.put("singleton", this.pattern);
		
		return data;
	}

}
