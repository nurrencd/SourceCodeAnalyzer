package design.team.nothing;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Preprocessor {
	public static final List<String> PROPERTIES = Collections.unmodifiableList(Arrays.asList(
			"path", "uml", "recursive", "depth",
			"classlist", "exclude", "sequence",
			"mainmethod", "filters", "java", "pattern"));

	private Map<String, Filter> filterMap;

	public Preprocessor() {
		filterMap = new HashMap<>();
		filterMap.put("public", new PublicFilter());
		filterMap.put("protected", new ProtectedFilter());
		filterMap.put("private", new PrivateFilter());
		filterMap.put("java", new JDKFilter());
	}

	public AnalyzerChain makePileline(String[] args, Data data) {
		Properties config = configGen(args);
		data.put("properties", config);
		AnalyzerChain listOfAnalyzers = new AnalyzerChain();
		listOfAnalyzers.add(new SootClassAnalyzer());
		

		// Should recursively iterate
		if (config.containsKey("recursive")) {
			listOfAnalyzers.add(new RecursiveAnalyzer());
		}

		// Create a Uml
		if (config.containsKey("uml")) {
			createUMLAnalyzers(config, listOfAnalyzers);
		}

		if (config.containsKey("sequence")) {
			AbstractAnalyzer seqAnal = new SequenceDiagramAnalyzer((String) config.get("sequence"));
			listOfAnalyzers.add(seqAnal);
			if (!config.containsKey("java")) {
				Filter jdk = new JDKFilter();
				seqAnal.addFilter(jdk);
			}

		}

		data.put("path", Paths.get(config.getProperty("path")));
		return listOfAnalyzers;
	}

	/**
	 * @param config
	 * @param listOfAnalyzers
	 */
	private void createUMLAnalyzers(Properties config, AnalyzerChain listOfAnalyzers) {
		AbstractAnalyzer impAnal = new ImplementationAnalyzer();
		AbstractAnalyzer inhAnal = new InheritenceAnalyzer();
		AbstractAnalyzer depAnal = new DependencyAnalyzer();
		AbstractAnalyzer assAnal = new AssociationAnalyzer();

		listOfAnalyzers.add(impAnal);
		listOfAnalyzers.add(inhAnal);
		listOfAnalyzers.add(assAnal);
		listOfAnalyzers.add(depAnal);

		addPatternAnalyzers(config, listOfAnalyzers);
		
		AbstractAnalyzer cGen = new ClassCodeGenAnalyzer();

		listOfAnalyzers.add(cGen);
		if (config.containsKey("filters")) {

			String instructions = config.getProperty("filters");
			for (String s : instructions.split(" "))
				cGen.addFilter(this.filterMap.get(s));
		}
		if (!config.containsKey("java")) {
			Filter jdk = new JDKFilter();
			impAnal.addFilter(jdk);
			inhAnal.addFilter(jdk);
			assAnal.addFilter(jdk);
			depAnal.addFilter(jdk);
			cGen.addFilter(jdk);
		}
	}

	
	private void addPatternAnalyzers(Properties config, AnalyzerChain listOfAnalyzers) {
		if(config.containsKey("pattern")){
			String pattern  = config.getProperty("pattern");
			String[] patternArray = pattern.split(" ");
			for(String p : patternArray){
				try {
					//Look in bin directory
					AbstractAnalyzer analyzer = (AbstractAnalyzer) Class.forName(p).newInstance();
					listOfAnalyzers.add(analyzer);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private Properties configGen(String[] args) {
		Path path = Paths.get(args[0]);
		Properties prop = new Properties();
		
		FileInputStream in;
		try {
			in = new FileInputStream(path.toFile());
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

	public void addFilter(String key, Filter filter) {
		this.filterMap.put(key, filter);
	}
}
