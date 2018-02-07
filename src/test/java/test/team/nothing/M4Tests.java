package test.team.nothing;

import static org.junit.Assert.*;

import org.junit.Test;

import design.team.nothing.AnalyzerChain;
import design.team.nothing.Data;
import design.team.nothing.Preprocessor;

public class M4Tests {

	@Test
	public void DITest() {
		String[] args = new String[] { "-config", "PropertiesFiles/DipendencyProp" };
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}

	@Test
	public void DecoratorTest() {
		String[] args = new String[] { "-config", "PropertiesFiles/M4DecoratorLab" };
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}
	
	@Test
	public void AdapterTest() {
		String[] args = new String[] { "-config", "PropertiesFiles/M4Adapter" };
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}
	
	@Test
	public void DecoratorAdapterTest() {
		String[] args = new String[] { "-config", "PropertiesFiles/AdapterDecoratorMerge" };
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}

	@Test
	public void BadDecoratorTest() {
		String[] args = new String[] { "-config", "PropertiesFiles/M4BadDecorator" };
		Preprocessor pre = new Preprocessor();
		Data data = new Data();
		AnalyzerChain analyzerCollection = pre.makePileline(args, data);
		analyzerCollection.run(data);
	}
	
}
