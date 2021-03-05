FROM maven:3.6.3-jdk-8 as builder

# Setting JAVA_HOME for performing Maven build.
ENV JAVA_HOME /usr/local/openjdk-8
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Creating base directory
RUN mkdir /tmp/conductor-boot

COPY pom.xml /tmp/conductor-boot/
COPY src /tmp/conductor-boot/src

# Building the executable.
RUN cd /tmp/conductor-boot \
  && mvn clean install -Dmaven.test.skip=true -q

FROM openjdk:8-jdk as server

MAINTAINER zzzmahesh@gmail.com

ENV DEBIAN_FRONT_END noninteractive

# Declaring internal / defaults variables
ENV ELASTICSEARCH_HOST localhost
ENV ELASTICSEARCH_PORT 9200
ENV ELASTICSEARCH_DATA_DIR /appln/data/elasticsearch
ENV ELASTICSEARCH_RESOURCE_DIR /appln/bin/elasticsearch
ENV ELASTICSEARCH_URL http://$ELASTICSEARCH_HOST:$ELASTICSEARCH_PORT
ENV WORKFLOW_ELASTICSEARCH_VERSION 5
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
ENV ADFS_USER_AUTHORIZATION_URL https://$ADFS_HOST/adfs/oauth2/authorize
ENV ADFS_ACCESS_TOKEN_URL https://$ADFS_HOST/adfs/oauth2/token
ENV ADFS_USER_INFO_URL https://$ADFS_HOST/adfs/oauth2/authorize
ENV OAUTH2_USER_INFO_URL https://$OAUTH2_HOST/oauth/token
ENV SPRING_PROFILES_ACTIVE basic,mariadb4j,embedded-elasticsearch,embedded-oauth2,security,conductor
ENV USER_TIMEZONE IST
ENV CONDUCTOR_VERSION 2.31.0

# Switching to root working  directory
WORKDIR /

# Starting up as root user
USER root

# Installing all the base necessary packages for execution of embedded MariaDB4j i.e. Open SSL, libaio & libncurses5
RUN apt-get -y -qq update --ignore-missing --fix-missing \
  && apt-get -y -qq install libaio1 libaio-dev libncurses5 openssl sudo

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

# Changing to the user conductor
USER conductor

# Moving the executable / build to the run location
COPY --from=builder /tmp/conductor-boot/target/conductor-boot*.jar /appln/bin/conductor/

# Creating the startup script, by passing the env variables to run the jar. Logs are written directly to continer logs.
RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin/conductor" >> /appln/scripts/startup.sh \
  && echo "java \
  -Dspring.profiles.active=\$SPRING_PROFILES_ACTIVE \
  -Duser.timezone=\$USER_TIMEZONE \
  -DELASTICSEARCH_HOST=\$ELASTICSEARCH_HOST \
  -DELASTICSEARCH_PORT=\$ELASTICSEARCH_PORT \
  -DELASTICSEARCH_URL=\$ELASTICSEARCH_URL \
  -Dworkflow.elasticsearch.version=\$WORKFLOW_ELASTICSEARCH_VERSION \
  -DADFS_CLIENT_ID=\$ADFS_CLIENT_ID \
  -DADFS_RESOURCE=\$ADFS_RESOURCE \
  -DADFS_HOST=\$ADFS_HOST \
  -DADFS_USER_AUTHORIZATION_URL=\$ADFS_USER_AUTHORIZATION_URL \
  -DADFS_ACCESS_TOKEN_URL=\$ADFS_ACCESS_TOKEN_URL \
  -DADFS_USER_INFO_URL=\$ADFS_USER_INFO_URL \
  -DOAUTH2_HOST=\$OAUTH2_HOST \
  -DOAUTH2_USER_INFO_URL=\$OAUTH2_USER_INFO_URL \
  -DMYSQL_DATABASE=\$MYSQL_DATABASE \
  -DMYSQL_USER=\$MYSQL_USER \
  -DMYSQL_PASSWORD=\$MYSQL_PASSWORD \
  -DMYSQL_DATABASE_HOST=\$MYSQL_DATABASE_HOST \
  -DMYSQL_DATABASE_PORT=\$MYSQL_DATABASE_PORT \
  -DMARIADB4J_DIR=\$MARIADB4J_DIR \
  -DELASTICSEARCH_DATA_DIR=\$ELASTICSEARCH_DATA_DIR \
  -DELASTICSEARCH_RESOURCE_DIR=\$ELASTICSEARCH_RESOURCE_DIR \
  -jar conductor-boot-$CONDUCTOR_VERSION.jar" >> /appln/scripts/startup.sh

# Owning the executable scripts
RUN sudo chown -R conductor:conductor /appln/scripts /appln/bin \
    && sudo chmod -R +x /appln/scripts /appln/bin \
    && sudo chmod -R +w /appln/data

# Exposing the necessary ports
EXPOSE 8080

# Enabling the startup
CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]
