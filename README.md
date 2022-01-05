# Cinema - api
## Description
This application goal was to expose REST-api intended to work with frontend application used to make reservations for movies
played in multiplex cinema. Application uses in memory database h2 and operates with springboot framework.
## Running
In order to start application go to root folder and run command:
```bash
# Run main application
 ./gradlew bootRun
 # Run java junit tests
 ./gradlew test 
 # Run shell scripts
 cd src/test/bash
 ./eventTest.sh             # Test of /events endpoint
 ./finalizeTest.sh          # Test of /finalize endpoint
 ./reservationTest.sh       # Test of /reserve endpoint
 ./screeningTest.sh         # Test of /screening endpoint
 ./testCase.sh              # Business case of a person trying to buy tickets
```
## Architecture
There are 4 endpoints available in the application:
### /events
|Method| POST| 
| --- | --- |
|**Request body**|    Time interval presented as two unix epoch time in seconds|
|**Response**|        Events that are taking place in given time interval|
|**Validation**|      Time interval is checked if it starts before its end time |
### /screening
|Method| GET |
| --- | --- |
|**Request param**|   Index of event |
|**Response**|        All seats in hall in which event is taking place with marked information if seat is taken| 
### /reserve
|Method| POST|
| --- | --- |
|**Request body**|    All information required to make reservation: event, seats to reserve with ticket type, credentials |
|**Response**|        Reservation index, total price of reservation and time when the reservation will expire |
|**Validation**|      Check if the credentials matches regular expressions and there are no less than 1 seat picked|
### /finalize
|Method| POST| 
| --- | --- |
|**Request body**|    Identifier of reservation and time of expiration time|
|**Response**|        Status of the performed actions|

### Assumptions
In case of illegal state of request for example incorrect seat placement, information about failure is logged and the returned
value is empty.
## Demo
Demo contains of two parts. First part will be conducted with usage of shell scripts and the second one with java tests.\
rest calls are made. 
1. User checks if for movies that will be played in the next two hours.
2. User pick event with id 3 which is movie James Bond 007 played in 10 minutes.
3. User looks at the free seats and tries to make a reservation
4. Reservation is denied due to time constraints (user cannot reserve tickets less than 15 minutes before event)
5. User changes the event to 1, movie Diune played in 20 minutes. (Already reserved seats 1row:3, 1row:4, 2row:3, 2row:4)
6. User picks 2 seats (1row:1, 2row:2) and correctly inserts credentials.
7. Reservation is denied due to leaving one place between 1row:1 -> 1row:3
8. User picks 2 correct seats (1row:1, 1row:2) but inserts its surname starting with small letter
9. Reservation is denied due to incorrect naming
10. User fixes naming 
11. User finalizes the reservation right away and retrieves positive response
```bash
.../src/test/bash ./testCase.sh
```
### Polish characters
Due to errors in validation during the testing, there is an alternative test for polish characters send in request body.
Information in the database and returned is displayed correctly (1 point of demo).
Unfortunately all tests conducted with curl failed but with usage of java http request and postman such difficulties
did not occur. Default coding is utf-8 which can handle polish characters. Changing file encoding also did not help.
```bash
./gradlew polishTest
```