# Spring Boot Wrapper for Netflix Conductor Server 
## With Embedded persistent Security(OAuth2), Database(MariaDB) and Elasticsearch(V5.5.2) 

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_conductor-boot)

Before starting, for details on Netflix Conductor, refer to <a href="http://netflix.github.io/conductor/">Conductor Documentation</a>


##### Note

      • Only MySQL is supported as external Database
      
      				Or
      
      • Embedded MariaDB4j is supported, which is persistent and data is not lost, thus eliminating the need for external


## Overview

The idea is to build a single Spring Boot Jar with the following 

      • Micro Services Orchestration - by Conductor Server
      
      • OAuth2 Authentication & Authorization - by Auth & Resource Servers
      
      • Zuul Gateway - by Netflix Zuul Proxy for acting as proxy to Conductor Server APIs
      
      • Optional Embedded Persistent MariaDB4j



## Build Status 

Circle CI : [![maheshyaddanapudi](https://circleci.com/gh/maheshyaddanapudi/conductor-boot.svg?style=shield)](https://circleci.com/gh/maheshyaddanapudi/conductor-boot)

Java CI: ![Java CI with Maven](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Java%20CI%20with%20Maven/badge.svg?branch=main)

Travis CI: [![Build Status](https://travis-ci.com/maheshyaddanapudi/conductor-boot.svg?branch=main)](https://travis-ci.com/maheshyaddanapudi/conductor-boot)

CodeQL: ![CodeQL](https://github.com/maheshyaddanapudi/conductor-boot/workflows/CodeQL/badge.svg?branch=main)

Docker : ![Docker](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Docker/badge.svg?branch=main)

Docker Image CI: ![Docker Image CI](https://github.com/maheshyaddanapudi/conductor-boot/workflows/Docker%20Image%20CI/badge.svg?branch=main)

SonarQube: [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=maheshyaddanapudi_conductor-boot&metric=alert_status)](https://sonarcloud.io/dashboard?id=maheshyaddanapudi_conductor-boot)

## Build using maven

		cd <to project root folder>
		mvn clean install
		
	The maven build should place the conductor-boot-wrapper-${conductor.version}-secure.jar inside the target folder.

## Run the Boot Wrapper

		cd <to project root folder>/target
		
	Below command will start the Conductor Boot Wrapper with Embedded MariaDB4J as Database
		java -jar conductor-boot-wrapper-${conductor.version}-secure.jar

	Below command will start the Conductor Boot Wrapper with external MySQL as Database. Please ensure you have created a config file for DB connection etc as shown in application-mysql.yml
		java -Dspring.profiles.active=mysql -Dspring.config.location=config/application-mysql.yml -jar conductor-boot-wrapper-${conductor.version}-secure.jar

	Note: -Dspring.config.location points to the path of the external mysql dtabase configuration enhanced application.yml. Sample is available in src/main/resources/application-mysql.yml 

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
      

## Tech/framework used
      
      --> Docker Image to host the Jar. (Will be added soon - with pre built jar inside docker image)
	  			
      --> Spring Boot Wrapper
			
            • Spring Cloud OAuth2
            
            • MariaDB4j
            
            • Flyway Initialiser
            
            • Liquibase Initialiser
            
            • Netflix Conductor Server with MySQL as persistence option (with pre-existing flyway migration).
			
            • Netflix Zuul Proxy Server

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


##### Generating a self signed certificate

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

## Conductor Authentication & Authorization - Roles

##### Category of APIs available at Conductor level.
	
		☐  Event Services - For Event Handling APIs
		
		☐  Workflow Management - For workflow executing, rerun, terminate, pause etc. functionalities.
		
		☐  Metadata Management - Workflow or task creation / updation / deletion etc. functionalities.
		
		☐  Health Check - Ignore for now
		
		☐  Admin - Ignore for now
		
		☐  Workflow Bulk Management - For workflow bulk executing, rerun, terminate, pause etc. functionalities.
		
		☐  Task Management - For task executing, rerun, terminate, pause etc. functionalities.
		

##### Roles that are to be mapped to APIs
		
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
			  	
## OAuth Authentication & Authorization - Roles

##### Category of APIs available at OAuth level.
	
		☐  Admin Services - For Admin APIs - to onboard new clients and users, update or delete users / clients, reset client secret / user password.
		
		☐  Client Services - For Client Admin APIs - to onboard new users, update or delete users, reset client secret / user password.
		
		☐  User Services - For User APIs - to get basic principal, update details / reset password.

##### Roles that are to be mapped to APIs
		
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
