# SmartBoard
Open Eclipse editor and clone smart board 

Then in order to run the A2 SmartBoard project, you will need to ensure the following are installed
- JavaFX 
	- ensure JavaFX jar file is downloaded and added in your user library to start with
	- Right click on project and Build path -> click configure build path, Go to the Build path libraries section and you will have to 
	  add JavaFX to the build path by clicking on add library  - user library and including javafx in your user library
- JUnit 4
	- Right click on project and Build path -> click configure build path, Go to the Build path libraries section and you will have to 
	  add JUnit to the build path by selecting add library and selecting JUnit
- SQLlite jar file
	- You must download the sqllite jar file from https://github.com/xerial/sqlite-jdbc#usage and add that file path by:
	- Right click on project and Build path -> click configure build path, Go to the Build path libraries section and you will have to 
	  add jar file to the referenced libraries by selecting add external jars
	  
A test user is created on the initial run if the database is not set up
username: test
password: test

To run, ensure your package is open and click on the run main button. You can choose to create a new user or use the above test user

To test, click on the test package and double click the test class that you would like to test and test that class by clicking the run
button 
