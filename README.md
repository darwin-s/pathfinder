#Pathfinder

"Pathfinder" is a small demo app made in Java, using Swing which
shows an example implementation of a pathfinding algorithm based on
BFS.

#Build
The project uses Maven.
To compile the demo run
```shell
mvn compile
```
To package it in a jar file run
```shell
mvn package
```
The resulting files will be located in a directory named "target"
inside the project root.

#Usage
The app can be downloaded from the "releases" sections on GitHub.
It can be downloaded as an .exe or .jar file. To run the jar file 
from the terminal use
```shell
java -jar Pathfinder.jar
```

##Controls
You can use the left mouse button to add "walls" to the scene.
The path cannot go over those walls. To remove them you can use
the right mouse button.