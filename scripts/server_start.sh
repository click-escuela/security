#!/usr/bin/env bash
cd /home/ec2-user/server/security

sudo rm -rf /home/ec2-user/server/security/security-service.pid

echo"eliminando archivo"

sudo java -jar security-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null & echo $! > security-service.pid