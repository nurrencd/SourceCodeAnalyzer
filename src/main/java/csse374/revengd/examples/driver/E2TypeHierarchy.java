package csse374.revengd.examples.driver;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import csse374.revengd.soot.MainMethodMatcher;
import csse374.revengd.soot.SceneBuilder;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;

public class E2TypeHierarchy implements Runnable {
	@Override
	public void run() {
		String dirToLoad = Paths.get(System.getProperty("user.dir"), "build", "classes", "main").toString();
		
		Scene scene = SceneBuilder.create()
				.addDirectory(dirToLoad)												// Add the directory from which to load the file or jars
				.setEntryClass("csse374.revengd.examples.fixtures.CalculatorApp")		// Sets the entry point class for the application under analysis
				.addEntryPointMatcher(new MainMethodMatcher("csse374.revengd.examples.fixtures.CalculatorApp"))	// Matches main method of CalculatorApp
				.addExclusions(Arrays.asList("java.*", "javax.*", "sun.*")) 			// Exclude JDK classes from analysis
				.addExclusions(Arrays.asList("soot.*", "polygot.*"))					// Exclude SOOT classes from analysis 
				.addExclusions(Arrays.asList("org.*", "com.*"))						// Exclude other library classes from analysis 
				.build();															// This creates the SOOT's Scene object which has the result of SOOT analysis
		
		System.out.println("==============================================================");
		System.out.println("Application classes loaded by SOOT:");
		scene.getApplicationClasses().forEach(clazz -> {
			System.out.println(clazz.getName() );
		});

		// Let's ask the Scene API for TypeHierarchy
		Hierarchy typeHierarchy = scene.getActiveHierarchy();

		// Now let's query the typeHierarchy for all the implementers of the ICalculator interface
		SootClass iCalculator = scene.getSootClass("csse374.revengd.examples.fixtures.ICalculator");
		Collection<SootClass> implementors = typeHierarchy.getImplementersOf(iCalculator);
		this.prettyPrint("Implementors of ICalculator", implementors);

		// TODO: Can you list all (direct and indirect) supertypes, including 
		// classes and interfaces, of CalculatorC?
		// HINT: This is not as straightforward as it sounds using the Hierarchy API.
		// See if there are methods in the SootClass that can help.
		SootClass calculatorC = scene.getSootClass("csse374.revengd.examples.fixtures.CalculatorC");
		Collection<SootClass> allSuperTypes = new HashSet<>();
		this.computeAllSuperTypes(calculatorC, allSuperTypes);		
		this.prettyPrint("Super types of CalcualtorC using the SootClass API:", allSuperTypes);
	}
	
	void computeAllSuperTypes(final SootClass clazz, final Collection<SootClass> allSuperTypes) {
		if(clazz.getName().equals("java.lang.Object"))
			return;
		
		Collection<SootClass> directSuperTypes = new ArrayList<SootClass>();

		SootClass superClazz = clazz.getSuperclass();
		if(superClazz != null)
			directSuperTypes.add(superClazz);
		
		if(clazz.getInterfaceCount() > 0)
			directSuperTypes.addAll(clazz.getInterfaces());

		directSuperTypes.forEach(aType -> {
			if(!allSuperTypes.contains(aType)) {
				allSuperTypes.add(aType);
				this.computeAllSuperTypes(aType, allSuperTypes);					
			}
		});
	}
	
	
	
	<T> void prettyPrint(String title, Iterable<T> iterable) {
		System.out.println("-------------------------------------------------");
		System.out.println(title);
		System.out.println("-------------------------------------------------");
		iterable.forEach(item -> System.out.println(item));
		System.out.println("-------------------------------------------------");
	}
}
