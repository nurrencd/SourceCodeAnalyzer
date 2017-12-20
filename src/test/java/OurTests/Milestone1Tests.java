package OurTests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ourStuff.Analyzer;
import ourStuff.AnalyzerChain;
import ourStuff.ClassCodeGenAnalyzer;
import ourStuff.Data;
import ourStuff.Preprocessor;
import ourStuff.ImplementationAnalyzer;
import ourStuff.SootClassAnalyzer;

public class Milestone1Tests {

	@Test
	public void testString() {
		String[] args = new String[] {
			"./src/main/java",
			"-u",
			"-r",
			"-j",
			"-c",
			"java.lang.String",
			"-e",
			"csse374.*"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		data.path = Paths.get(path);
		analyzerCollection.run(data);
		
	}
	
	@Test
	public void testTeamatesCode() {
		String[] args = new String[] {
			"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin",
			"-u",
			"-m",
			"problem.DataStandardizerApp",
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
	public void testJComponent() {
		String[] args = new String[] {
			"./src/main/java",
			"-u",
			"-r",
			"-j",
			"-f",
			"public",
			"-c",
			"javax.swing.JComponent",
			"-e",
			"csse374.*"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();

		data.path = Paths.get(path);
		
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		data.path = Paths.get(path);
		analyzerCollection.run(data);
	}
}
