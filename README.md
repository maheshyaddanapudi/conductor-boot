# Spring Boot Wrapper for Netflix Conductor Server 
## With Embedded persistent Security(OAuth2), Database(MariaDB) and Elasticsearch(V5.5.2) 
## With External Security(OAuth2 & ADFS), Database(MariaDB & MySQL) and Elasticsearch support


[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_conductor-boot)

Before starting, for details on Netflix Conductor, refer to <a href="http://netflix.github.io/conductor/">Conductor Documentation</a>

##### The README covers on the operational part of Conductor Boot. For more details on the code level walkthrough / developer point of view, please refer to <a href="https://zzzmahesh.medium.com/netflix-conductor-spring-boot-wrapper-45960a3e36f6">NETFLIX CONDUCTOR — SPRING BOOT WRAPPER</a>

##### Note

      • Only MySQL is supported as external Database
      
      				Or
      
      • Embedded MariaDB4j is supported, which is persistent and data is not lost, thus eliminating the need for external


## Overview

The idea is to build a single production grade Spring Boot Jar with the following 

      • Micro Services Orchestration - by Conductor Server
      
      • Spring Cloud - OAuth2 Authentication & Authorization - by Auth & Resource Servers / External OAuth2 provider or ADFS provider
      
      • Zuul Gateway - by Netflix Zuul Proxy for acting as proxy to Conductor Server APIs
      
      • Optional Embedded Persistent MariaDB4j

      • Optional Embedded Persistent Elasticsearch

## Build using maven

		cd <to project root folder>
		mvn clean install
		
	The maven build should place the conductor-boot-${conductor.version}.jar inside the target folder.

## Build Status

| CI Provider | Status          |
| ------- | ------------------ |
| Circle CI   | [![maheshyaddanapudi](https://circleci.com/gh/maheshyaddanapudi/conductor-boot.svg?style=shield)](https://circleci.com/gh/maheshyaddanapudi/conductor-boot) |
| Java CI   | ![Java CI with Maven](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main) |
| Travis CI   | [![Build Status](https://travis-ci.com/maheshyaddanapudi/conductor-boot.svg?branch=main)](https://travis-ci.com/maheshyaddanapudi/conductor-boot) |

## Code coverage

CodeQL: ![CodeQL](https://github.com/maheshyaddanapudi/conductor-boot/workflows/CodeQL/badge.svg?branch=main)

## Code quality

SonarQube: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=maheshyaddanapudi_conductor-boot&metric=alert_status)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_conductor-boot)

## Containerization CI

| CI Provider | Status          |
| ------- | ------------------ |
| Docker   | ![Docker](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Docker/badge.svg?branch=main) |
| Docker Image CI   | ![Docker Image CI](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Docker%20Image%20CI/badge.svg?branch=main) |

Docker Image published to <a href="https://hub.docker.com/repository/docker/zzzmahesh/conductorboot" target="_blank">DockerHub here</a>

Image is equipped with basic tools like vim, curl, wget, net-tools(telnet)

To pull the image :

	docker pull zzzmahesh/conductorboot

To run the container :

    docker run --name conductor-boot -p 8080:8080 -d zzzmahesh/conductorboot:latest

  Few other examples / ways / configurations to run the container as:

    1) Running with external MySQL and Elasticsearch & Embedded OAuth2 - The database and elasticsearch search is decoupled just leaving the OAuth2 feature to be embedded. The OAuth2 data is persistent as it uses MySQL Database.

        docker run --name conductor-boot -p 8080:8080 \
            -e SPRING_PROFILES_ACTIVE=basic,mysql,external-elasticsearch,embedded-oauth2,security,conductor \
            -e MYSQL_DATABASE_HOST=172.x.x.x \
            -e MYSQL_DATABASE_PORT=3306 \
            -e MYSQL_USER=conductor \
            -e MYSQL_PASSWORD=conductor \
            -e ELASTICSEARCH_HOST=172.x.x.x \
            -e ELASTICSEARCH_PORT=9200 \
            -d zzzmahesh/conductorboot:latest

    2) Running with external MySQL, Elasticsearch & External OAuth2

        docker run --name conductor-boot -p 8080:8080 \
            -e SPRING_PROFILES_ACTIVE=basic,mysql,external-elasticsearch,external-oauth2,security,conductor \
            -e MYSQL_DATABASE_HOST=172.x.x.x \
            -e MYSQL_DATABASE_PORT=3306 \
            -e MYSQL_USER=conductor \
            -e MYSQL_PASSWORD=conductor \
            -e ELASTICSEARCH_HOST=172.x.x.x \
            -e ELASTICSEARCH_PORT=9200 \
            -e OAUTH2_HOST=oauth2.xyz.com \
            -d zzzmahesh/conductorboot:latest

    3) Running with external MySQL, Elasticsearch & ADFS

        docker run --name conductor-boot -p 8080:8080 \
            -e SPRING_PROFILES_ACTIVE=basic,mysql,external-elasticsearch,external-adfs,security,conductor \
            -e MYSQL_DATABASE_HOST=172.x.x.x \
            -e MYSQL_DATABASE_PORT=3306 \
            -e MYSQL_USER=conductor \
            -e MYSQL_PASSWORD=conductor \
            -e ELASTICSEARCH_HOST=172.x.x.x \
            -e ELASTICSEARCH_PORT=9200 \
            -e ADFS_CLIENT_ID=123-ABC_CLIENT_ID \
            -e ADFS_RESOURCE=123-ABC_RESOURCE \
            -e ADFS_HOST=adfs.xyz.com \
            -d zzzmahesh/conductorboot:latest

    4) Changing the default embedded profile configurations

        docker run --name conductor-boot -p 8080:8080 \
            -e SPRING_PROFILES_ACTIVE=basic,mariadb4j,embedded-elasticsearch,embedded-oauth2,security,conductor \
            -e MYSQL_USER=conductor \
            -e MYSQL_PASSWORD=conductor \
            -e ELASTICSEARCH_PORT=9200 \
            -e ELASTICSEARCH_DATA_DIR=/appln/data/elasticsearch
            -e ELASTICSEARCH_RESOURCE_DIR=/appln/bin/elasticsearch
            -e MARIADB4J_DIR=/appln/data/mariadb4j
            -d zzzmahesh/conductorboot:latest        

    Similarly any combination of profile and configurations can be used.

#### All the below mentioned configurables / properties (under Available Profiles section) can be passed as Docker Container environment variables and will be set accordingly.

Available configurables - shown below with default values.

    ELASTICSEARCH_HOST localhost
    ELASTICSEARCH_PORT 9200
    ELASTICSEARCH_DATA_DIR /appln/data/elasticsearch
    ELASTICSEARCH_RESOURCE_DIR /appln/bin/elasticsearch
    ADFS_CLIENT_ID POPULATE_CLIENT_ID
    ADFS_RESOURCE POPULATE_ADFS_RESOURCE
    ADFS_HOST POPULATE_ADFS_HOST
    OAUTH2_HOST POPULATE_EXTERNAL_OAUTH2_HOST
    MYSQL_DATABASE conductor
    MYSQL_USER conductor
    MYSQL_PASSWORD conductor
    MYSQL_DATABASE_HOST localhost
    MYSQL_DATABASE_PORT 3306
    MARIADB4J_DIR /appln/data/mariadb4j
    SPRING_PROFILES_ACTIVE basic,mariadb4j,embedded-elasticsearch,embedded-oauth2,security,conductor

Also the below mentioned paths / volumes can be mounted to docker container for persistence, in case of embedded profiles (mariadb4j and embedded-elasticsearch)
    
    ELASTICSEARCH_DATA_DIR /appln/data/elasticsearch
    ELASTICSEARCH_RESOURCE_DIR /appln/bin/elasticsearch
    MARIADB4J_DIR /appln/data/mariadb4j

## Run Conductor Boot

		cd <to project root folder>/target
		
	Below command will start the Conductor Boot Wrapper with Embedded MariaDB4J as Database and Embedded OAuth2 security as well as persistent Embedded Elasticsearch
		java -jar conductor-boot-${conductor.version}.jar

    The profiles included by default are
        - basic
        - conductor
        - mariadb4j
        - embedded-elasticsearch
        - embedded-oauth2
        - security

### Available Profiles

    1) basic
        This profile holds the basic startup configuration needed for the Spring Boot Wrapper.

    2) conductor
        This profile enables the Integrated Conductor Server startup.
        Most of it is pre configured and will not require changes. For more details refer to application-conductor.yml file.

    3) mariadb4j
        This profile configures and startsup Embedded MariaDB4J and the database details are passed on to Integrated Conductor Server.
        By default, the database is non-persistent. Set MARIADB4J_DIR parameter for enabling a persistent database. More details are discussed in confgirations section.
        Configurations available are as below. Shown are default values.
            MYSQL_DATABASE: conductor
            MYSQL_USER: conductor
            MYSQL_PASSWORD: conductor
            
            # This directory has to be explicitly set to let Embedded MariaDB know the master persistence location.
            # Use embedded/persistence/mariadb4j to have a common persistence location for all embedded components.
            # Default is NONE
            MARIADB4J_DIR: NONE

    4) mysql
        This profile configures the external MySQL database details passed on to Integrated Conductor Server.
        Configurations available are as below. Shown are default values.
            MYSQL_DATABASE_HOST: localhost
            MYSQL_DATABASE_PORT: 3306
            MYSQL_DATABASE: conductor
            MYSQL_USER: conductor
            MYSQL_PASSWORD: conductor

    5) embedded-elasticsearch
        This profile configures and startsup Embedded Elasticsearch and the connection details are passed on to Integrated Conductor Server.
        By default, the elasticsearch is non-persistent. Set ELASTICSEARCH_DATA_DIR parameter for enabling a persistent elasticsearch. 
        Configurations available are as below. Shown are default values.
            ELASTICSEARCH_HOST: localhost
            ELASTICSEARCH_PORT: 9200
            
            # Use embedded/persistence to have a common persistence location for all embedded components.
            # Default is NONE
            ELASTICSEARCH_DATA_DIR: NONE
            
            # Use embedded/resources to have a common resources location for all embedded components.
            # Default is NONE
            ELASTICSEARCH_RESOURCE_DIR: NONE

    6) external-elasticsearch
        This profile configures the external Elasticsearch details passed on to Integrated Conductor Server.
        Configurations available are as below. Shown are default values.
            ELASTICSEARCH_HOST: localhost
            ELASTICSEARCH_PORT: 9200

    7) embedded-oauth2 (cannot be selected alone, will need security & mariadb4j/mysql profiles as well)
        This profile configures and startsup Embedded OAuth2 and the Integrated Conductor Server API's are protected / secure.
        By default, the roles, users and credentials mentioned below in Roles section are configured. Change them using APIs. 
        No configurations to expose.

    8) external-oauth2 (cannot be selected alone, will need security profile as well)
        This profile configures the external OAuth2 and the Integrated Conductor Server API's are protected / secure.
        The roles specified below, in the Roles section, need to be configured in the corresponding OAUTH2 provider system.
        Configurations available are as below. Shown are default values.
            OAUTH2_HOST: github.maheshyaddanapudi.com

    9) external-adfs (cannot be selected alone, will need & mariadb4j/mysql profiles as well)
        This profile configures the external OAuth2 and the Integrated Conductor Server API's are protected / secure.
        The roles specified below, in the Roles section, need to be configured in the corresponding ADFS provider system.
        Configurations available are as below. Shown are default values.
            ADFS_CLIENT_ID: ABCD-1234_CLIENT
            ADFS_RESOURCE: ABCD-1234_RESOURCE
            ADFS_HOST: idm.github.maheshyaddanapudi.com

    10) security (useless be selected alone, will need either embedded-oauth2 or external-oauth2 or external-adfs)
        No configurations to expose.
        If the Roles specified below cannot be configured, refer application-security.yml and edit security.oauth2.resource.mapping JSON property accordingly. Its self explanatory.

    Default Startup will include - basic, conductor, mariadb4j (without-persistent-storage), embedded-elasticsearch (without-persistent storage), embedded-oauth2 & security.
        This necessarily just overrides the in memory database and integrated elasticsearch pre-existing in conductor server and will essentially be useless unless persistence is configured.

    For more detailed properties, refer to application-{profile}.yml file as per the required profile properties.

## Application URLs

		1) HTTPS
			a) https://localhost:8080/openapi-ui - To access the Swagger pertaining to APIs for OAUTH
			b) https://localhost:8080/index.html - To access the Swagger pertaining to APIs for Conductor, as https redirection doesn't work to index.html
			
		2) HTTP
			a) http://localhost:8080/openapi-ui - To access the Swagger pertaining to APIs for OAUTH
			b) http://localhost:8080/ - To access the Swagger pertaining to APIs for Conductor, as redirection is taken care to index.html

## Motivation

To avoid the pain points of

      • Hosting OAuth2 Server for securing Conductor Server APIs
      
      • Housing external database engine for Conductor Server persistence unit

      • Housing external elasticsearch engine for Conductor Server persistence unit
      

## Tech / Framework used
      
      --> Docker Image to host the Jar. (Will be added soon - with pre built jar inside docker image)
	  			
      --> Spring Boot Wrapper
			
            • Spring Cloud OAuth2
            
            • MariaDB4j
            
            • Flyway Initialiser
            
            • Liquibase Initialiser
            
            • Netflix Conductor Server with MySQL as persistence option (with pre-existing flyway migration).
			
            • Netflix Zuul Proxy Server

            • Elasticsearch

## Authentication Process

##### Note
	
  • HTTPS is NOT enabled by default (with self signed certificate). Change the property to true and HTTPS will be enabled
  
	  		server:
	  		  ssl:
	  		    enabled: true
	  		    
	  		--> To view the Swagger URLs in browser - In Chrome, you can use url chrome://flags/#allow-insecure-localhost to allow insecure localhost. Refer to https://stackoverflow.com/questions/7580508/getting-chrome-to-accept-self-signed-localhost-certificate
	  			And	  		
	  		--> To call APIs in Postman - There is an option in Postman, in the settings, turn off the SSL certificate verification option 
	  		
	  		NOTE: For accessing Condcutor Swagger on HTTPS https://localhost:8080/index.html should be used as a default redirection doesnt work.
  
  • HTTP only mode - Change the property to true and HTTPS will be disabled
  
	  		server:
	  		  ssl:
	  		    enabled: false
	  		    
NOTE: For accessing Condcutor Swagger on HTTPS https://localhost:8080/ will be sufficient as redirection works without https.


##### Generating a self-signed certificate (Not Recommended - Developer use only)

Certificate for HTTPS

	We can store as many numbers of key-pair in the same keystore each identified by a unique alias.
	
	  • For generating our keystore in a JKS format, we can use the following command:
			keytool -genkeypair -alias my -keyalg RSA -keysize 2048 -keystore my.jks -validity 3650
	  
	  • It is recommended to use the PKCS12 format which is an industry standard format. So in case we already have a JKS keystore, we can convert it to PKCS12 format using the following command:
    		keytool -importkeystore -srckeystore my.jks -destkeystore my.p12 -deststoretype pkcs12
	  
	  Place the generated my.p12 file in src/main/resources/keystore
	  

Certificate for OAUTH2

	
	  • For generating our keystore in a JKS format, we can use the following command:
	  	  keytool -genkeypair -alias jwt -keyalg RSA -keypass password -keystore jwt.jks -storepass password
	  	  
	  • It is recommended to use the PKCS12 format which is an industry standard format. So in case we already have a JKS keystore, we can convert it to PKCS12 format using the following command:
	  	  keytool -importkeystore -srckeystore jwt.jks -destkeystore jwt.jks -deststoretype pkcs12
	  	  
	  • To view the private and public key generated and to extract the public key, we can use the following command:
    	  	  keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey
	
        	  Copy everything including the tags from, as shown below and paste it into the applications.yml as a property
          	  -----BEGIN PUBLIC KEY-----
          	  .....
          	  -----END PUBLIC KEY-----
	  	 
	  Place the generated jwt.jks file in src/main/resources

## Integrated Conductor Server Authentication & Authorization - Roles

##### Category of APIs available at Conductor level.
	
		☐  Event Services - For Event Handling APIs
		
		☐  Workflow Management - For workflow executing, rerun, terminate, pause etc. functionalities.
		
		☐  Metadata Management - Workflow or task creation / updation / deletion etc. functionalities.
		
		☐  Health Check - Ignore for now
		
		☐  Admin - Ignore for now
		
		☐  Workflow Bulk Management - For workflow bulk executing, rerun, terminate, pause etc. functionalities.
		
		☐  Task Management - For task executing, rerun, terminate, pause etc. functionalities.
		

##### Roles that are mapped to APIs
		
		role_conductor_super_manager
		role_conductor_super_viewer
		role_conductor_core_manager
		role_conductor_core_viewer
		role_conductor_execution_manager
		role_conductor_execution_viewer
		role_conductor_event_manager
		role_conductor_event_view
		role_conductor_metadata_manager
		role_conductor_metadata_viewer
		role_conductor_metadata_workflow_manager
		role_conductor_metadata_workflow_viewer
		role_conductor_metadata_taskdefs_manager
		role_conductor_metadata_taskdefs_viewer
		role_conductor_workflow_manager
		role_conductor_workflow_viewer
		role_conductor_task_manager
		role_conductor_task_viewer

##### Technical mapping to roles.
		
		☐  All Manager roles will be able to Create/Update/Delete the mentioned API implemented functionalities.
		
		☐  All Viewer roles will be able to View existing API implemented functionalities.
		
		☐  A default user for each role is created while the flyway migration happens and the username is same as the role (example - 'role_conductor_super_manager') and the password is 'password'
	  		
			1) role_conductor_super_manager - POST / PUT / DELETE
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Health Check
			    Admin
			    Workflow Bulk Management
			    Task Management
			
			2) role_conductor_super_viewer - GET
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Health Check
			    Admin
			    Workflow Bulk Management
			    Task Management
			
			3) role_conductor_core_manager - POST / PUT / DELETE
			    Event Services
			    Workflow Management
			    Metadata Management
			    Workflow Bulk Management
			    Task Management
			
			4) role_conductor_core_viewer - GET
				
			    Event Services
			    Workflow Management
			    Metadata Management
			    Workflow Bulk Management
			    Task Management
			
			5) role_conductor_execution_manager - POST / PUT / DELETE
				
			    Event Services
			    Workflow Management
			    Task Management
			
			6) role_conductor_execution_viewer - GET
				
			    Event Services
			    Workflow Management
			    Task Management
			
			7) role_conductor_event_manager - POST / PUT / DELETE
				
			  	Event Services
			  
			8) role_conductor_event_viewer - GET
			  	
			  	Event Services
			
			9) role_conductor_metadata_manager - POST / PUT / DELETE
				
			    Metadata Management
			
			10) role_conductor_metadata_viewer - GET
			
			    Metadata Management
			
			11) role_conductor_workflow_manager - POST / PUT / DELETE
				
			  	Workflow Management
			  
			12) role_conductor_workflow_viewer - GET
			  	
			  	Workflow Management
			
			13) role_conductor_task_manager - POST / PUT / DELETE
				
			  	Task Management
			  
			14) role_conductor_task_viewer - GET
				
			  	Task Management
			  	
## OAuth2 / ADFS Authentication & Authorization - Roles

##### Category of APIs available at OAuth level.
	
		☐  Admin Services - For Admin APIs - to onboard new clients and users, update or delete users / clients, reset client secret / user password.
		
		☐  Client Services - For Client Admin APIs - to onboard new users, update or delete users, reset client secret / user password.
		
		☐  User Services - For User APIs - to get basic principal, update details / reset password.

##### Roles that are mapped to APIs
		
		role_oauth_super_admin
		role_oauth_client_admin
		
##### Technical mapping to roles.
		
		☐  All Manager roles will be able to Create/Update/Delete the mentioned API implemented functionalities.
		
		☐  All Viewer roles will be able to View existing API implemented functionalities.
		
		☐  A default user for each role is created while the flyway migration happens and the username is same as the role (example - 'role_conductor_super_manager') and the password is 'password'
	  		
			1) role_oauth_super_admin - GET / POST / PUT / PATCH / DELETE
				
			    Admin Services
			    User Services
			
			2) role_oauth_client_admin - GET / POST / PUT / PATCH / DELETE
				
			    Client Services
			    User Services
			
##### NOTE: All the User Services will be OPEN for Conductor Roles as well.
