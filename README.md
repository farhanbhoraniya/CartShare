# CartShare - Term Project - Team2

Alok Goyal<br>
Shraddha Nayak<br>
Farhan Bhoraniya<br>
Shubham Singh <br>


How to use:
1. Import as maven project
2. Build the jar file and run java -jar CartShare-1.0-SNAPSHOT.jar
              OR
   In IDE, Run as Java Application
   

## Steps to Build on command line:

1) Navigate to the project repository.
2) Run the command `mvn package`
    This will generate jar file in the `target/` directory inside the project repo.
3) The jar file can then be executed using the command `java -r [path to jar file]`
   Add an ampersand sign (`&`) to make it run in the background.
4) All configuration is present in app.properties file.

**Info:**
The application runs on port 9000.

Nginx is configured to sit in front on port 80 and forwards all the requests to port 9000.
