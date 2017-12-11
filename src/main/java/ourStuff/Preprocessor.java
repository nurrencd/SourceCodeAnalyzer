package ourStuff;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Preprocessor {
	
	Collection<Analyzer> makePileline(String[] args, Data data){
		Map<String, String> config  = configGen(args);
		data.config = config;
		Collection<Analyzer> listOfAnalyzers = new ArrayList<>();
		listOfAnalyzers.add(new RecursiveAnalyzer());
		if(config.containsKey("-u")){
			listOfAnalyzers.add(new RelationshipAnalyzer());
			CodeGenAnalyzer cGen = new CodeGenAnalyzer();
			listOfAnalyzers.add(cGen);
			if(config.containsKey("-f")){
				String instruction = config.get("-f");
				if(instruction.equals("public")){
					cGen.addFilter(new PublicFilter());
				}else if(instruction.equals("protected")){
					cGen.addFilter(new ProtectedFilter());
				}else if(instruction.equals("private")){
					cGen.addFilter(new PrivateFilter());
				}
			}
		}
		
		return listOfAnalyzers;
	}
	
	Map<String, String> configGen(String[] args){
		Map<String, String> config =  new HashMap<String, String>();
		String flag = "";
		for(int i = 1; i < args.length; i++){
			String e = args[i];
			if(e.startsWith("-")){
				flag = e;
				config.put(flag, ""); //For flags that don't have a filter/value associated with it
										// E.g. -u For generating UML
			}else{
				config.put(flag, e);
			}
		}
		return config;
	}
}
