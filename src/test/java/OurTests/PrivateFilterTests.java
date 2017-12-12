package OurTests;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import ourStuff.Data;
import ourStuff.Filter;
import ourStuff.PrivateFilter;
import soot.SootClass;
import soot.SootMethod;

public class PrivateFilterTests {
	
	Data data;

	@Before
	public void setup(){
		data = new Data();
		
		SootClass class1 = new SootClass("Hello", Modifier.PRIVATE);
		data.classes.add(class1);
	}
	
	@Test
	public void testPrivateClass(){
		Filter filter = new PrivateFilter();
		Iterator it = data.classes.iterator();
		assertFalse(filter.ignore((SootClass)it.next()));
	}

}
