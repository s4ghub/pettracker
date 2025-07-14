# Intro
This application returns the number of pets outside the power saving zone and have other relevant endpoints.

# Curl and Postman

This application gives 6 endpoints to insert, update and get pet tracker info Data.
- The example curl requests for testing are provided in src/main/resources/curl_requests.txt
- The example postman requests for testing are there in src/main/resources/tractive.postman_collection.json

# Brief description
- insert api is used to insert the Pet record for the first time in the database.
- update api is basically used by the tracker to send the location info. The tracker lost info may be sent by the same api.
- getAll api returns all the current data in the db
- getOutOfZone api returns all the pets which are not lost but outside powersaving zone.
- getbyowners api returns all the pets owned by a particular owner
- lost api returns all the lost tracker infos

# How to get the application in your system
- Step 1: Install git
- Step 2: Run  git clone git@github.com:s4ghub/pettracker.git from command line

# How to run the application?

### Way 1: 
run main method of src/main/java/com/example/pettracker/pettracker/PettrackerApplication.java from the IDE.

### way 2:
- Get inside directory C:\<clone location>\pettracker from command prompt
- Run mvn spring-boot:run

# System used:

- OS: Windows 11 home
- IDE: Intellij idea 2025.1.2 community edition
- jdk 21

# How to run the tests.

- Run PettrackerApplicationIntegrationTests.java from IDE
- Run PetTrackerInputValidationUnitTests.java from IDE
- MockMvc and end-toend resttemplate test cases are not added as per assumption. If required those can be added too.

# Code walk through

- GlobalExceptionHandler catches all the exceptions. Wrong formatted Json, such as missing curly brace is handled too.
- All the rest endpoints are in PetTrackingController.java
- InputValidator.java does custom validation.
- PetTrackingService.java is a java interface as the service interface
- IOutOfZoneCount.java is the dto for pets out of power saving zone aggregation result.
- Pet is the single entity


# Assumptions

- There is no data race in this application as each tracker is responsible to send update for it's own record. So no scenario of 2 threads competing with each other for updating same data.
- It's assumed that the tracker sends update signal only when it's sure that the earlier signal has either succeeded or failed.
- If the tracker is lost then it is not a candidate for in or out of zone aggregation.
- No delete api is provided. But if required it won't be an issue to add.



# Philosophy

- For simplicity pagination has not been used in the controller when multiple entities are returned
- In memory db H2 has been used.
- Scalability, durability or availability are not the purpose of this poc. But if MongoDb or similar is used and other components of distributed system (such as load balancer etc.) these peoperties can be easily achieved.
- H2 and Jpa can be replaced with any other suitable db and crudrepository, as spring data has been used.
- Single table structure is used as joins are not necessary in most of the cases.
- The db can be easily replaced with redis or MongoDb.
- Total 7 hours are used to code this application.



