#!/bin/bash
echo Lanzando Jenkins en el puerto 8081
#java -jar /usr/share/jenkins/jenkins.war --httpPort=8081 >> /var/jenkins_home/jenkins_log.log
nohup java -jar /usr/share/jenkins/jenkins.war --httpPort=8081 &
echo Lanzando Liferay en el puerto 8080 y HSQL
#./opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/bin/startup.sh >> /var/liferay-home/lr_log.log
nohup ./opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/bin/startup.sh &
echo "El ejemplo esta corriendo"
bash