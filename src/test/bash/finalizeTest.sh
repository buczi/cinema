#!/bin/bash
expirationTime=$(date +%s%3N)
twoHours=7200000
echo "[Tests of /finalize endpoint]"
echo "Tests to be conducted:"
echo "- 1. transaction already expired "
echo "- 2. correctly finalized transaction"
echo "- 3. finalize already finalized transaction"
echo "- 4. given incorrect reservation"
echo "- 5. given no parameter -> return status 400"
echo ""
echo "Conduct test [1]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 -d'{"reservationId": "1", "expirationTime": "'"$(($expirationTime-$twoHours))"'"}' \
 "http:/localhost:8080/finalize"
echo ""
echo "Conduct test [2]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 -d'{"reservationId": "2", "expirationTime": "'"$(($expirationTime+$twoHours))"'"}' \
 "http:/localhost:8080/finalize"
echo ""
echo "Conduct test [3]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 -d'{"reservationId": "2", "expirationTime": "'"$(($expirationTime+$twoHours))"'"}' \
 "http:/localhost:8080/finalize"
 echo ""
 echo "Conduct test [4]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
 -d'{"reservationId": "null", "expirationTime": "null"}' \
 -X POST \
"http:/localhost:8080/finalize"
 echo ""
 echo "Conduct test [5]"
 curl -i \
 -H "Accept: application/json" \
 -H "Content-Type:application/json" \
  -X POST \
  "http:/localhost:8080/finalize"