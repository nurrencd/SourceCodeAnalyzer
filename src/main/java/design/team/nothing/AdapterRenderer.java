package design.team.nothing;

public class AdapterRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		return "<< " + patternType + " >> #red";
	}

	@Override
	public String getRelationshipModification() {
		return "#red : adapts";
	}

}
