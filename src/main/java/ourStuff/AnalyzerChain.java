package ourStuff;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnalyzerChain {

	private List<Analyzer> analyzerCollection;
	
	public AnalyzerChain(){
		analyzerCollection = new ArrayList<>();
	}
	
	public void add(Analyzer a){
		this.analyzerCollection.add(a);
	}
	
	public void run(Data data){
		Iterator<Analyzer> iterator = analyzerCollection.iterator();
		for (int i = 0; i < analyzerCollection.size(); i++){
			
			data = iterator.next().analyze(data);
		}
	}
	
	public int size(){
		return analyzerCollection.size();
	}
	
}
