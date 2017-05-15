#!/bin/bash
echo Lanzando Jenkins en el puerto 8081
nohup java -jar /usr/share/jenkins/jenkins.war --httpPort=8081 &
echo Lanzando Liferay en el puerto 8080 y HSQL
nohup ./opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/bin/startup.sh &
MAVEN_HOME=/opt/apache-maven-3.5.0/bin
export MAVEN_HOME=/opt/apache-maven-3.5.0
PATH=$PATH:$MAVEN_HOME/bin
echo "Inicializando codigo fuente en /var/setup/fuentes
cd /var/setup/fuentes
git init
git config core.sparseCheckout true
git remote add -f origin https://github.com/Iwomatic/lr-example.git
echo "LiferayExercise/*" > .git/info/sparse-checkout
git checkout master
cd /var/setup/
echo "El ejemplo esta corriendo"
bash