package ourStuff;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Scene;

public class RecursiveAnalyzer implements Analyzer {
	private List<Filter> filters;
	
	public RecursiveAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		System.out.println("Recursive drcccrcrcrcrcrrcrcdrcdrcdrcdrcdrcd---------");
		String path = data.path.toFile().getAbsolutePath();
		String dirToLoad = Paths.get(System.getProperty("user.dir"),  "build", "classes", "main").toString();
		if (!data.config.containsKey("-m")){
			throw new IllegalArgumentException();
		}
		String mainClass = data.config.get("-m");
		System.out.println(mainClass);
		Scene scene = SceneBuilder.create()
				.addDirectory(dirToLoad)
				.setEntryClass(mainClass)
				.addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList("sun.*", "soot.*", "polygot.*", "org.*", "com.*"))
				.build();
		
		data.scene = scene;	
		data.classes = scene.getApplicationClasses();
		return data;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
		
	}

}
