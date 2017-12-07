package OurStuff;

public interface Subject {

	void makeFile(String text);

	void addObserver(Observer o);
	
	void removeObserver(Observer o);
	
	void updateObserver();

}
