cyNeo4j
============

[![build](https://github.com/cyNeo4j/cyNeo4j/actions/workflows/maven.yml/badge.svg)](https://github.com/cyNeo4j/cyNeo4j/actions/workflows/maven.yml)

A Cytoscape app to connect to a Neo4j database and execute extensions of the Neo4j database.
This app is being extended to query the shortest path for metabolic conversions.

Minimum System Requirements (for CyNeo4j 2.x):
- Neo4j [3.5.x](https://neo4j.com/download-center/#community)
- Cytoscape [3.9.x](https://cytoscape.org/download.html) (automatically comes with a Java 11 download)


Find the app in the Cytoscape app store (http://apps.cytoscape.org/apps/cyneo4j) and on
Github (https://github.com/cyneo4j/cyNeo4j).

For a tutorial and installation guide for the server side component checkout
https://cyneo4j.wordpress.com/ !

Build this project from the command line:
```
mvn clean install
```

Update the app through Eclipse:
1. (Fork and) clone this repository; open Eclipse and create a new workspace.
2. Import this project by clicking "Import projects..." in the package explorer, select Git > Projects from Git > Existing local repository.
3. Add the folder where you cloned this repository, click Finish, Next, and Finish.
4. Build this repository with the Maven dependancies using the pom.xml file; rightclick on file, select "Maven > Update Project"
5. Create a launch configuration using the pom.xml file; rightclick on file, select "Run as > Run configurations...". Provide a name, and add as goal 'clean install'. Click Apply, click 'Run'
6. Build the jar with existing launch configuration using the pom.xml file; rightclick on file, select "Run as > Maven Build".


Icons from:
http://openclipart.org/detail/7983/red-+-green-ok-not-ok-icons-by-tzeeniewheenie
