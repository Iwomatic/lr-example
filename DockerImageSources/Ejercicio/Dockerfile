FROM openjdk:8-jre-alpine

RUN apk add --no-cache git openssh-client curl unzip bash ttf-dejavu coreutils

ARG JENKINS_URL=http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war

RUN mkdir -p /usr/share/jenkins/
RUN mkdir -p /usr/share/exercise
RUN mkdir -p /var/setup/jenkins_home

ENV JENKINS_HOME /var/setup/jenkins_home
ENV JENKINS_OPTS --httpPort=-8081
#VOLUME /var/jenkins_home
ADD jenkins_home /var/setup/jenkins_home



#COPY *.groovy /var/jenkins_home/init.groovy.d
# Descargamos Jenkins
RUN curl -L http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war -o /usr/share/jenkins/jenkins.war

EXPOSE 8081

# Descargamos Liferay 6.2 y descomprimimos
RUN curl -O -k -L -C - http://downloads.sourceforge.net/project/lportal/Liferay%20Portal/6.2.5%20GA6/liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip \
	&& unzip liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip -d /opt \
	&& rm liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip

# add config for bdd
#RUN /bin/echo -e '\nCATALINA_OPTS="$CATALINA_OPTS -Dexternal-properties=portal-bd-${DB_TYPE}.properties"' >> /opt/liferay-portal-6.2-ce-ga6/tomcat-7.0.62/bin/setenv.sh

# Añadimos portal-ext.properties
ADD lr-setup/portal-ext.properties /opt/liferay-portal-6.2-ce-ga6/portal-ext.properties

# Volumenes
VOLUME ["/var/setup/compartido"]

# Puertos
EXPOSE 8080

COPY scripts/run-exercise.sh /usr/share/exercise/run-exercise.sh
COPY scripts/copy.sh /var/setup/compartido/run-exercise.sh
RUN ["chmod", "+x", "/usr/share/exercise/run-exercise.sh"]
ENTRYPOINT ["/usr/share/exercise/run-exercise.sh"]
