#!/bin/bash
echo "Lanzando Jenkins en el puerto 8081"
nohup java -jar /usr/share/jenkins/jenkins.war --httpPort=8081 &