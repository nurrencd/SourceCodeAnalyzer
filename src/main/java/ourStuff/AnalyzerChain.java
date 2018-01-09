package ourStuff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnalyzerChain {

	private List<AbstractAnalyzer> analyzerCollection;
	
	public AnalyzerChain(){
		analyzerCollection = new ArrayList<>();
	}
	
	public void add(AbstractAnalyzer a){
		this.analyzerCollection.add(a);
	}
	
	public void run(Data data){
		Iterator<AbstractAnalyzer> iterator = analyzerCollection.iterator();
		for (int i = 0; i < analyzerCollection.size(); i++){
			
			data = iterator.next().analyze(data);
		}
	}
	
	public int size(){
		return analyzerCollection.size();
	}
	
}
