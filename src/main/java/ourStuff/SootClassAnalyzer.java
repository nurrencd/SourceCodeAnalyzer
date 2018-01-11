package ourStuff;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Scene;
import soot.SootClass;

public class SootClassAnalyzer extends AbstractAnalyzer {
	private List<Filter> filters;
	
	public SootClassAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	public Data analyze(Data data) {
		String path = data.path.toFile().getAbsolutePath();

		SceneBuilder sb = SceneBuilder.create();
		System.out.println("In SootClass");
		if(data.config.containsKey("-m")){
			String mainClass = data.config.get("-m").get(0);
			System.out.println(mainClass);
			sb.addDirectory(path)
				.setEntryClass(mainClass)
				.addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList("soot.*", "polygot.*", "org.*", "com.*"));
		}else if(data.config.containsKey("-c")){
			List<String> arrayOfClasses = data.config.get("-c");
			for(String s : arrayOfClasses){
				Scene scene =  Scene.v();
				SootClass sc = scene.loadClassAndSupport(s);
				data.classes.add(sc);
				data.scene = scene;
			}
			return data;
					
		}else{
			throw new IllegalArgumentException();			
		}
		
		//add extra exclusions
		
		if (!data.config.containsKey("-j")) {
			sb.addExclusions(Arrays.asList("soot.*", "java.*", "javax.*", "sun.*"));
		}
		if (data.config.containsKey("-e")) {
			sb.addExclusions(data.config.get("-e"));
		}
		//other configs
		//build scene
		Scene scene = sb.addExclusions(Arrays.asList("soot.*", "polygot.*", "org.*", "com.*")).build();
		ArrayList<SootClass> classesToRemove = new ArrayList<SootClass>();
		for (SootClass c : scene.getClasses()){
			if (c.getName().contains("soot.")){
				classesToRemove.add(c);
			}
		}
		for (SootClass c : classesToRemove){
			scene.removeClass(c);
		}
		
		data.scene = scene;
		data.classes = scene.getApplicationClasses();
		return data;
	}

}
