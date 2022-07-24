**"LoggerApi" application**

To run the application by commandline:
1. Prerequisites: maven version 3.6.3 and above, jdk11
2. Installing: open a commandline in main folder that contains project pom.xml file and use following commands:

- **mvn clean**  
- **mvn compile**
- **mvn package** 

3. After previous paragraph you will get a jar file in target folder:

 -  **target/LoggerApi-1.0-SNAPSHOT.jar**

4. To run LoggerApi-1.0-SNAPSHOT.jar, enter in commandline:
```
java -jar target/LoggerApi-1.0-SNAPSHOT.jar "[Path_to_input_file>] 
```
or (In case you don't enter a file name (filePath), it will already be predefined)

```
java -jar target/LoggerApi-1.0-SNAPSHOT.jar 
```

Examples of valid commands:
```
java -jar target/LoggerApi-1.0-SNAPSHOT.jar C:/Users/Halyna/Desktop/LoggerApp/LoggerApi/src/main/resources/logfile.txt
java -jar target/LoggerApi-1.0-SNAPSHOT.jar src/main/resources/logfile.txt
```

5. The result will be:
```
*****************Result About Logs Details From DataBase***************************
   LogDetails(id=scsmbstgra, duration=5, type=APPLICATION_LOG, host=12345, alert=true)
   LogDetails(id=scsmbstgrb, duration=3, type=null, host=null, alert=false)
   LogDetails(id=scsmbstgrc, duration=8, type=null, host=null, alert=true)
********************************************
```
![img_2.png](img_2.png)

6. Provided unit tests for LogService (src/test/java/org/logger/LogServiceUnitTest.java) and test for context of spring boot (src/test/java/org/logger/SpringBootContextTest.java).
7. After launch of the app you will find myApp.log in the root of the project.
8. The application uses HSQLDB, all setting are located in application.properties (src/main/resources/application.properties).

                               SUMMARY OF TASK
Our custom-build server logs different events to a file named logfile.txt. Every event has 2 entries in the file
- one entry when the event was started and another when the event was finished. The entries in the file
  have no specific order (a finish event could occur before a start event for a given id)
  Every line in the file is a JSON object containing the following event data:
  - id - the unique event identifier
  - state - whether the event was started or finished (can have values "STARTED" or "FINISHED"
  - timestamp - the timestamp of the event in milliseconds
  Application Server logs also have the following additional attributes:
  - type - type of log
  - host - hostname

{"id":"scsmbstgra", "state":"STARTED", "type":"APPLICATI ON_LOG", "host":"12345",
"timestamp":1491377495212}
{"id":"scsmbstgrb", "state":"STARTED", "timestamp":1491377495213}
{"id":"scsmbstgrc", "state":"FINISHED", "timestamp":1491377495218}
{"id":"scsmbstgra", "state":"FINISHED", "type":"APPLICATION_LOG", "host":"12345",
"timestamp":1491377495217}
{"id":"scsmbstgrc", "state":"STARTED", "timestamp":1491377495210}
{"id":"scsmbstgrb", "state":"FINISHED", "timestamp":1491377495216}
...

The program should:
- Take the path to logfile.txt as an input argument
- Parse the contents of logfile.txt
- Flag any long events that take longer than 4ms
- Write the found event details to file-based HSQLDB (http://hsqldb.org/) in the working folder
- The application should create a new table if necessary and store the following values:
- Event id
- Event duration
- Type and Host if applicable
- Alert (true if the event took longer than 4ms, otherwise false)
