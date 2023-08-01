# Dummy API Caller

Save APIs through a frontend and call them from your server side application.

## Compatibility

Currently the application is compatible with Java 11 or above. There for JRE 11 or above must be installed in order to run the jar in local machine.

## Running the JAR

Download the jar from release section. Open Terminal/Cmd and run:
```java -jar dummy-api-caller.jar```
The application should start on `localhost:15070`.

### Changing properties and accessing saved files

Just running the jar would use the properties inside the jar and would also save file in the jar. 

To change the properties, create an `application.properties` file in the same directory as the jar. The mandatory propreties for the file are below:
```
response.folder.name = dummyJsonResponses
base.url = localhost
```
You can also add the following properties to choose the port where the application is running and the logging in the console.
```
server.port=${PORT:15070} 
logging.level.org.atmosphere = warn
```
Create a folder in the same directory as the jar file. The folder and the property `response.folder.name` in properties file must be kept same.

Now the run the jar file like before and you should see your responses in the folder.


Email me at ad1whqv2q@mozmail.com for any issues that you are facing.


## Running the Application In Your IDE

While developing the application, it is recommended to launch the `Application` class in debug mode from your IDE.

After the application has started, you can view your it at http://localhost:8080/ in your browser.
