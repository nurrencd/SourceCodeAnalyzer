package OurStuff;

import java.util.Collection;

import soot.SootClass;

public interface Filter {

	Collection<PlantClassContainer>  filter(Collection<SootClass> classCollection);

}
