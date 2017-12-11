package ourStuff;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;

public class App {

	public static void main(String[] args) {
		String path = args[0];
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		Collection<Analyzer> analyzerCollection = pre.makePileline(args, data);
	
		data.path = Paths.get(path);
		for (int i = 0; i < analyzerCollection.size(); i++){
			Iterator<Analyzer> iterator = analyzerCollection.iterator();
			data = iterator.next().analyze(data);
		}
		
	}

}
