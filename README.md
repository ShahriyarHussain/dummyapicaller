# Dummy API Caller

Save APIs through a frontend and call them from your server side application/apiclient.

## Requirements:

Must have **Java version 11 or above** installed. For a minimal JRE to run the app you can Download **Eclipse Temurin JRE** from *[here.](https://adoptium.net/temurin/releases/?version=11)*

## Running The App:

Download the zip file from Releases section and extract it. you will find a `.jar` file here. Open terminal/Command Line and type 

```java -jar dummy-api-2.0.0.jar```

The app should start. Go to *localhost:8080* in your browser and you should see the App running.

## Properties

You can find the following properties in the `application.properties` file. 

```
\# Misc
logging.level.org.atmosphere = warn 
spring.mustache.check-template-location = false

\# You can change these to your liking
server.port=${PORT:8080} 
response.folder.name = saved-responses
base.url = localhost
server.error.include-stacktrace=never

```

Email [me](mailto:ad1whqv2q@mozmail.com) for any issues.

#### N.B. While developing the application, it is recommended to launch the `Application` class in debug mode from your IDE.
