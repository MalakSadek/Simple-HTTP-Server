#!/bin/bash
ROOT=$TESTDIR/../../../Resources/www
(timeout 2 java WebServerMain $ROOT 12345 > /dev/null 2>&1 ) & (sleep 1 ; curl -s -I -X PUT localhost:12345/"test.txt text/txt 15 \"This is a test.\"")
wait
