package ourStuff;

import java.util.Collection;

public class App {

	public static void main(String[] args) {
		String cmd = args[0];
		String path = args[1];
		
		Preprocessor pre = new Preprocessor();
		Collection<Analyzer> analyzerCollection = pre.makePileline(args);
		
	}

}
