package design.team.nothing;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Scene;
import soot.SootClass;

public class SootClassAnalyzer extends AbstractAnalyzer {
	private List<Filter> filters;
	
	public SootClassAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		data.put("classes", new ArrayList<SootClass>());
		data.put("relationships", new CustomCollection<Relationship>());
		
		String path = data.get("path", Path.class).toFile().getAbsolutePath();
		Properties prop = data.get("properties", Properties.class);
		Collection<SootClass> classes = data.get("classes", Collection.class);

		System.out.println(path.toString());
		SceneBuilder sb = SceneBuilder.create();
		System.out.println("In SootClass");
		if(prop.containsKey("main")){
			String mainClass = prop.getProperty("main");
			System.out.println(mainClass);
			sb.addDirectory(path)
				.setEntryClass(mainClass)
				.addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList("soot.*", "polygot.*", "org.*", "com.*"));
		}else if(prop.containsKey("classlist")){
			String[] arrayOfClasses = prop.getProperty("classlist").split(" ");
			for(String s : arrayOfClasses){
				Scene scene =  Scene.v();
				SootClass sc = scene.loadClassAndSupport(s);
				classes.add(sc);
				data.put("scene", scene);
			}
			return data;
					
		}else{
			throw new IllegalArgumentException();			
		}
		
		//add extra exclusions
		
		if (!prop.containsKey("java")) {
			sb.addExclusions(Arrays.asList("soot.*", "java.*", "javax.*", "sun.*"));
		}
		if (prop.containsKey("exclude")) {
			sb.addExclusions(Arrays.asList(prop.getProperty("exclude").split(" ")));
		}
		//other configs
		//build scene
		Scene scene = sb.build();
		ArrayList<SootClass> classesToRemove = new ArrayList<SootClass>();
		for (SootClass c : scene.getClasses()){
			if (c.getName().contains("soot.")){
				classesToRemove.add(c);
			}
		}
		for (SootClass c : classesToRemove){
			scene.removeClass(c);
		}

		data.put("scene", scene);
		data.put("classes", scene.getApplicationClasses());
		return data;
	}

}
