package ourStuff;

import java.nio.file.Paths;
import java.util.Arrays;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Scene;

public class RecursiveAnalyzer implements Analyzer {

	@Override
	public Data analyze(Data data) {
		String path = data.path.toFile().getAbsolutePath();
		String dirToLoad = Paths.get(System.getProperty("user.dir"),  "build", "classes", "main").toString();
		if (!data.config.containsKey("-m")){
			throw new IllegalArgumentException();
		}
		String mainClass = data.config.get("-m");
		Scene scene = SceneBuilder.create().addDirectory(dirToLoad)
				.setEntryClass(mainClass).addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList("sun.*", "soot.*", "polygot.*", "org.*", "com.*"))
				.build();
		
			
		
		return null;
	}

	@Override
	public void addFilter(Filter filter) {
		// TODO Auto-generated method stub
		
	}

}
