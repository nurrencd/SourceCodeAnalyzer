package design.team.nothing;

public class DecoraterRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		if(patternType.equals("component")){
			return "<< component >> #green";
		}
		return "<< decorater >> #green";
	}

	@Override
	public String getRelationshipModification() {
		
		return ": << decorates >>";
	}

}
