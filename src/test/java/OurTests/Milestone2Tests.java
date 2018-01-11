package OurTests;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import ourStuff.AnalyzerChain;
import ourStuff.Data;
import ourStuff.Preprocessor;

public class Milestone2Tests {

	@Test
	public void testLab4(){
		String[] args = new String[] {
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab4-1\\bin",
				"-u",
				"-m",
				"problem.SimpleGuiApp",
				"-e",
				"headfirst.*"
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testLab2_1() {
		String[] args = new String[] {
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab2-1\\bin",
				"-u",
				"-m",
				"problem.AppLauncherApplication",
				"-e",
				"headfirst.*",
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testWeather() {
		String[] args = new String[] {
				"C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\Lab2-1\\bin",
				"-s",
				"<problem.AppLauncherApplication: void main(java.lang.String[])>",
				"-m",
				"problem.AppLauncherApplication",
				"-e",
				"headfirst.*",
				"-d",
				"20"
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testProject() {
		String[] args = new String[] {
				"./src/main/java",
				"-s",
				"<ourStuff.App: void main(java.lang.String[])>",
				"-m",
				"ourStuff.App",
				"-d",
				"20",
				"-e",
				"csse374.*"
			};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
//		assertEquals(5,analyzerCollection.size());
			
		data.path = Paths.get(path);
		analyzerCollection.run(data);
	}
	

}
