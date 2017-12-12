package OurTests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ourStuff.Analyzer;
import ourStuff.CodeGenAnalyzer;
import ourStuff.Data;
import ourStuff.Preprocessor;
import ourStuff.RecursiveAnalyzer;
import ourStuff.RelationshipAnalyzer;

public class Integration {

	@Test
	public void systemTest() {
		String[] args = new String[] {
			"C:/Users/agarwaa/Desktop/Everything/Rose-Hulman/CSSE/374/New folder/term-project/src/main/java",
			"-u",
			"-m",
			"ourStuff.App",
			"-f",
			"private",
			"-j",
			"-e",
			"csse374.*"
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
		
		data.path = Paths.get(path);
		Iterator<Analyzer> iterator = analyzerCollection.iterator();
		for (int i = 0; i < analyzerCollection.size(); i++){
			
			data = iterator.next().analyze(data);
			System.out.println(i);
		}
		
	}

}
