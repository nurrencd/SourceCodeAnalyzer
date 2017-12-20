package ourStuff;

import java.util.Iterator;

import soot.SootClass;
import soot.SootField;
import soot.util.Chain;

public class AssociationAnalyzer implements Analyzer{

	@Override
	public Data analyze(Data data) {
		Iterator<SootClass> it = data.scene.getClasses().iterator();
		while(it.hasNext()){
			Chain<SootField> chField = it.next().getFields();
			for(SootField sf :  chField){
			}
		}
		return null;
	}

	@Override
	public void addFilter(Filter filter) {
		// TODO Auto-generated method stub
		
	}

}
