package design.team.nothing;

public class DependencyInversionRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		// TODO Auto-generated method stub
		if (patternType.equals("dependency")) {
			return "#purple";
		}
		return "#green";
	}

	@Override
	public String getRelationshipModification() {
		// TODO Auto-generated method stub
		return "#purple";
	}

}
