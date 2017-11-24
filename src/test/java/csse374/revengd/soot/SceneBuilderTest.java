package csse374.revengd.soot;

import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import csse374.revengd.soot.SceneBuilder;
import soot.Scene;

public class SceneBuilderTest {
	SceneBuilder builder;
	List<String> allFixtureClasses;
	List<String> allRechableClassesFromEntry;
	List<String> allUnRechableClassesFromEntry;

	@Before
	public void setUp() throws Exception {
		this.builder = SceneBuilder.create();
		
		this.allRechableClassesFromEntry = Arrays.asList(
				"csse374.revengd.examples.fixtures.CalculatorApp",
				"csse374.revengd.examples.fixtures.CalculatorA",
				"csse374.revengd.examples.fixtures.CalculatorB",
				"csse374.revengd.examples.fixtures.ICalculator"
		);
		
		this.allUnRechableClassesFromEntry = Arrays.asList(
				"csse374.revengd.examples.fixtures.CalculatorC",
				"csse374.revengd.examples.fixtures.UnrelatedClass"				
		);
		
		this.allFixtureClasses = new ArrayList<>();
		this.allFixtureClasses.addAll(this.allRechableClassesFromEntry);
		this.allFixtureClasses.addAll(this.allUnRechableClassesFromEntry);

		// Needed for running tests from Gradle
		Path buildResourcesTestPath = Paths.get("build", "resources", "test");
		if(!buildResourcesTestPath.toFile().exists()) {
			buildResourcesTestPath.toFile().mkdirs();
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreate() {
		assertNotNull(builder);
	}

	@Test
	public void testAddClassPath() {
		List<String> classPaths = Arrays.asList("abc", "def");		
		classPaths.forEach(path -> builder.addClassPath(path));
		
		assertEquals(classPaths, builder.classPaths);
	}

	@Test
	public void testAddClassPaths() {
		List<String> classPaths = Arrays.asList("abc", "def");		
		builder.addClassPaths(classPaths);
		
		assertEquals(classPaths, builder.classPaths);
	}

	@Test
	public void testAddDirectory() {
		List<String> dirs = Arrays.asList("abc", "def");		
		dirs.forEach(dir -> builder.addDirectory(dir));
		
		assertEquals(dirs, builder.dirsToProcess);
	}

	@Test
	public void testAddDirectories() {
		List<String> dirs = Arrays.asList("abc", "def");		
		builder.addDirectories(dirs);
		
		assertEquals(dirs, builder.dirsToProcess);
	}

	@Test
	public void testSetEntryClass() {
		String entryClass = "package.Class";

		builder.setEntryClass(entryClass);
		assertEquals(entryClass, builder.entryClassToLoad);
	}

	@Test
	public void testAddEnterPointMatcher() {
		List<IEntryPointMatcher> matchers = Arrays.asList(mock(IEntryPointMatcher.class), mock(IEntryPointMatcher.class));
		matchers.forEach(matcher -> builder.addEntryPointMatcher(matcher));
		
		assertEquals(matchers, builder.matchers);
	}

	@Test
	public void testAddEnterPointMatchers() {
		List<IEntryPointMatcher> matchers = Arrays.asList(mock(IEntryPointMatcher.class), mock(IEntryPointMatcher.class));
		builder.addEntryPointMatchers(matchers);
		
		assertEquals(matchers, builder.matchers);
	}
	
	@Test
	public void testAddExclusion() {
		List<String> exclusions = Arrays.asList("abc.*", "def.abc.*");
		exclusions.forEach(exclusion -> builder.addExclusion(exclusion));
		
		assertEquals(exclusions, builder.exclusions);
	}

	@Test
	public void testAddExclusions() {
		List<String> exclusions = Arrays.asList("abc.*", "def.abc.*");
		builder.addExclusions(exclusions);
		
		assertEquals(exclusions, builder.exclusions);
	}
	
	@Test
	public void testShouldLoadClassesOnlyRechableFromMain() {
		final Scene scene = SceneBuilder.create()
				.addExclusions(Arrays.asList("sun.*", "soot.*", "polygot.*"))
				.setEntryClass("csse374.revengd.examples.fixtures.CalculatorApp")
				.addEntryPointMatcher(new MainMethodMatcher("csse374.revengd.examples.fixtures.CalculatorApp"))
				.build();

		this.allRechableClassesFromEntry.forEach(clazz -> {
			assertTrue(scene.containsClass(clazz));
		});

		this.allUnRechableClassesFromEntry.forEach(clazz -> {
			assertFalse(scene.containsClass(clazz));
		});
	}

	@Test
	public void testProcessDirShouldAnalyzeAllClassesInDir() {
		Path path = Paths.get("build", "classes", "main");
		if(!path.toFile().exists()) {
			path = Paths.get("bin");
			
			if(!path.toFile().exists()) {
				fail("Could not find the classes directory - [" + path.toAbsolutePath().toString() + "]");
			}
		}
		
		String dirToProcess = path.toAbsolutePath().toString();
		System.out.println("Processing test analysis from: " + dirToProcess);
		
		final Scene scene = SceneBuilder.create()
				.addExclusions(Arrays.asList("sun.*", "soot.*", "polygot.*"))
				.setEntryClass("csse374.revengd.examples.fixtures.CalculatorApp")
				.addEntryPointMatcher(new MainMethodMatcher("csse374.revengd.examples.fixtures.CalculatorApp"))
				.addDirectory(path.toAbsolutePath().toString())
				.build();

		
		this.allFixtureClasses.forEach(clazz -> {
			assertTrue(scene.containsClass(clazz));
		});
	}
}
