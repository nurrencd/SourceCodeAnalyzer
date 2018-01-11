package OurTests;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import ourStuff.AnalyzerChain;
import ourStuff.Data;
import ourStuff.Preprocessor;

public class SeqDiagramTest {

	@Test
	public void test() {
		String[] args = new String[] {
				"C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin",
				"-s",
				"problem.AppLauncher",
				"-m",
				"problem.AppLauncher",
				"-e",
				"headfirst.*"
			};
			String path = args[0];
			Preprocessor pre = new Preprocessor();
			Data data = new Data();
			
			AnalyzerChain analyzerCollection = pre.makePileline(args, data);
			
			data.path = Paths.get(path);
			analyzerCollection.run(data);
	}

}
