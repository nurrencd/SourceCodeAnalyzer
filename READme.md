This software generates an uml depiction for the java file at the given directory.  It will save the svg file of the uml and will also bring up the svg on completion.

How to use it in command line:

java -jar uml <Directory Path> <flag1> <flag2> <value1> <value2> ...

Examples:
java -jar uml.jar "C:\\Users\\moormaet\\workspace\\CSSE 374\\Lab1-1\\bin" -u -c javax.swing.JComponent -j -r -e headfirst.* -f public

java -jar uml.jar "C:\Users\moormaet\workspace\CSSE 374\term-project\src\main\java" -u -m ourStuff.App -e csse374.* -f private

NOTE:  you must either have the -m flag followed by a main method or a -c followed by a list of classes in order for your request to run


Flags:
-gf [globalFilters...]  --> apply filters to first thing
-f  [filters...]        --> apply filters (rework ideology)
-u                      --> generate UML
-j                      --> include java files
-e [exclusions]         --> exclude file types
-m mainClass            --> sets mainClass as the main class to begin searching
-c [classes...]         --> classes to model in the UML
-r                      --> recursively model all of the interfaces and superclasses of the given system


Contributions:
Eric:
- Written Tests
- Wrote ReadMe
- Added file writting and SVG generation
- Added exclusion flag functionality
- worked on SootClassAnalyzer
- pair programmed for entire implementation of the milestone

Chris:

Abu:
 - Wrote/helped test to check if our code worked in UML
 - Pair programmed with teammates for the whole of milestone 1
 - Worked in CodeGen
 - Helped with file writing
 - Worked with Preprocessor and App class
 - Made the project skeleton (Which was almost completely refactored after design review)

