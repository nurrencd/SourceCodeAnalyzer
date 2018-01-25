package design.team.nothing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import design.team.nothing.Relationship.RelationshipType;
import edu.rosehulman.jvm.sigevaluator.FieldEvaluator;
import edu.rosehulman.jvm.sigevaluator.GenericType;
import edu.rosehulman.jvm.sigevaluator.MethodEvaluator;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.tagkit.Tag;

public class ClassCodeGenAnalyzer extends AbstractAnalyzer {

	public ClassCodeGenAnalyzer() {
		this.filters = new ArrayList<Filter>();
	}

	@Override
	public Data analyze(Data data) {
		StringBuilder code = new StringBuilder();
		code.append("@startuml\n");
		code.append("skinparam linetype ortho \n");
		Collection<SootClass> classes = data.get("classes", Collection.class);
		for (SootClass c : classes) {
			if (!this.applyFilters(c) || !this.checkClasses(data, c)) {
				code.append(genString(c, data) + "\n");
			}
		}
		
		
		// draw arrows
		code.append(this.addRelationshipArrows(data));
		code.append("@enduml");
//		System.out.println(code.toString());
		try {
			FileCreator fc = new FileCreator();
			fc.getSVG(code.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

	private String genString(SootClass c, Data data) {

		StringBuilder code = new StringBuilder();

		// Classes/Relations have been added
		genClasses(c, data, code);

		// Fields added
		genField(c, code);

		// Methods added
		genMethod(c, code, data);

		code.append("}");
		return code.toString();
	}

	/**
	 * @param c
	 * @param code
	 */
	private void genMethod(SootClass c, StringBuilder code, Data data) {
		Properties prop = data.get("properties", Properties.class);
		
		for (SootMethod m : c.getMethods()) {
			if (prop.containsKey("synthetic")){
				if (prop.getProperty("synthetic").equals("false") && m.getName().contains("$")){
					continue;
				}
			}
			if (m.isPublic()) {
				code.append("  + ");
			} else if (m.isProtected()) {
				code.append("  # ");
			} else {
				code.append("  - ");
			}
			if (m.isStatic()) {
				code.append("{static} ");
			}
			if (m.isAbstract()) {
				code.append("{abstract} ");
			}
			code.append(this.genMethodDeclaration(m));
		}
	}

	/**
	 * @param c
	 * @param code
	 */
	private void genField(SootClass c, StringBuilder code) {
		code.append(" { \n");
		for (SootField f : c.getFields()) {
			if (f.isPublic()) {
				code.append("  + ");
			} else if (f.isProtected()) {
				code.append("  # ");
			} else {
				code.append("  - ");
			}
			if (f.isStatic()) {
				code.append("{static} ");
			}
			Tag t = f.getTag("SignatureTag");
			if (t == null) {
				code.append(f.getType().toQuotedString() + " ");
			} else {
				code.append(new FieldEvaluator(t.toString()).getType().toString() + " ");
			}
			code.append(f.getName() + " \n");
		}
	}

	/**
	 * @param c
	 * @param data
	 * @param code
	 */
	private void genClasses(SootClass c, Data data, StringBuilder code) {
		if (c.isInterface()) {
			code.append("interface ");
		} else if (c.isAbstract()) {
			code.append("abstract ");
		} else {
			code.append("class ");
		}
		code.append(c.getName());
		Collection<SootClass> interfaces = new ArrayList<>();
		Collection<Relationship> rels = data.get("relationships", Collection.class);

		for(Pattern p: data.getPatterns()){
			Collection<String> classKeys = p.getClassKeys();
			for(String key: classKeys){
				//System.out.println(p.getAppliedRelationships(s));
				if(p.getAppliedClasses(key).contains(c)){
					code.append(" " + p.getDeclarationModification());
				}
			}
		}
		
//		if (interfaces.size() > 0) {
//			code.append(" implements ");
//			interfaces.forEach((i) -> {
//				code.append(i.getName() + ", ");
//			});
//			code.deleteCharAt(code.length() - 1);
//			code.deleteCharAt(code.length() - 1);
//		}
	}

	private String genMethodDeclaration(SootMethod m) {
		StringBuilder sb = new StringBuilder();
		Tag t = m.getTag("SignatureTag");
		if (t==null||t.toString().contains("TT;")||t.toString().contains("<*")) {
			sb.append(m.getReturnType().toString() + " ");
			String[] ar = m.getSignature().split(" ");
			sb.append(ar[ar.length - 1]);
			sb.deleteCharAt(sb.length() - 1);
			
		}
		else {
			MethodEvaluator me = new MethodEvaluator(t.toString());
			System.out.println(me.getReturnType());
			sb.append(me.getReturnType().toString() + " ");
			sb.append(m.getName() + "(");
			for (GenericType gt : me.getParameterTypes()) {
				sb.append(gt.toString() + ", ");
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append(")");
		}
		sb.append("\n");
		return sb.toString();
	}

	private boolean filterClass(SootClass c, Data data) {
		// if class gets filtered
		for (Filter filter : this.filters) {
			if (filter.ignore(c)) {
				return false;
			}
		}
		// filter things from soot classes
		Collection<SootMethod> methodsToRemove = new ArrayList<>();
		for (SootMethod m : c.getMethods()) {
			for (Filter filter : this.filters) {
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
			for (Filter filter : this.filters) {
				if (filter.ignore(f)) {
					fieldsToRemove.add(f);
				}
			}
		}
		fieldsToRemove.forEach((f) -> {
			c.removeField(f);
		});
		// done filtering

		return true;
	}

	private String addRelationshipArrows(Data data) {
		StringBuilder sb = new StringBuilder();
		Collection<Relationship> rels = data.get("relationships", Collection.class);
		for (Relationship r : rels) {
			if (this.applyFilters(r.from) && this.applyFilters(r.to) 
					&& (this.checkClasses(data, r.to) || this.checkClasses(data, r.from))){
				continue;
			}
			if (r.type == RelationshipType.DEPENDENCY_ONE_TO_MANY) {
				sb.append(r.from.getName() + " ..> \"*\" " + r.to.getName());
			} else if (r.type == RelationshipType.DEPENDENCY_ONE_TO_ONE) {
				sb.append(r.from.getName() + " ..> \"1\" " + r.to.getName());
			}
			else if (r.type == RelationshipType.ASSOCIATION_ONE_TO_MANY) {
				sb.append(r.from.getName() + " --> \"*\" " + r.to.getName());
			} else if (r.type == RelationshipType.ASSOCIATION_ONE_TO_ONE) {
				sb.append(r.from.getName() + " --> \"1\" " + r.to.getName());
			}else if (r.type == RelationshipType.INHERITANCE) {
				sb.append(r.from.getName() + " --|> " + r.to.getName());
			
			} else if (r.type == RelationshipType.IMPLEMENTATION) {
				sb.append(r.from.getName() + " ..|> " + r.to.getName());
			}
			for(Pattern p: data.getPatterns()){
				Collection<String> relationshipKeys = p.getRelationshipKeys();
				for(String key: relationshipKeys){
					if(p.getAppliedRelationships(key).contains(r)){
						sb.append(" " + p.getRelationshipModification());
					}
				}
			}
			sb.append('\n');
		}
		return sb.toString();
	}
	
	private boolean checkClasses(Data data, SootClass sc){
		return !data.get("properties", Properties.class).getProperty("classlist").contains(sc.getName());
		
	}

}
