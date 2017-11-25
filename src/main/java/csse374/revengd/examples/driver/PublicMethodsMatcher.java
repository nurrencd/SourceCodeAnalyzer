package csse374.revengd.examples.driver;

import csse374.revengd.soot.IEntryPointMatcher;
import soot.SootClass;
import soot.SootMethod;

/**
 * Matches the all public methods of the supplied Java class.
 */
public class PublicMethodsMatcher implements IEntryPointMatcher {
	String className;

	public PublicMethodsMatcher(String className) {
		this.className = className;
	}

	@Override
	public boolean match(SootClass clazz) {
		boolean matches = clazz.getName().equals(this.className);
		return matches;
	}

	@Override
	public boolean match(SootMethod method) {
		return method.isPublic();
	}
}
