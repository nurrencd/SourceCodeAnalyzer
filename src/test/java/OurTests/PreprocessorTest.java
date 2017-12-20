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
import ourStuff.SootClassAnalyzer;
import ourStuff.ImplementationAnalyzer;

public class PreprocessorTest {

	@Test
	public void testWithU() {
		String[] args = new String[] {
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/ourStuff",
			"-u",
			"-m",
			"OurStuff.App",
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
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/ourStuff",
			"-m",
			"OurStuff.App",
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
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/ourStuff",
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
			"C:/Users/moormaet/Documents/GitHub/term-project/src/main/java/ourStuff",
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
