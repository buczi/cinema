#!/bin/bash
now=$(date +%s%3N)
twoHours=7200000
echo ""
echo "Check what movies are available in the cinema in nearest two hours"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 --data '{"startTime": "'"$now"'", "endTime": "'"$(($now+$twoHours))"'"}' \
 "http:/localhost:8080/events"
 echo ""
 echo "Pick movie that is going to be played in 10 minutes (3rd event)"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X GET \
 "http:/localhost:8080/screening?eventId=3"
 echo ""
 echo "All places are free so well pick two"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json;" \
  -X POST \
 --data '{"name": "Bob", "surname": "Dylan", "submitDate": "'"$now"'", "eventId": "3" ,"reservedSeats": [{ "first": "1", "second":"ADULT"},{"first": "2", "second":"CHILD"}]}' \
  "http:/localhost:8080/reserve"
  echo ""
  echo "The reservation was declined due to time limitation"
  echo "Pick movie that is going to be played a little bit later (1st event)"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X GET \
 "http:/localhost:8080/screening?eventId=1"
 echo ""
 echo "Pick seats number 1 in first row and number 2 in second"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json;" \
  -X POST \
 --data '{"name": "Bob", "surname": "Dylan", "submitDate": "'"$now"'", "eventId": "1" ,"reservedSeats": [{ "first": "1", "second":"ADULT"},{"first": "6", "second":"CHILD"}]}' \
  "http:/localhost:8080/reserve"
 echo ""
 echo "Another decline due to seat left between"
 echo "After picking new seats with number 1 in first row and number 2 in first row, but unfortunately the name starts with small character"
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json;" \
   -X POST \
  --data '{"name": "Bob", "surname": "dylan", "submitDate": "'"$now"'", "eventId": "1" ,"reservedSeats": [{ "first": "1", "second":"ADULT"},{"first": "2", "second":"CHILD"}]}' \
   "http:/localhost:8080/reserve"
   echo ""
   echo "Another decline due to incorrect naming"
   echo "Last try with same seats and corrected surname"
     curl -i \
     -H "Accept: application/json" \
     -H "Content-Type:application/json;" \
      -X POST \
     --data '{"name": "Bob", "surname": "Dylan", "submitDate": "'"$now"'", "eventId": "1" ,"reservedSeats": [{ "first": "1", "second":"ADULT"},{"first": "2", "second":"CHILD"}]}' \
      "http:/localhost:8080/reserve"
  echo ""
  echo "It worked now only thing left is payment"
  echo "After payment finally whole process will finish"
  curl -i \
  -H "Accept: application/json" \
  -H "Content-Type:application/json" \
   -X POST \
   -d'{"reservationId": "3", "expirationTime": "'"$(($now+$twoHours))"'"}' \
   "http:/localhost:8080/finalize"
  echo "All done now it is time to enjoy movie"

