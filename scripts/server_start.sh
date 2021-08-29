#!/usr/bin/env bash
cd /home/ec2-user/server/security

sudo rm -rf /home/ec2-user/server/security/security-service.pid

echo"eliminando archivo"

sudo java -jar -Dspring.datasource.url=jdbc:mysql://clickescuela.ccmmeszml0xl.us-east-2.rds.amazonaws.com:3306/clickescuela -Dspring.datasource.username=root -Dspring.datasource.password=secret123 \
security-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null & echo $! > security-service.pid
