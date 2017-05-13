FROM openjdk:8-jre-alpine

RUN apk add --no-cache git openssh-client curl unzip bash ttf-dejavu coreutils

ARG JENKINS_URL=http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war

ENV JENKINS_HOME /var/jenkins_home
ENV JENKINS_OPTS --httpPort=-8081
VOLUME /var/jenkins_home


RUN mkdir -p /var/jenkins_home/init.groovy.d
RUN mkdir -p /usr/share/jenkins/
RUN mkdir -p /usr/share/exercise

COPY *.groovy /var/jenkins_home/init.groovy.d

RUN curl -L http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war -o /usr/share/jenkins/jenkins.war

EXPOSE 8081


RUN curl -O -k -L -C - http://downloads.sourceforge.net/project/lportal/Liferay%20Portal/6.2.5%20GA6/liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip \
	&& unzip liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip -d /opt \
	&& rm liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip

# add config for bdd
RUN /bin/echo -e '\nCATALINA_OPTS="$CATALINA_OPTS -Dexternal-properties=portal-bd-${DB_TYPE}.properties"' >> /opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/bin/setenv.sh

# add configuration liferay file
#ADD lr-setup/portal-bundle.properties /opt/liferay-portal-6.2-ce-ga6/portal-bundle.properties
ADD lr-setup/portal-ext.properties /opt/liferay-portal-6.2-ce-ga6/portal-ext.properties
#ADD lr-setup/portal-bd-MYSQL.properties /opt/liferay-portal-6.2-ce-ga6/portal-bd-MYSQL.properties
#ADD lr-setup/portal-bd-POSTGRESQL.properties /opt/liferay-portal-6.2-ce-ga6/portal-bd-POSTGRESQL.properties

# volumes
VOLUME ["/var/liferay-home", "/opt/liferay-portal-6.2-ce-ga6/"]

# Ports
EXPOSE 8080

COPY run-exercise.sh /usr/share/exercise/run-exercise.sh
RUN ["chmod", "+x", "/usr/share/exercise/run-exercise.sh"]
ENTRYPOINT ["/usr/share/exercise/run-exercise.sh"]
