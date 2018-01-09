package ourStuff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Preprocessor {
	private Map<String, Filter> filterMap;
	
	public Preprocessor() {
		filterMap = new HashMap<>();
		filterMap.put("public", new PublicFilter());
		filterMap.put("protected", new ProtectedFilter());
		filterMap.put("private", new PrivateFilter());
		filterMap.put("java", new JDKFilter());
	}
	
	
	public AnalyzerChain makePileline(String[] args, Data data){
		Map<String, ArrayList<String>> config  = configGen(args);
		data.config = config;
		AnalyzerChain listOfAnalyzers = new AnalyzerChain();
		listOfAnalyzers.add(new SootClassAnalyzer());
		
		//Should recursively iterate
		if (data.config.containsKey("-r")) {
			listOfAnalyzers.add(new RecursiveAnalyzer());
		}
		
		//Create a Uml
		if(config.containsKey("-u")){
			AbstractAnalyzer impAnal = new ImplementationAnalyzer();
			AbstractAnalyzer inhAnal = new InheritenceAnalyzer();
			AbstractAnalyzer assAnal = new AssociationAnalyzer();
			listOfAnalyzers.add(impAnal);
			listOfAnalyzers.add(inhAnal);
			listOfAnalyzers.add(assAnal);
			AbstractAnalyzer cGen = new ClassCodeGenAnalyzer();
			
			
			listOfAnalyzers.add(cGen);
			if(config.containsKey("-f")){
				
				List<String> instructions = config.get("-f");
				for (String s : instructions)
					cGen.addFilter(this.filterMap.get(s));
			}
			if (!config.containsKey("-j")) {
				Filter jdk = new JDKFilter();
				impAnal.addFilter(jdk);
				inhAnal.addFilter(jdk);
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
	
	public void addFilter(String key, Filter filter) {
		this.filterMap.put(key, filter);
	}
}
