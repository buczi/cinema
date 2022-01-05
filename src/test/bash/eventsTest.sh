#!/bin/bash
now=$(date +%s%3N)
twoHours=7200000
tenHours=36000000
echo "[Tests of /events endpoint]"
echo "Tests to be conducted:"
echo "- 1. given big time interval -> return all movie events"
echo "- 2. given smaller period -> return some movies"
echo "- 3. given startTime greater than endTime -> return status 400 and empty collection"
echo "- 4. given no parameter -> return status 400"
echo ""
echo "Conduct test [1]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 --data '{"startTime": "0", "endTime": "'"$(($now+$tenHours))"'"}' \
 "http:/localhost:8080/events"
echo ""
echo "Conduct test [2]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X POST \
 --data '{"startTime": "0", "endTime": "'"$(($now+$twoHours))"'"}' \
 "http:/localhost:8080/events"
echo ""
echo "Conduct test [3]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST \
 --data '{"startTime": "'"$tenHours"'", "endTime": "'"$twoHours"'"}' \
"http:/localhost:8080/events"
echo ""
echo "Conduct test [4]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
-X POST \
"http:/localhost:8080/events"