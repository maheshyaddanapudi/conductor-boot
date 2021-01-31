FROM ubuntu:latest

MAINTAINER zzzmahesh@gmail.com

ENV DEBIAN_FRONT_END noninteractive

# Switching to root working  directory
WORKDIR /

# Starting up as root user
USER root

# Installing all the base necessary packages for build and execution of executables i.e. Java, Maven etc.
RUN apt-get -y update --ignore-missing --fix-missing \
  && apt-get -y install software-properties-common \
  && apt-get -y install libaio1 libaio-dev \
  && apt-get -y install sudo vim curl wget net-tools openjdk-8-jdk openssl

ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

RUN apt-get -y install maven

# Creating a dedicated user conductor
RUN groupadd -g 999 conductor \
  && useradd -u 999 -g conductor -G sudo --create-home -m -s /bin/bash conductor \
  && echo -n 'conductor:conductor' | chpasswd

# Delegating password less SUDO access to the user conductor
RUN sed -i.bkp -e \
      's/%sudo\s\+ALL=(ALL\(:ALL\)\?)\s\+ALL/%sudo ALL=NOPASSWD:ALL/g' \
      /etc/sudoers

# Changing to the user conductor
USER conductor

# Creating base directory
RUN sudo mkdir /appln

# Taking the ownership of working directories
RUN sudo chown -R conductor:conductor /appln

# Creating necessary directory structures to host the platform
RUN mkdir /appln/bin /appln/scripts /appln/logs /appln/tmp /appln/tmp/conductor-boot

# Owning the executable scripts
RUN sudo chown -R conductor:conductor /appln/scripts
RUN chmod -R +x /appln/scripts

COPY src /appln/tmp/conductor-boot/
COPY pom.xml /appln/tmp/conductor-boot/

# Copying the necessary scripts for build, startup etc.
RUN cd /appln/tmp \
  && cd conductor-boot \
  && mvn clean install

RUN sudo mv /appln/tmp/conductor-boot/target/conductor-boot*.jar /appln/bin

RUN echo "#!/bin/bash" > /appln/scripts/startup.sh \
  && echo "cd /appln/bin" >> /appln/scripts/startup.sh \
  && echo "java -Dspring.profiles.active=\"basic,conductor,mariadb4j,embedded-elasticsearch,embedded-oauth2,security\" -jar conductor-boot-2.30.4.jar > /appln/logs/conductor.log 2>&1 & " >> /appln/scripts/startup.sh \
  && echo "echo \"\$!\" > /appln/app.pid" >> /appln/scripts/startup.sh \
  && echo "sudo tail -f /dev/null" >> /appln/scripts/startup.sh


# Owning the executable scripts
RUN sudo rm -rf /appln/tmp

# Exposing the necessary ports
EXPOSE 8080

CMD ["/appln/scripts/startup.sh"]
ENTRYPOINT ["/bin/bash"]
