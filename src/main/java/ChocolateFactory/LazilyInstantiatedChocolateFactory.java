package ChocolateFactory;

public class LazilyInstantiatedChocolateFactory {

	private static LazilyInstantiatedChocolateFactory fact;
	
	private LazilyInstantiatedChocolateFactory(){
		
	}
	
	public static LazilyInstantiatedChocolateFactory getInstance(){
		if (fact == null){
			fact = new LazilyInstantiatedChocolateFactory();
		}
		return fact;
	}
	
}
