FROM openjdk:8-jdk-alpine

RUN apk add --no-cache git openssh-client curl unzip bash ttf-dejavu coreutils

# Creación de directorios
RUN mkdir -p /usr/share/jenkins/
RUN mkdir -p /usr/share/exercise
RUN mkdir -p /var/setup/jenkins_home
RUN mkdir -p /var/setup/fuentes

# Variables de entorno
ENV JENKINS_HOME /var/setup/jenkins_home
ENV JENKINS_OPTS --httpPort=-8081
ENV MAVEN_HOME=/opt/apache-maven-3.5.0/bin

# Añadimos a la imagen el directorio jenkins_home
ADD jenkins_home /var/setup/jenkins_home

# Descargamos Jenkins
RUN curl -L http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war -o /usr/share/jenkins/jenkins.war

# Descargamos Liferay 6.2 y descomprimimos
RUN curl -O -k -L -C - http://downloads.sourceforge.net/project/lportal/Liferay%20Portal/6.2.5%20GA6/liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip \
	&& unzip liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip -d /opt \
	&& rm liferay-portal-tomcat-6.2-ce-ga6-20160112152609836.zip

# Añadimos configuracion de Liferay
ADD lr-setup/portal-ext.properties /opt/liferay-portal-6.2-ce-ga6/portal-ext.properties
ADD lr-setup/public.lar /opt/liferay-portal-6.2-ce-ga6/public.lar
ADD lr-setup/LiferayExercise.war /opt/liferay-portal-6.2-ce-ga6/deploy/LiferayExercise.war

#Descargamos y descomprimimos Maven
RUN curl -O -k -L -C - http://apache.uvigo.es/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip \
	&& unzip apache-maven-3.5.0-bin.zip -d /opt \
	&& rm apache-maven-3.5.0-bin.zip


# Volumenes
VOLUME ["/var/setup/compartido"]

# Puertos (8080 Tomcat, 8081 Jenkins)
EXPOSE 8080
EXPOSE 8081

# Copiamos scripts
COPY scripts/run-exercise.sh /usr/share/exercise/run-exercise.sh
COPY scripts/copy.sh /var/setup/compartido/copy.sh

# Punto de entrada
RUN ["chmod", "+x", "/usr/share/exercise/run-exercise.sh"]
ENTRYPOINT ["/usr/share/exercise/run-exercise.sh"]
