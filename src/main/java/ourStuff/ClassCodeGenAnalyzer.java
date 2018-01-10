package ourStuff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ourStuff.Relationship.RelationshipType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
public class ClassCodeGenAnalyzer extends AbstractAnalyzer {

	public ClassCodeGenAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		StringBuilder code = new StringBuilder();
		code.append("@startuml\n");
		for(SootClass c : data.classes){
			if(filterClass(c)){
				code.append(genString(c, data) + "\n");
			}
		}
		//draw arrows
		code.append(this.addAssociationArrows(data));
		code.append(this.addDependencyArrows(data));
		code.append("@enduml");
		System.out.println(code.toString());
		try {
			FileCreator fc= new FileCreator();
			fc.getSVG(code.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private String genString(SootClass c, Data data){
		
		StringBuilder code = new StringBuilder();
		if (c.isInterface()) {
			code.append("interface ");
		}
		else if (c.isAbstract()){
			code.append("abstract ");
		}
		else {
			code.append("class ");
		}
		code.append(c.getName());
		Collection<SootClass> interfaces = new ArrayList<>();
		for(Relationship r : data.relationships){
			if(r.from.equals(c)){
				if(r.type == RelationshipType.INHERITANCE){
					code.append(" extends " + r.to.getName());
				}else if(r.type == RelationshipType.IMPLEMENTATION){
					interfaces.add(r.to);
				}
			}
		} 
		if (interfaces.size() > 0){ 
			code.append(" implements ");
			interfaces.forEach( (i) -> {
					code.append(i.getName() + ", ");
			});
			code.deleteCharAt(code.length() - 1);
			code.deleteCharAt(code.length() - 1);
		}
	
		
		//Classes/Relations have been added
		
		code.append(" { \n");
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
		
		//Fields added
		
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
			code.append(this.genMethodDeclaration(m));
		}
		//Methods added
		
		code.append("}");
		return code.toString();
	}
	
	private String genMethodDeclaration(SootMethod m) {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getReturnType().toString() + " ");
		String[] ar = m.getSignature().split(" ");
		sb.append(ar[ar.length-1]);
		sb.deleteCharAt(sb.length()-1);
		sb.append("\n");
		return sb.toString();
	}

	private boolean filterClass(SootClass c) {
		//if class gets filtered
		for (Filter filter : this.filters) {
			if (filter.ignore(c)) {
				return false;
			}
		}
		//filter things from soot classes
		Collection<SootMethod> methodsToRemove = new ArrayList<>();
		for (SootMethod m : c.getMethods()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(m)) {
					methodsToRemove.add(m);
				}
			}
		}
		methodsToRemove.forEach((m) -> {
			c.removeMethod(m);
		});
		
		Collection<SootField> fieldsToRemove = new ArrayList<>();
		for (SootField f : c.getFields()) {
			for (Filter filter :this.filters) {
				if (filter.ignore(f)) {
					fieldsToRemove.add(f);
				}
			}
		}
		fieldsToRemove.forEach((f) -> {
			c.removeField(f);
		});
		//done filtering
		
		
		return true;
	}
	
	private String addDependencyArrows(Data data){
		StringBuilder sb = new StringBuilder();
		for (Relationship r : data.relationships) {
			if (r.type==RelationshipType.DEPENDENCY_ONE_TO_MANY) {
				sb.append(r.from.getName() + " ..> \"*\" " + r.to.getName());
				sb.append('\n');
			}
			else if (r.type == RelationshipType.DEPENDENCY_ONE_TO_ONE) {
				sb.append(r.from.getName() + " ..> " + r.to.getName());
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	private String addAssociationArrows(Data data) {
		StringBuilder sb = new StringBuilder();
		for (Relationship r : data.relationships) {
			if (r.type==RelationshipType.ASSOCIATION_ONE_TO_MANY) {
				sb.append(r.from.getName() + " --> \"*\" " + r.to.getName());
				sb.append('\n');
			}
			else if (r.type == RelationshipType.ASSOCIATION_ONE_TO_ONE) {
				sb.append(r.from.getName() + " --> " + r.to.getName());
				sb.append('\n');
			}
		}
		return sb.toString();
	}

}
