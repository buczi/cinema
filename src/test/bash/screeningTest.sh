#!/bin/bash
# use -v to display response status
echo "[Tests of /screening endpoint]"
echo "Tests to be conducted:"
echo "- 1. given correct parameter -> return all seats for given screening"
echo "- 2. given incorrect parameter -> return empty list"
echo "- 3. given no parameter -> return status 400"
echo ""
echo "Conduct test [1]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X GET \
 "http:/localhost:8080/screening?eventId=1"
echo ""
echo "Conduct test [2]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X GET \
 "http:/localhost:8080/screening?eventId=-1"
echo ""
echo "Conduct test [3]"
curl -i \
-H "Accept: application/json" \
-H "Content-Type:application/json" \
 -X GET \
 "http:/localhost:8080/screening"
