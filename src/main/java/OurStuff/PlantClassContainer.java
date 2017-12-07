package OurStuff;

import java.util.ArrayList;
import java.util.Collection;

import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class PlantClassContainer {

	private String className;

	private Collection<SootMethod> methodCollection = new ArrayList<>();
	private Collection<SootField> fieldCollection = new ArrayList<>();
	private Collection<SootClass> parentCollection = new ArrayList<>();
	private Collection<SootClass> interfaceCollection = new ArrayList<>();
	
	public PlantClassContainer(String className){
		this.className = className;
	}
	
	public void addMethod(SootMethod m){
		
	}	
	
	public void addField(SootField f){
		
	}	
	
	public void addClass(SootClass c){
		
	}
	
	public void addInterface(SootClass i){
		
	}
	
	public String getClassName() {
		return className;
	}

	public Collection<SootMethod> getMethodCollection() {
		return methodCollection;
	}

	public Collection<SootField> getFieldCollection() {
		return fieldCollection;
	}

	public Collection<SootClass> getParentCollection() {
		return parentCollection;
	}

	public Collection<SootClass> getInterfaceCollection() {
		return interfaceCollection;
	}

}
