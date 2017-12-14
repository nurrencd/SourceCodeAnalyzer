package ourStuff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Preprocessor {
	
	public Collection<Analyzer> makePileline(String[] args, Data data){
		Map<String, ArrayList<String>> config  = configGen(args);
		data.config = config;
		Collection<Analyzer> listOfAnalyzers = new ArrayList<>();
		listOfAnalyzers.add(new SootClassAnalyzer());
		if (data.config.containsKey("-r")) {
			listOfAnalyzers.add(new RecursiveAnalyzer());
		}
		//TODO: refactor
		if(config.containsKey("-u")){
			Analyzer relAnal = new RelationshipAnalyzer();
			listOfAnalyzers.add(relAnal);
			CodeGenAnalyzer cGen = new CodeGenAnalyzer();
			listOfAnalyzers.add(cGen);
			if(config.containsKey("-f")){
				
				String instruction = config.get("-f").get(0);
				if(instruction.equals("public")){
					cGen.addFilter(new PublicFilter());
				}else if(instruction.equals("protected")){
					cGen.addFilter(new ProtectedFilter());
				}else if(instruction.equals("private")){
					cGen.addFilter(new PrivateFilter());
				}
			}
			if (config.containsKey("-j")) {
				relAnal.addFilter(new JDKFilter());
			}
		}
		
		return listOfAnalyzers;
	}
	
	Map<String, ArrayList<String>> configGen(String[] args){
		Map<String, ArrayList<String>> config =  new HashMap<String, ArrayList<String>>();
		String flag = "";
		ArrayList<String> configStringArray;
		for(int i = 1; i < args.length; i++){
			String e = args[i];
			if(e.startsWith("-")){
				configStringArray = new ArrayList<String>();
				flag = e;
				config.put(flag,configStringArray); //For flags that don't have a filter/value associated with it
										// E.g. -u For generating UML
			}else{
				config.get(flag).add(e);
				System.out.println(flag + " " + config.get(flag).toString());
			}
		}
		return config;
	}
}
