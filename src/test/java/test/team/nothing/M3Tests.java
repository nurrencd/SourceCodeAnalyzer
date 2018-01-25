package test.team.nothing;

import org.junit.Test;

import design.team.nothing.AnalyzerChain;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;

public class M3Tests {

	@Test
	public void testFrame() {
		String[] args = new String[] {
				"-config",
				"PropertiesFiles/M3Swing.prop"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testExternalPattern() {
		String[] args = new String[] {
				"-config",
				"C:\\Users\\moormaet\\Documents\\GitHub\\bidirectional-detector\\ExternalTestProp"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testSingleton(){
		String[] args = new String[] {
				"-config",
				"PropertiesFiles/M3SingletonRuntime"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testChain(){
		String[] args = new String[] {
				"-config",
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\PatternDetectorExample2\\PropertyFolders\\ChainFolder"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testIntersection(){
		String[] args = new String[] {
				"-config",
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\PatternDetectorExample2\\PropertyFolders\\Intersection"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testUnion(){
		String[] args = new String[] {
				"-config",
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\PatternDetectorExample2\\PropertyFolders\\Union"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	

	@Test
	public void testWeather(){
		String[] args = new String[] {
				"-config",
				"PropertiesFiles/WeatherProp"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
}
