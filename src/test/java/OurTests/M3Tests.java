package OurTests;

import static org.junit.Assert.*;

import org.junit.Test;

import ourStuff.AnalyzerChain;
import ourStuff.Data;
import ourStuff.Preprocessor;

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

}
