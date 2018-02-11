package design.team.nothing;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import soot.SootClass;
import soot.SootMethod;

public class DecoratorRenderer extends PatternRenderer{

	List<SootClass> badDec;
	
	public DecoratorRenderer(List<SootClass> bad) {
		this.badDec = bad;
	}

	@Override
	public String getClassModification(String patternType) {
		for (SootClass c : badDec) {
			if (c.getName().equals(this.currrent.getName())) {
				return "<< bad decorator >> #green";
			}
		}
		if(patternType.equals("component")){
			return "<< component >> #green";
		}
		return "<< decorator >> #green";
	}

	@Override
	public String getRelationshipModification() {
		return "#green : << decorates >>";
	}
	
	@Override
	public String generateClassCode(SootClass c, String patternType, Properties prop) {
		if(patternType.equals("baddecorator")){
			return "";
			
		}
		return super.generateClassCode(c, patternType, prop);
	}
	
	@Override
	public String generateExtraSignatures(SootClass c) {
		//Collection<SootClass> collectionSC = pattern.getAppliedClasses("baddecorator");
		
		StringBuilder sb = new StringBuilder();
		for(SootClass sc : this.badDec){
			if(sc.getName().equals(c.getName())){
				for(SootMethod sm: sc.getMethods()){	
					String methodSig = this.generateMethodSignature(sm);
					methodSig= methodSig.substring(0, 4) + "<font color = #FF0000> " + methodSig.substring(4) + " </font> \n";
					sb.append(methodSig);
				}
			}
		}
		return sb.toString();
	}

}
