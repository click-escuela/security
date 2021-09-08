#!/bin/bash
chmod +x /home/ec2-user/server/security/logs
chmod +x /home/ec2-user/server/security/logs/error.log
chmod +x /home/ec2-user/server/security/logs/debug.log

var="$(cat /home/ec2-user/server/security/security-service.pid)"
sudo kill $var
sudo rm -rf /home/ec2-user/server/security/security-service.pid