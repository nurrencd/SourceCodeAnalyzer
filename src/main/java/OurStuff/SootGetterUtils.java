package OurStuff;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.util.Chain;

public class SootGetterUtils {
	
	//TODO:  each of these has a trainwreck.  Not sure how to fix them

	public static SootMethod getMethod(SootClass sootClass, int n){
		if (n > sootClass.getMethodCount() || n < 0) {
			return null;
		}
		
		return sootClass.getMethods().get(n);
	}
	
	public static SootField getField(SootClass sootClass, int n){
		if (n > sootClass.getFieldCount() || n < 0){
			return null;
		}
		Chain<SootField> fields = sootClass.getFields();
		SootField field = fields.getFirst();
		while (n > 0){
			field = fields.getSuccOf(field);
			n--;
		}
		return field;
	}
	
	public static SootClass getParent(SootClass sootClass){
		return sootClass.getSuperclass();
	}
	
	public static SootClass getInterface(SootClass sootClass, int n){
		if (n >sootClass.getInterfaceCount() || n < 0) {
			return null;
		}
		Chain<SootClass> interfaces = sootClass.getInterfaces();
		SootClass inter = interfaces.getFirst();
		while (n > 0){
			inter = interfaces.getSuccOf(inter);
			n--;
		}
		return inter;
	}
	
}
