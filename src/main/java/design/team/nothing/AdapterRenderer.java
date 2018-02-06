package design.team.nothing;

public class AdapterRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		// TODO Auto-generated method stub
		
		return "<< " + patternType + " >> #red";
	}

	@Override
	public String getRelationshipModification() {
		// TODO Auto-generated method stub
		return "#red : adapts";
	}

}
