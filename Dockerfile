FROM maven:3.6.3-jdk-8

MAINTAINER zzzmahesh@gmail.com

ENV DEBIAN_FRONT_END noninteractive

# Declaring internal / defaults variables
ENV ELASTICSEARCH_HOST localhost
ENV ELASTICSEARCH_PORT 9200
ENV ELASTICSEARCH_DATA_DIR /appln/data/elasticsearch
ENV ELASTICSEARCH_RESOURCE_DIR /appln/bin/elasticsearch
ENV ADFS_CLIENT_ID POPULATE_CLIENT_ID
ENV ADFS_RESOURCE POPULATE_ADFS_RESOURCE
ENV ADFS_HOST POPULATE_ADFS_HOST
ENV OAUTH2_HOST POPULATE_EXTERNAL_OAUTH2_HOST
ENV MYSQL_DATABASE conductor
ENV MYSQL_USER conductor
ENV MYSQL_PASSWORD conductor
ENV MYSQL_DATABASE_HOST localhost
ENV MYSQL_DATABASE_PORT 3306
ENV MARIADB4J_DIR /appln/data/mariadb4j
ENV SPRING_PROFILES_ACTIVE basic,mariadb4j,embedded-elasticsearch,embedded-oauth2,security,conductor
ENV CONDUCTOR_VERSION 2.30.4

# Switching to root working  directory
WORKDIR /

# Starting up as root user
USER root

# Installing all the base necessary packages for build and execution of executables i.e. Java, Maven etc.
RUN apt-get -y -qq update --ignore-missing --fix-missing \
  && apt-get -y -qq install sudo openssl libncurses5-dev

# Setting JAVA_HOME for performing Maven build.
ENV JAVA_HOME /usr/local/openjdk-8
ENV PATH="${JAVA_HOME}:${PATH}"

# Creating base directory
RUN mkdir /appln

# Creating necessary directory structures to host the platform
RUN mkdir /appln/bin /appln/bin/conductor /appln/bin/elasticsearch /appln/data /appln/data/mariadb4j /appln/data/elasticsearch /appln/scripts /appln/logs /appln/tmp /appln/tmp/conductor-boot

# Creating a dedicated user conductor
RUN groupadd -g 999 conductor \
  && useradd -u 999 -g conductor -G sudo --create-home -m -s /bin/bash conductor \
  && echo -n 'conductor:conductor' | chpasswd

# Delegating password less SUDO access to the user conductor
RUN sed -i.bkp -e \
      's/%sudo\s\+ALL=(ALL\(:ALL\)\?)\s\+ALL/%sudo ALL=NOPASSWD:ALL/g' \
      /etc/sudoers

# Taking the ownership of working directories
RUN chown -R conductor:conductor /appln

# Copying the necessary src for build.
COPY src /appln/tmp/conductor-boot/
COPY pom.xml /appln/tmp/conductor-boot/

# Changing to the user conductor
USER conductor

# Building the executable.
RUN cd /appln/tmp \
  && cd conductor-boot \
  && mvn clean install -q

# Moving the executable / build to the run location
RUN mv /appln/tmp/conductor-boot/target/conductor-boot*.jar /appln/bin/conductor

# Creating the startup script, by passing the env variables to run the jar and tailing /dev/null to keep container running even in case of jar startup failure.
RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin/conductor" >> /appln/scripts/startup.sh \
  && echo "java \
  -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \
  -DELASTICSEARCH_HOST=$ELASTICSEARCH_HOST \
  -DELASTICSEARCH_PORT=$ELASTICSEARCH_PORT \
  -DADFS_CLIENT_ID=$ADFS_CLIENT_ID \
  -DADFS_RESOURCE=$ADFS_RESOURCE \
  -DADFS_HOST=$ADFS_HOST \
  -DOAUTH2_HOST=$OAUTH2_HOST \
  -DMYSQL_DATABASE=$MYSQL_DATABASE \
  -DMYSQL_USER=$MYSQL_USER \
  -DMYSQL_PASSWORD=$MYSQL_PASSWORD \
  -DMYSQL_DATABASE_HOST=$MYSQL_DATABASE_HOST \
  -DMYSQL_DATABASE_PORT=$MYSQL_DATABASE_PORT \
  -DMARIADB4J_DIR=$MARIADB4J_DIR \
  -DELASTICSEARCH_DIR=$ELASTICSEARCH_DIR \
  -DELASTICSEARCH_RESOURCE_DIR=$ELASTICSEARCH_RESOURCE_DIR \
  -jar conductor-boot-$CONDUCTOR_VERSION.jar > /appln/logs/conductor.log 2>&1 & " >> /appln/scripts/startup.sh \
  && echo "echo \"\$!\" > /appln/app.pid" >> /appln/scripts/startup.sh \
  && echo "sudo tail -f /dev/null" >> /appln/scripts/startup.sh

# Owning the executable scripts
RUN sudo chown -R conductor:conductor /appln/scripts /appln/bin
RUN sudo chmod -R +x /appln/scripts /appln/bin
RUN sudo chmod -R +w /appln/data

# Removing the temp folder i.e. source code etc used for creating the executable / build.
RUN sudo rm -rf /appln/tmp/* /tmp/* ~/.m2 /appln/data/elasticsearch/* /appln/data/mariadb4j/*

# Exposing the necessary ports
EXPOSE 8080

# Enabling the startup
CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]
