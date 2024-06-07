#!/bin/bash
java -jar -Dspring.profiles.active=production /app/snippet-service.jar &
tail -f /dev/null
