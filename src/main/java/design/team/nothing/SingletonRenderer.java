package design.team.nothing;

public class SingletonRenderer extends PatternRenderer{

	@Override
	public String getClassModification(String patternType) {
		return "<<Singleton>> ##blue";
	}

	@Override
	public String getRelationshipModification() {
		return "";
	}


}
