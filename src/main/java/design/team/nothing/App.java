package design.team.nothing;

import java.nio.file.Paths;

public class App {

	public static void main(String[] args) {
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		
		data.put("path", Paths.get(path));
		analyzerCollection.run(data);
		
	}

}
