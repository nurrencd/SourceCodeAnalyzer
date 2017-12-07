package OurStuff;

import java.util.ArrayList;
import java.util.Collection;

import soot.SootClass;

public class Parser {

	Filter filter;
	Compiler compiler;
	Collection<SootClass> classCollection = new ArrayList<SootClass>();
	
	public Parser(Filter filter, Compiler compiler) {
		this.filter = filter;
		this.compiler = compiler;
	}

	public void createUML(String path) {
		classCollection = ClassGen.generateClasses(path);
		String text = this.compiler.compile(this.filter, classCollection);
		Subject system = new FileSystem();
		system.makeFile(text);
	}
	
	public void setCompiler(Compiler p){
		
	}
	
	public void setFilter(Filter f){
		
	}

}
