package ChocolateFactory;

public class EagerlyInstantiatedChocolateFactory {

	private static EagerlyInstantiatedChocolateFactory fact = new EagerlyInstantiatedChocolateFactory();
	
	private EagerlyInstantiatedChocolateFactory(){
		
	}
	
	public static EagerlyInstantiatedChocolateFactory getInstance(){
		
		return fact;
	}
	
}
