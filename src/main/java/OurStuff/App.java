package OurStuff;

public class App {

	public static void main(String[] args) {
		String cmd = args[0];
		String path = args[1];
		Compiler compiler = new BasicUMLCompiler();
		Filter filter = new PublicFilter();
		Parser parser = new Parser(filter, compiler);
		parser.createUML(path);
	}

}
