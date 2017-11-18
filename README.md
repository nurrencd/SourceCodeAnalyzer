# Reverse-Engineered Design (RevEngD)

[![build status](https://ada.csse.rose-hulman.edu/CSSE374-Public/RevEngD/badges/master/build.svg)](https://ada.csse.rose-hulman.edu/CSSE374-Public/RevEngD/commits/master)
[![coverage report](https://ada.csse.rose-hulman.edu/CSSE374-Public/RevEngD/badges/master/coverage.svg)](https://ada.csse.rose-hulman.edu/CSSE374-Public/RevEngD/commits/master)

This is a starter repository for **Software Design - CSSE 374** term project. This application reverse engineers the design of a supplied codebase using [SOOT](https://github.com/Sable/soot), a bytecode instrumentation framework. You will need to learn a few things about SOOT to do a good job in the project. Use these references whenever you get stuck:
1. [SOOT Survival Guide PDF](http://www.brics.dk/SootGuide/sootsurvivorsguide.pdf)
2. [Fundamental Soot Objects](https://github.com/Sable/soot/wiki/Fundamental-Soot-objects)
3. [Other Online Help](https://github.com/Sable/soot/wiki/Getting-help)

## About Seed Contents
The repo, as is, contains four examples for you to try out and expand. Lets' take a quick tour of the repo contents:

1. [SceneBuilder API](/src/main/java/csse374/revengd/soot/SceneBuilder.java) - Helps with setting up SOOT for a whole program analysis.
2. [Examples / Todos](/src/main/java/csse374/revengd/examples/driver)  - There are four examples for you to **review and expand upon** in the [csse374.revengd.examples.driver](/src/main/java/csse374/revengd/examples/driver) package:
  a. **SimpleDirectoryLoading** - Shows how to load Java classes from a directory in SOOT and prints the fields and methods of the classes.
  b. **TypeHierarchy** - Shows how to use the type hierarchy provided by SOOT.
  c. **ControlFlowGraph** - Shows how to get the statements inside a method and search for specific method calls from within the method.
  d. **PointerAnalysis** - Shows how to resolve dynamic dispatch target(s) of a method call statically using pointer analysis. 
  e. **ExamplesDriver** - Implements REPL to drive the above four examples from command prompt.
3. [Fixtures](/src/main/java/csse374/revengd/examples/fixtures) - Contains a sample calculator application for running test and examples.


## Cloning the Repo
You can clone the repo locally using Git Bash/Shell as follows:
```
git clone git@ada.csse.rose-hulman.edu:CSSE374-Public/RevEngD.git
```

## Using IDE
You can import the cloned folder as a Gradle project in Eclipse or IntelliJ IDEs.

## Running the Examples
###Running From Command Line
```
cd <to the RevEngD folder>
./gradlew run
```
It should present a prompt:
```
========================= Your choices ========================= 
1 - E1SimpleDirectoryLoading
2 - E2TypeHierarchy
3 - E3ControlFlowGraph
4 - E4PointerAnalysis
========================== End choices ========================= 
Please enter your choice (number) or press q to quit: 
```
Enter the number corresponding to the example that you want to run, sit back, and enjoy the ride.

###Running From Eclipse/IntelliJ
In your IDE, open the Gradle Tasks view and double-click on the **run** task under **application**. 

