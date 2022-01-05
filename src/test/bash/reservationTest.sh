#!/bin/bash
echo "[Tests of /reserve endpoint]"
echo "Tests to be conducted:"
echo "- 1. [Validation Test] given name shorter than 3  -> return status 400"
echo "- 2. [Validation Test] given surname shorter than 3  -> return status 400"
echo "- 3. [Validation Test] given name and surname shorter than 3  -> return status 400"
echo "- 4. [Validation Test] given name which doesn't fit pattern  -> return status 400"
echo "- 5. [Validation Test] given surname which doesn't fit pattern  -> return status 400"
echo "- 6. [Validation Test] given name and surname which doesn't fit pattern  -> return status 400"
echo "- 7. [Validation Test] reservation without seats -> return status 400"
echo "- 8. [Validation Test] reservation without event  -> return empty reservation"
echo "- 9. [Validation Test] reservation without submitDate  -> return status 400"

echo "- 10. [Seat Placement Test] reservation with already reserved seats  -> return empty reservation"
echo "- 11. [Seat Placement Test] reservation with one place left between picked seats  -> return empty reservation"
echo "- 12. [Seat Placement Test] reservation with one place left between picked seats and already reserved seats  -> return empty reservation"
echo "- 13. [Seat Placement Test] reservation with correctly placed seats  -> return full reservation"
echo ""
echo "Conduct test [1]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
 -X POST \
 --data '{"name": "Al", "surname": "Biscops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
 "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [2]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Bi", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [3]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Al", "surname": "Bi", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [4]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "anDrzej+", "surname": "Biscops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [5]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "bisCops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [6]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "andrzeJ", "surname": "bisCops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [7]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "1" ,"reservedSeats": []}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [8]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "null" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [9]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "null", "eventId": "1" ,"reservedSeats": [{ "first": "4", "second":"ADULT"},{"first": "5", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [10]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "2", "second":"ADULT"},{"first": "3", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [11]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "4" ,"reservedSeats": [{ "first": "9", "second":"ADULT"},{"first": "11", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [12]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "1" ,"reservedSeats": [{ "first": "1", "second":"ADULT"},{"first": "6", "second":"ADULT"}]}' \
  "http:/localhost:8080/reserve"
echo ""
echo "Conduct test [13]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
 --data '{"name": "Andrzej", "surname": "Biscops", "submitDate": "100", "eventId": "4" ,"reservedSeats": [{ "first": "9", "second":"ADULT"},{"first": "10", "second":"STUDENT"}]}' \
  "http:/localhost:8080/reserve"
