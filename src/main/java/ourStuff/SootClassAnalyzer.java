package ourStuff;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Scene;

public class SootClassAnalyzer implements Analyzer {
	private List<Filter> filters;
	
	public SootClassAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
//		System.out.println("Recursive drcccrcrcrcrcrrcrcdrcdrcdrcdrcdrcd---------");
		String path = data.path.toFile().getAbsolutePath();
//		String dirToLoad = Paths.get(System.getProperty("user.dir"),  "build", "classes", "main").toString();
		if (!data.config.containsKey("-m")){
			throw new IllegalArgumentException();
		}
		String mainClass = data.config.get("-m").get(0);
		System.out.println(mainClass);
		SceneBuilder sb = SceneBuilder.create()
				.addDirectory(path)
				.setEntryClass(mainClass)
				.addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList("sun.*", "soot.*", "polygot.*", "org.*", "com.*"));
		
		//add extra exclusions
		
		if (!data.config.containsKey("-j")) {
			sb.addExclusions(Arrays.asList("java.*", "javax.*"));
		}
		if (data.config.containsKey("-e")) {
			sb.addExclusions(data.config.get("-e"));
		}
		//other configs

		//build scene
		Scene scene = sb.build();		
		data.scene = scene;	
		data.classes = scene.getApplicationClasses();
		return data;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
		
	}

}
