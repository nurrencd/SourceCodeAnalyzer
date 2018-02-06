package design.team.nothing;

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

public abstract class PatternRenderer {
	
	protected String patternType;
	protected Pattern pattern;
	
	public String generateClassCode(SootClass c, String patternType, Properties prop, Pattern pattern) {
		this.pattern = pattern;
		this.patternType = patternType;
		StringBuilder code = new StringBuilder();
		code.append(generateClassSignature(c) + " ");
		code.append(getClassModification(patternType));
		code.append(" {\n");
		for (SootField f : c.getFields()) {
			code.append(generateFieldSignature(f));
			code.append("\n");
		}
		for (SootMethod m : c.getMethods()) {
			if (prop.containsKey("synthetic")){
				if (prop.getProperty("synthetic").equals("false") && m.getName().contains("$")){
					continue;
				}
			}
			code.append(generateMethodSignature(m));
			code.append("\n");
		}
		code.append(generateExtraSignatures(c));
		code.append("\n}\n");
		code.append(generateNotes(c));
		return code.toString();
	}
	
	private String generateNotes(SootClass c) {
		return "";
	}

	public String generateRelationshipCode(Relationship r) {
		StringBuilder sb = new StringBuilder();
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
		sb.append(" " + this.getRelationshipModification());
		sb.append('\n');
		return sb.toString();
	}
	
	public String generateClassSignature(SootClass c) {
		StringBuilder code = new StringBuilder();
		if (c.isInterface()) {
			code.append("interface ");
		} else if (c.isAbstract()) {
			code.append("abstract ");
		} else {
			code.append("class ");
		}
		code.append(c.getName());
		return code.toString();
	}
	
	public String generateMethodSignature(SootMethod m) {
		StringBuilder sb = new StringBuilder();
		
		if (m.isPublic()) {
			sb.append("  + ");
		} else if (m.isProtected()) {
			sb.append("  # ");
		} else {
			sb.append("  - ");
		}
		if (m.isStatic()) {
			sb.append("{static} ");
		}
		if (m.isAbstract()) {
			sb.append("{abstract} ");
		}
		Tag t = m.getTag("SignatureTag");
		if (t==null||t.toString().contains("TK;")||t.toString().contains("<T") || t.toString().contains("TT;")
				|| t.toString().contains("(T")) {
			sb.append(m.getReturnType().toString() + " ");
			String[] ar = m.getSignature().split(" ");
			sb.append(ar[ar.length - 1]);
			sb.deleteCharAt(sb.length() - 1);
		}
		else {
			MethodEvaluator me = new MethodEvaluator(t.toString());
			try {
				sb.append(me.getReturnType().toString() + " ");
			}catch (Exception e) {
				sb.append("T ");
			}
			sb.append(m.getName() + "(");
			try {
				for (GenericType gt : me.getParameterTypes()) {
					sb.append(gt.toString() + ", ");
				}
			} catch(Exception e) {
				sb.append("T, ");
			}
			sb.delete(sb.length()-2, sb.length());
			sb.append(")");
		}
		return sb.toString();
	}
	
	public String generateFieldSignature(SootField f) {
		StringBuilder code = new StringBuilder();
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
		if (t == null || t.toString().contains("TV") || t.toString().contains("TT;")) {
			code.append(f.getType().toQuotedString() + " ");
		} else {
			try {
			code.append(new FieldEvaluator(t.toString()).getType().toString() + " ");
			} catch (Exception e) {
				code.append("T ");
			}
		}
		code.append(f.getName());
		return code.toString();
	}
	
	public String generateExtraSignatures(SootClass c) {
		return "";
	}
	
	public abstract String getClassModification(String patternType);

	public abstract String getRelationshipModification();
}
