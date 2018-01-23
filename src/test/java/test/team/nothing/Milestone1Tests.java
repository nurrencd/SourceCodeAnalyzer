package test.team.nothing;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import design.team.nothing.AbstractAnalyzer;
import design.team.nothing.AnalyzerChain;
import design.team.nothing.ClassCodeGenAnalyzer;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;
import design.team.nothing.ImplementationAnalyzer;
import design.team.nothing.SootClassAnalyzer;

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
		
		//data.path = Paths.get(path);
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
		
		//data.path = Paths.get(path);
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

		//data.path = Paths.get(path);
		
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		//data.path = Paths.get(path);
		analyzerCollection.run(data);
	}
}
