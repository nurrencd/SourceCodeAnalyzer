package test.team.nothing;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import design.team.nothing.AnalyzerChain;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;

public class Milestone2Tests {

	@Test
	public void testLab4_1(){
		String[] args = new String[] {
				//"C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\374 Workspace\\Lab4-1.zip_expanded\\bin",
				"C:\\Users\\agarwaa\\Desktop\\Everything\\Rose-Hulman\\CSSE\\374\\Lab4-1\\bin",
				//"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab4-1\\bin",
				"-u",
				"-m",
				"headfirst.factory.pizzaaf.PizzaTestDrive",
				"-e",
				"problem.*",
				"windows.*",
				"linux.*",
				"mac.*",
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			//data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testLab2_1() {
		String[] args = new String[] {
//				"C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\Lab2-1\\bin",
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab2-1\\bin",
				"-u",
				"-m",
				"problem.AppLauncherApplication",
				"-e",
				"headfirst.*"
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			//data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testWeather() {
		String[] args = new String[] {
				//"C:\\Users\\agarwaa\\Desktop\\Everything\\Rose-Hulman\\CSSE\\374\\Lab2-1"
				"C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\Lab2-1\\bin",
				"-s",
				"<headfirst.designpatterns.observer.weather.WeatherStation: void main(java.lang.String[])>",
				"-m",
				"headfirst.designpatterns.observer.weather.WeatherStation",
				"-e",
				"problem.*",
				"-d",
				"20"
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			//data.path = Paths.get(path);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testProject() {
		String[] args = new String[] {
				"./src/main/java",
				"-u",
				"-m",
				"design.team.nothing.App",
				"-e",
				"csse374.*"
			};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
//		assertEquals(5,analyzerCollection.size());
			
		//data.path = Paths.get(path);
		analyzerCollection.run(data);
	}
	

}
