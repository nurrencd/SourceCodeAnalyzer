package OurTests;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import ourStuff.Data;
import ourStuff.Filter;
import ourStuff.PrivateFilter;
import ourStuff.PublicFilter;
import soot.SootClass;

public class PublicFilterTest {

	Data data;

	@Before
	public void setup(){
		data = new Data();
		
		SootClass class1 = new SootClass("Hello", Modifier.PUBLIC);
		data.classes.add(class1);
	}
	
	@Test
	public void testPrivateClass(){
		Filter filter = new PublicFilter();
		Iterator it = data.classes.iterator();
		assertFalse(filter.ignore((SootClass)it.next()));
	}
	

}
