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
import soot.SootClass;
import design.team.nothing.ImplementationAnalyzer;

public class Integration {

	
	@Test
	public void systemTest() {
		String[] args = new String[] {
			"PropertiesFiles/IntegrationProperties"
		};
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}

}
