This software generates a UML or Sequence Diagram depiction for the java file at the given directory.  It will save the svg file of the UML and will also bring up the svg on completion.

Graphviz, PlantUML and Soot are required for this project. Find how to install Graphviz below. Soot and PlantUML will automatically be installed when the project is installed.

**How to install Graphviz on Windows**

Go to: https://graphviz.gitlab.io/_pages/Download/Download_windows.html

Download the latest stable release of Graphviz.msi (note download the msi and not the zip for easy install)

UML View of the project <a href=https://tinyurl.com/yay3vybu>here</a>

**How to export file**

Simply export the program as a Runnable Jar File
    Set Launch Configuration to App (the main file, in \src\main\java\ourStuff)
    Set Export Destination to your desired destination
    
**How to use it in command line:**

java -jar uml <Directory Path> <flag1> <value1> <flag2>  <value2> ...
Not all filters have values;

Examples:
<h3> Creating UML </h3>

java -jar uml.jar "C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin" -uml -classlist javax.swing.JComponent -java -recursive -exclude headfirst.* -filter public

java -jar uml.jar "C:\Users\moormaet\workspace\CSSE 374\term-project\src\main\java" -uml -main ourStuff.App -exclude csse374.* -filter private

NOTE:  you must either have the -main flag followed by a main method or a -classlist followed by a list of classes in order for your request to run

<h3> Creating Sequence Diagrams </h3>

java -jar uml.jar "C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\Lab2-1\\bin" -sequence "<problem.AppLauncherApplication: void main(java.lang.String[])>" -mainmethod "problem.AppLauncherApplication" -exclude"headfirst.*" -depth "20"



<h1>Flags:</h1>
- -uml                      --> generate UML
- -sequence                 --> generate sequence diagram
- -filters                  --> apply filters
- -java                     --> include java files
- -exclude                  --> exclude file types
- -main                     --> sets mainClass as the main class to begin searching
- -classlist                --> classes to model in the UML
- -recursive                --> recursively model all of the interfaces and superclasses of the given system
- -depth                    --> set maximum call depth for sequence diagram
- -pattern                  --> set which pattern to detect
- -resolutionstrategy       --> set which resolution strategy to use for the given <code>Algorithms</code>
- -path                     --> set the path of the file
- -algorithms               --> Abstract Method Resolvers for sequence diagram. Include whole package name to load.
- -synthetic                --> Adds the Synthetic function filter

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
- worked on SequenceDiagramAnalyzer
- worked on Dependency and Association Analyzers
- made CustomCollection

Chris:
 - Recursive Analyzer
 - Separated SootClassAnalyzer ideology from recursive analyzer ideology
 - Refactored and opimized code in preprocessor
 - designed most of flag system
 - Relationship Analyzer
 - Initial Design refactoring
 - One design / implementation of SequenceDiagramAnalyzer
 - Association analyzer
 - Data relationship maintenance
 - Data class, providing extensibility
 - Created Pattern and Algorithm infrastructure
 - Pattern logic and SingletonPatternAnalyzer
 - Applying pattern logic to UML Generator
 - Restructuring part of the SequenceDiagramAnalyzer to properly handle Algorithm's resolutions

Abu:
 - Wrote/helped test to check if our code worked in UML + Sequence Generator
 - Algorithm/CallGraphAlgorithm/HierarchyAlgorithm/AggregateAlgorithm
 - ChainResolutionStrategy/IntersectionResolutionStrategy/SingleResolutionStrategy
 - InheritanceOverCompositionAnalyzer/Pattern
 - Pattern/AbstractPattern
 - Worked in CodeGen
 - Helped with file writing
 - Worked with Preprocessor and App class
 - Sequence Generator/SequenceDiagramAnalyzer
 - ClassCodeGenAnalyzer
 - Readme
 - Made the project skeleton (Which was almost completely refactored after design review)

