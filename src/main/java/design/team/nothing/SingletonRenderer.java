package design.team.nothing;

public class SingletonRenderer implements PatternRenderer{

	@Override
	public String getClassModification() {
		return "<<Singleton>> ##blue";
	}

	@Override
	public String getRelationshipModification() {
		return "";
	}

}
