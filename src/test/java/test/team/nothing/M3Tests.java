package test.team.nothing;

import static org.junit.Assert.*;

import org.junit.Test;

import design.team.nothing.AnalyzerChain;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;

public class M3Tests {

	@Test
	public void testFrame() {
		String[] args = new String[] {
				"PropertiesFiles/M3Swing.prop"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}
	
	@Test
	public void testSingleton(){
		String[] args = new String[] {
				"PropertiesFiles/M3SingletonRuntime"
			};
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			analyzerCollection.run(data);
	}

}
