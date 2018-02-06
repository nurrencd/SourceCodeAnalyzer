package design.team.nothing;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
		//if(prop.containsKey("main")){
			String mainClass = prop.getProperty("main");
			System.out.println(mainClass);
			sb.addDirectory(path)
				.setEntryClass(mainClass)
				.addEntryPointMatcher(new MainMethodMatcher(mainClass))
				.addExclusions(Arrays.asList(/*"soot.*", "polygot.*", "org.*", "com.*",*/ "design.team.nothing.Preprocessor"));
//		}else if(prop.containsKey("classlist")){
//			String[] arrayOfClasses = prop.getProperty("classlist").split(" ");
//			for(String s : arrayOfClasses){
//				sb.addDirectory(path)
//					.setEntryClass("design.team.nothing.App")
//					.addEntryPointMatcher(new MainMethodMatcher("design.team.nothing.App"))
//					.addExclusions(Arrays.asList("soot.*", "polygot.*", "org.*", "com.*", "design.team.nothing.Preprocessor"));
//				Scene scene =  sb.build();
//				System.out.println(s);
//				SootClass sc = scene.loadClassAndSupport(s);
//				classes.add(sc);
//				data.put("scene", scene);
//			}
//			return data;
//					
//		}else{
//			throw new IllegalArgumentException();			
//		}
		
		//add extra exclusions
		
		if (!prop.containsKey("java")) {
			sb.addExclusions(Arrays.asList("soot.*", "java.*", "javax.*", "sun.*"));
		}
		if (prop.containsKey("exclude")) {
			sb.addExclusions(Arrays.asList(prop.getProperty("exclude").split(" ")));
		}
//		sb.addExclusion("csse374.*");
		//other configs
		//build scene
		Scene scene = sb.build();
		ArrayList<SootClass> classesToRemove = new ArrayList<SootClass>();
	
//		for (SootClass c : scene.getClasses()){
//			if (c.getName().contains("soot.")){
//				classesToRemove.add(c);
//			}
//		}
//		for (SootClass c : classesToRemove){
//			scene.removeClass(c);
//		}
		
		if (prop.containsKey("exclude")){
			Collection<SootClass> classesRem = new HashSet<SootClass>();
			String[] clazzes = prop.getProperty("exclude").split(" ");
			for (SootClass c : scene.getClasses()){
				boolean rm = false;
				for (String s : clazzes){
					if (s.contains("*")) {
						if (c.getName().contains(s.substring(0, s.length()-1))){
							rm = true;
						}
					}else {
						if (c.getName().equals(s)){
							rm = true;
						}
					}
				}
				if (rm){
					classesRem.add(c);
				}
			}
			for (SootClass c : classesRem){
				scene.removeClass(c);
			}
		}
		
		if (prop.containsKey("classlist")){
			Collection<SootClass> classesRem = new HashSet<SootClass>();
			String[] clazzes = prop.getProperty("classlist").split(" ");
			for (String str : clazzes){
				if (!str.contains("java")){
					continue;
				}
				try {
					scene.loadClassAndSupport(str);
				}catch(Exception e){	
				}
			}
			for (SootClass c : scene.getClasses()){
				boolean rm = true;
				for (String s : clazzes){
					if (c.getName().equals(s)){
						rm = false;
					}
				}
				if (rm){
					classesRem.add(c);
				}
			}
			for (SootClass c : classesRem){
				scene.removeClass(c);
			}
		}
		
//		if (prop.containsKey("add")) {
//			String[] clazzes = prop.getProperty("add").split(" ");
//			for (String s : clazzes) {
//				try {
//					SootClass c = scene.loadClassAndSupport(s);
//				} catch (Exception e) {
//					System.out.println("Could not load class: " + s);
//				}
//			}
//		}
		
		data.put("scene", scene);
		Collection<SootClass> classesToStore = new ArrayList<>();
		if (prop.containsKey("classlist")){
			for (SootClass c : scene.getClasses()){
				classesToStore.add(c);
			}
		}else {
			for (SootClass c : scene.getApplicationClasses()){
				classesToStore.add(c);
			}
		}
		data.put("classes", classesToStore);
		
		
		return data;
	}

}
