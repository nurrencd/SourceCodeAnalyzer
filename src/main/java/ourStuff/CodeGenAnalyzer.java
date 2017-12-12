package ourStuff;

import java.util.ArrayList;
import java.util.List;

import ourStuff.Relationship.RelationshipType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class CodeGenAnalyzer implements Analyzer {
	private List<Filter> filters;

	public CodeGenAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		StringBuilder code = new StringBuilder();
//		System.out.println("CodeGen drcccrcrcrcrcrrcrcdrcdrcdrcdrcdrcd---------");
		code.append("@startuml \n");
		for(SootClass c : data.classes){
			if(filterClass(c)){
				code.append(genString(c, data) + "\n");
			}
		}
		code.append("@enduml");
		System.out.println(code.toString());
		return data;
	}
	
	private String genString(SootClass c, Data data){
		
		StringBuilder code = new StringBuilder();
		code.append("class " + c.getShortName());
		for(Relationship r : data.relationships){
			if(r.from.equals(c)){
				if(r.type == RelationshipType.INHERITANCE){
					code.append(" extends " + r.to.getShortName());
				}else if(r.type == RelationshipType.IMPLEMENTATION){
					code.append(" implements " + r.to.getShortName());
				}
			}
		} //Relations have been added
		code.append("{ \n");
		for(SootField f : c.getFields()){
			if(f.isPublic()){
				code.append("  + ");
			}else if(f.isProtected()){
				code.append("  # ");
			}else{
				code.append("  - ");
			}
			if(f.isStatic()){
				code.append("{static} ");
			}
			code.append(f.getType().toQuotedString() + " ");
			code.append(f.getName()+ " \n");
		}
		for(SootMethod m : c.getMethods()){
			if(m.isPublic()){
				code.append("  + ");
			}else if(m.isProtected()){
				code.append("  # ");
			}else{
				code.append("  - ");
			}
			if(m.isStatic()){
				code.append("{static} ");
			}
			if(m.isAbstract()){
				code.append("{abstract} ");
			}
			code.append(m.getReturnType().toString() + " ");
			code.append(m.getName() + " \n");
		}
		code.append("}");
		return code.toString();
	}

	private boolean filterClass(SootClass c) {
		//if class gets filtered
		for (Filter filter : this.filters) {
			if (filter.ignore(c)) {
				return false;
			}
		}
		//filter things from soot classes
		for (SootMethod m : c.getMethods()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(m)) {
					c.removeMethod(m);
				}
			}
		}
		for (SootField f : c.getFields()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(f)) {
					c.removeField(f);
				}
			}
		}
		//done filtering
		
		
		return true;
	}

	@Override
	public void addFilter(Filter filter) {
		this.filters.add(filter);
	}

}
