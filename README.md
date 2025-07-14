# Intro
This application returns the number of pets outside the power saving zone and have other relevant endpoints.

# The apis.

This application gives 6 endpoints to insert, update and get pet tracker info Data.
- The example curl requests for testing are provided in src/main/resources/curl_requests.txt
- The example postman requests for testing are there in src/main/resources/tractive.postman_collection.json

### insert api is used to insert the Pet record for the first time in the database.
### update api is basically used by the tracker to send the location info. The tracker lost info may be sent by the same api.
### getAll api returns all the current data in the db
### getOutOfZone returns all the pets which are not lost but outside powersaving zone.
### getbyowners returns all the pets owned by a particular owner
### lost returns all the lost tracker infos

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

# How to run the integration test.

- Run PettrackerApplicationIntegrationTests.java from IDE


