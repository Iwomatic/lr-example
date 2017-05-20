FROM openjdk:8-jdk-alpine

RUN apk add --no-cache git openssh-client curl unzip bash ttf-dejavu coreutils

# Creación de directorios
RUN mkdir -p /usr/share/jenkins/
RUN mkdir -p /var/setup/jenkins_home

# Variables de entorno
ENV JENKINS_HOME /var/setup/jenkins_home
ENV JENKINS_OPTS --httpPort=-8081
ENV MAVEN_HOME=/opt/apache-maven-3.5.0/bin

# Añadimos a la imagen el directorio jenkins_home
ADD jenkins_home/ /var/setup/jenkins_home

# Descargamos Jenkins
RUN curl -L http://mirrors.jenkins.io/war-stable/2.46.2/jenkins.war -o /usr/share/jenkins/jenkins.war

#Descargamos y descomprimimos Maven
RUN curl -O -k -L -C - http://apache.uvigo.es/maven/maven-3/3.5.0/binaries/apache-maven-3.5.0-bin.zip \
	&& unzip apache-maven-3.5.0-bin.zip -d /opt \
	&& rm apache-maven-3.5.0-bin.zip


# Volumenes
VOLUME ["/var/setup/jenkins_home"]

# Puertos (8081 Jenkins)
EXPOSE 8081

# Copiamos scripts
COPY run.sh /usr/share/jenkins/run.sh

# Punto de entrada
RUN ["chmod", "+x", "/usr/share/jenkins/run.sh"]
ENTRYPOINT ["/usr/share/jenkins/run.sh"]
