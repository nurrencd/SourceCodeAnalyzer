package OurTests;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import ourStuff.Data;
import ourStuff.Filter;
import ourStuff.PrivateFilter;
import ourStuff.ProtectedFilter;
import soot.SootClass;

public class ProtectedFilterTest {

	Data data;

	@Before
	public void setup(){
		data = new Data();
		
		SootClass class1 = new SootClass("Hello", Modifier.PROTECTED);
		data.classes.add(class1);
	}
	
	@Test
	public void testProtectedClass(){
		Filter filter = new ProtectedFilter();
		Iterator it = data.classes.iterator();
		assertFalse(filter.ignore((SootClass)it.next()));
	}
}
