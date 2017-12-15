This software generates an uml depiction for the java file at the given directory.  It will save the svg file of the uml and will also bring up the svg on completion.

**How to use it in command line:**

java -jar uml <Directory Path> <flag1> <flag2> <value1> <value2> ...

Examples:
java -jar uml.jar "C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin" -u -c javax.swing.JComponent -j -r -e headfirst.* -f public

java -jar uml.jar "C:\Users\moormaet\workspace\CSSE 374\term-project\src\main\java" -u -m ourStuff.App -e csse374.* -f private

NOTE:  you must either have the -m flag followed by a main method or a -c followed by a list of classes in order for your request to run


<h1>Flags:</h1>
- -f  [filters...]        --> apply filters (rework ideology)
- -u                      --> generate UML
- -j                      --> include java files
- -e [exclusions]         --> exclude file types
- -m mainClass            --> sets mainClass as the main class to begin searching
- -c [classes...]         --> classes to model in the UML
- -r                      --> recursively model all of the interfaces and superclasses of the given system

<h1>Extending the project:</h1>
**Creating Analyzers**
<p>Analyzers are the core of the project. The build instantiates a pipeline of Analyzers which run one after another. To create a new Analyzer,
create a new class that implements the Analyzer Interface. The <code>analyze</code> method will be called for each Analyzer in the order they are added.</p>

**Filters**
<p>Each analyzer can have a list of filters that can be applied to all SootClasses, SootMethods, and SootFields processed by the Analyzer. To create a new filter
create a new class that implements the Filter interface. The <code>ignore</code> methods will alert the parent Analyzer which SootObjects should be ignored.</p>

**Data**
<p>The Data structure serves as the platform through which successive Analyzers communicate. Each analyzer will be given the Data object when it is called.
The data object contains:</p>
<ul><li>classes --> collecttion of SootClasses generated with the specified configuration</li>
<li>relationships --> collection of Relationship objects that describes the relationships between SootClasses</li>
<li>config --> Map of configuration flags and their appropriate settings for this particular configuration</li>
<li>scene --> Soot scene created during the analysis of the specified project</li>
<li>path --> Path object to the specified directory</li>
</ul>

**AnalyzerChain**
<p>This object maintains the ordering of analyzers and the passing of the data object. To add an analyzer, call the <code>add</code> method with an Analyzer parameter.
To run the chain, call the <code>run</code> method with a starting Data object.</p>

<h1>Contributions:</h1>
Eric:
- Written Tests
- Wrote ReadMe
- Added file writting and SVG generation
- Added exclusion flag functionality
- worked on SootClassAnalyzer
- pair programmed for entire implementation of the milestone

Chris:
 - Recursive Analyzer
 - Separated SootClassAnalyzer ideology from recursive analyzer ideology
 - Refactored and opimized code in preprocessor
 - designed most of flag system
 - Relationship Analyzer
 - Initial Design refactoring

Abu:
 - Wrote/helped test to check if our code worked in UML
 - Pair programmed with teammates for the whole of milestone 1
 - Worked in CodeGen
 - Helped with file writing
 - Worked with Preprocessor and App class
 - Made the project skeleton (Which was almost completely refactored after design review)

