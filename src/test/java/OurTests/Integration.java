package OurTests;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import ourStuff.Analyzer;
import ourStuff.AnalyzerChain;
import ourStuff.CodeGenAnalyzer;
import ourStuff.Data;
import ourStuff.Preprocessor;
import ourStuff.SootClassAnalyzer;
import soot.SootClass;
import ourStuff.RelationshipAnalyzer;

public class Integration {

	@Test
	public void systemTest() {
		String[] args = new String[] {
			"./src/main/java",
			"-u",
			"-m",
			"ourStuff.App",
			"-f",
			"public",
			"-e",
			"csse374.*"
		};
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		assertEquals(3,analyzerCollection.size());
		
		data.path = Paths.get(path);
		analyzerCollection.run(data);
	}

}
