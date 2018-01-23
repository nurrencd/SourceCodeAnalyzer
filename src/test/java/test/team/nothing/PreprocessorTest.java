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
import design.team.nothing.SootClassAnalyzer;
import design.team.nothing.ImplementationAnalyzer;

public class PreprocessorTest {

	@Test
	public void testWithU() {
		String[] args = new String[] {
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/design.team.nothing",
			"-u",
			"-m",
			"design.team.nothing.App",
			"-f",
			"private"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		assertEquals(3,analyzerCollection.size());
		
	
	}
	
	@Test
	public void testWithoutU() {
		String[] args = new String[] {
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/design.team.nothing",
			"-m",
			"design.team.nothing.App",
			"-f",
			"private"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		assertEquals(1,analyzerCollection.size());
	    
	}
	
	@Test
	public void testNoMain() {
		String[] args = new String[] {
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/design.team.nothing",
			"-u",
			"-f",
			"private"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		try {
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
			fail();
		} catch (Exception e){
			
		}
	}
	
	@Test
	public void testNoMainPath() {
		String[] args = new String[] {
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/design.team.nothing",
			"-u",
			"-m",
			"-f",
			"private"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		try {
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
			fail();
		} catch (Exception e){
			
		}
	}

}
