package test.team.nothing;

import org.junit.Test;

import design.team.nothing.AnalyzerChain;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;

public class Integration {

	
	@Test
	public void systemTest() {
		String[] args = new String[] {
			"-config",
			"PropertiesFiles/IntegrationProperties"
		};
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}

}
