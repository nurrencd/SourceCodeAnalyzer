package ourStuff;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Preprocessor {
	public static final List<String> PROPERTIES = Collections.unmodifiableList(Arrays.asList("path", "uml", "recursive", "classlist",
			"exclude", "sequence", "mainmethod", "filters", "java"));
	
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
			createUMLAnalyzers(config, listOfAnalyzers);
		}
		
		if(config.containsKey("-s")){
			AbstractAnalyzer seqAnal = new SequenceDiagramAnalyzer(config.get("-s").get(0));
			listOfAnalyzers.add(seqAnal);
			if (!config.containsKey("-j")) {
				Filter jdk = new JDKFilter();
				seqAnal.addFilter(jdk);
			}
			
		}
		
		return listOfAnalyzers;
	}


	/**
	 * @param config
	 * @param listOfAnalyzers
	 */
	private void createUMLAnalyzers(Map<String, ArrayList<String>> config, AnalyzerChain listOfAnalyzers) {
		AbstractAnalyzer impAnal = new ImplementationAnalyzer();
		AbstractAnalyzer inhAnal = new InheritenceAnalyzer();
		AbstractAnalyzer depAnal = new DependencyAnalyzer();
		AbstractAnalyzer assAnal = new AssociationAnalyzer();
		
		listOfAnalyzers.add(impAnal);
		listOfAnalyzers.add(inhAnal);
		listOfAnalyzers.add(assAnal);
		listOfAnalyzers.add(depAnal);
		
		
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
			assAnal.addFilter(jdk);
			depAnal.addFilter(jdk);
		}
	}
	
	Map<String, ArrayList<String>> configGen(String[] args){
		Map<String, ArrayList<String>> config =  new HashMap<String, ArrayList<String>>();
		
		Path path = Paths.get(args[1]);
		Properties prop = new Properties();
		try {
			FileInputStream in = new FileInputStream(path.toFile());
			prop.load(in);
			//TODO:  Add the property file recursion
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		String flag = "";
//		ArrayList<String> configStringArray;
//		for(int i = 1; i < args.length; i++){
//			String e = args[i];
//			if(e.startsWith("-")){
//				configStringArray = new ArrayList<String>();
//				flag = e;
//				config.put(flag,configStringArray); //For flags that don't have a filter/value associated with it
//										// E.g. -u For generatingUML
//			}else{
//				config.get(flag).add(e);
//				System.out.println(flag + " " + config.get(flag).toString());
//			}
//		}
		return config;
	}
	
	public void addFilter(String key, Filter filter) {
		this.filterMap.put(key, filter);
	}
}
