This software generates a UML or Sequence Diagram depiction for the java file at the given directory.  It will save the svg file of the UML and will also bring up the svg on completion.

Graphviz, PlantUML and Soot are required for this project. Find how to install Graphviz below. Soot and PlantUML will automatically be installed when the project is installed.

**How to install Graphviz on Windows**

Go to: https://graphviz.gitlab.io/_pages/Download/Download_windows.html

Download the latest stable release of Graphviz.msi (note download the msi and not the zip for easy install)

UML View of the project <a href=http://www.plantuml.com/plantuml/svg/pLVRZjem47tFLupKbw32VW6A5h7TgfULqF9OzS4a0rZhsAbjK2dYt-lu4fm02wZT5Iy9jiSpPyxSmhtIHEfLoPAS4QLWL5MmXGJW8wm5BO1ogjEUNM-DgARvpXsKXF9qgYNbYny_WSY5wYMxX7ADSavoX14dRFC7PT2s9JEb9Sdr3Xw89a3SUMeNXNdqkcBM32cAHygCqjIzO6vVjQqaY8A7xfUDPLfM34laMaLeZ9K1VATA3vt2p6jKLy5y7Hl01vW9mP1m85N5DkvSfLCXzDY2pSz8FQ5U2iFo6R57Ygo0kQN3i-0z7IzDW1pvtjzmaWMdv5WmXhccWX-FUF0oRUWBDwDpkU9TTamMWAA6jHgNWsMz7ei2FoDljE5lZRmu4haJ6NWw8WDmgGWBvEvNp7KVJWexdX_Ge9IaPgB5ldsvAo44BiPMSJkSmLDhoZkm9tZOo6OkNL4TY3mZin3KabOPo6ZbPPz8DNJG-zu5eqpmELsuyscE747PfzsO4Bs4gduqy4psu5WYqK9kNudw0dKjwIiSj0bLkxvzoXzMTo2ABhYGs4tvSw9ntf-JZPBV-VRruPk75PL-6sZNsNDepqAy1DT4OYL5Zae96KeoZcMx4vcJurZRfLsI5voO437AST-8NJeS9Fpfp37rsBrzjCNvH7Z1JS0RjUBW9bqJZJSQ0g5DcM5nc-XMCqRpcuJsZFbAAhh6_zwrznRsZIxesFkYHNTdBaNfzZxnLGbwKs5aU5XRpj9OGRzzjzzSssCOcTBCgOLnYP_NZwUJRTuqsdZZr531oc11dkER3Cz_HFU5Br5IJTu1NZH6ByGMgkbzWdirlcRppUfhYhzMTUPS_lLrDG4NV1mbzJ-BmI1h3SEaDHdlxhBsZ4lQ4w--7NQQ8tkhXfDOJBLCpFgX1IjxQBT3GdB0IgtPzUFatl1c_ZBz1G00>here</a>

**How to export file**

Simply export the program as a Runnable Jar File
    Set Launch Configuration to App (the main file, in \src\main\java\ourStuff)
    Set Export Destination to your desired destination
    
**How to use it in command line:**

java -jar uml <Directory Path> <flag1> <value1> <flag2>  <value2> ...
Not all filters have values;

Examples:
<h3> Creating UML </h3>

java -jar uml.jar "C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin" -u -c javax.swing.JComponent -j -r -e headfirst.* -f public

java -jar uml.jar "C:\Users\moormaet\workspace\CSSE 374\term-project\src\main\java" -u -m ourStuff.App -e csse374.* -f private

NOTE:  you must either have the -m flag followed by a main method or a -c followed by a list of classes in order for your request to run

<h3> Creating Sequence Diagrams </h3>

java -jar uml.jar "C:\\Users\\nurrencd\\Documents\\1-Rose-Hulman\\CSSE\\374\\Lab2-1\\bin" -s "<problem.AppLauncherApplication: void main(java.lang.String[])>" -m "problem.AppLauncherApplication" -e "headfirst.*" -d "20"



<h1>Flags:</h1>
- -u                      --> generate UML
- -s                      --> generate sequence diagram
- -f  [filters...]        --> apply filters
- -j                      --> include java files
- -e [exclusions]         --> exclude file types
- -m mainClass            --> sets mainClass as the main class to begin searching
- -c [classes...]         --> classes to model in the UML
- -r                      --> recursively model all of the interfaces and superclasses of the given system
- -d                      --> set maximum call depth for sequence diagram

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

Abu:
 - Wrote/helped test to check if our code worked in UML + Sequence Generator
 - Worked in CodeGen
 - Helped with file writing
 - Worked with Preprocessor and App class
 - Sequence Generator
 - ClassCodeGenAnalyzer
 - Readme
 - Made the project skeleton (Which was almost completely refactored after design review)

