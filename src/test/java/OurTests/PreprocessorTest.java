package OurTests;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ourStuff.Analyzer;
import ourStuff.CodeGenAnalyzer;
import ourStuff.Data;
import ourStuff.Preprocessor;
import ourStuff.RecursiveAnalyzer;
import ourStuff.RelationshipAnalyzer;

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
		Collection<Analyzer> analyzerCollection = pre.makePileline(args, data);
		
		assertEquals(3,analyzerCollection.size());
		
		Iterator<Analyzer> it = analyzerCollection.iterator();
	    assertTrue(it.next() instanceof RecursiveAnalyzer);
	    assertTrue(it.next() instanceof RelationshipAnalyzer);
	    assertTrue(it.next() instanceof CodeGenAnalyzer);
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
		Collection<Analyzer> analyzerCollection = pre.makePileline(args, data);
		
		assertEquals(1,analyzerCollection.size());
		
		Iterator<Analyzer> it = analyzerCollection.iterator();
		assertTrue(it.next() instanceof RecursiveAnalyzer);
	    
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
			Collection<Analyzer> analyzerCollection = pre.makePileline(args, data);
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
			Collection<Analyzer> analyzerCollection = pre.makePileline(args, data);
			fail();
		} catch (Exception e){
			
		}
	}

}
