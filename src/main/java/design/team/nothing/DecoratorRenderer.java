package design.team.nothing;

public class DecoratorRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		if(patternType.equals("component")){
			System.out.println("Found a component.");
			return "<< component >> #green";
		}
		return "<< decorator >> #green";
	}

	@Override
	public String getRelationshipModification() {
		
		return "#green : << decorates >>";
	}

}
