#!/bin/bash
ROOT=$TESTDIR/../../../Resources/www
(timeout 2 java WebServerMain $ROOT 12345 > /dev/null 2>&1 ) & (sleep 1 ; curl -s -I -X DELETE localhost:12345/test.txt | grep -i -a 'HTTP/1.1')
wait
