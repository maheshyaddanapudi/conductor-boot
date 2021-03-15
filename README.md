# Conductor Boot - Complete Containerized Configuration

### Pre-Requisites

For setting up the integrated, containerized environment, the following should be installed on the target machine.
    
    1. Docker Desktop / Docker


## Target State
  
Each of the below functionalities would be hosted into separate containers - with persistence & security
  
  1. OAuth 2.0 Provider - Component / Network : keycloak
  
     1.1 Postgres Database - Container : postgres

     1.2 Keycloak OAuth 2.0 Server With Web UI - Container : keycloak

  2. Log Aggregation - Component / Networks : kibana & conductor-boot
  
     2.1. Elasticsearch - Container : elasticsearch

     a) Elasticsearch will be part of two networks kibana & conductor-boot networks

     b) conductor-boot network will be used for persisting conductor workflow execution data and the log aggregation indexes as well

     c) kibana network will be used for displaying the logs from elasticsearch on the Kibana Web UI.

     d) This is done in an attempt to segregate both the core and log aggregator component network for better component / container level security.

     2.2. Logspout - Container : logspout

     a) Logspout will be part of conductor-boot network

     b) conductor-boot network will be used for forwarding the log data from conductor container to logstash.

     c) This is done in an attempt to segregate both the core and log aggregator component network for better component / container level security.

     2.3. Logstash - Container : logstash

     a) Logstash will be part of conductor-boot network

     b) conductor-boot network will be used for forwarding the log data to elasticsearch by logstash. Thus ultimately the logs from the conductor container are frowarded as conductor --> logspout (Logs are formatted then forwarded) --> logstash --> elasticsearch --> kibana (display)

     c) This is done in an attempt to segregate both the core and log aggregator component network for better component / container level security.

     2.4. Kibana - Container : kibana

     a) Kibana will be part of kibana network

     b) kibana network will be used for displaying the log data from elasticsearch by kibana. 

     c) This is done in an attempt to segregate both the core and log aggregator component network for better component / container level security.

  3. Core Orchestration - Component / Network : conductor-boot
   
     3.1. Database - Container : mysql

     2.2. Conductor - Container : conductor-boot

     a) Conductor Boot is a Spring Boot Wrapper with Integrated Conductor Server and with extra features like OAuth 2.0 or ADFS security. Hence this will be a perfect component for core orchestration.
     
## Docker Compose - Start-Up

All the images needed to set-up the environment are already available on dockerhub & keycloak on quay repositories.

Here is the list of docker images that will be used.

  1. keycloak --> quay.io/keycloak/keycloak:latest (from quay)
  2. postgres --> postgres:latest (from dockerhub)
  3. mysql --> mysql:5.7 (from dockerhub)
  4. elasticsearch --> elasticsearch:5.6 (from dockerhub)
  5. kibana --> kibana:5.6.16 (from dockerhub)
  6. logstash --> logstash:5.6.8 (from dockerhub)
  7. logspout --> gliderlabs/logspout:v3 (from dockerhub)
  8. conductorboot --> zzzmahesh/conductorboot:latest (from dockerhub)

Navigate into the cloned directory and Create container persistence directories, which will be used to persist container data. 
In other words, to map container volumes to host machine.
  a.	cd conductor-boot
  b.	mkdir container 
  c.	mkdir container/persistence 
  d.	mkdir container/persistence/mysql 
  e.	mkdir container/persistence/postgres 
  f.	mkdir container/persistence/elasticsearch


There are a set of variants available on this Github repository as listed below. 

  1. docker-compose-suite-sequential-startup.yml
    a. If your system has 8 GB RAM or even if higher, the system resources are occupied, then use this.
    
  2. docker-compose-suite-medium-startup.yml
    a. If your system has 8 GB RAM or higher and has free system resources, then use this.

  3. docker-compose-suite-parallel-startup.yml
    a. If your system has 16 GB RAM or higher and has free system resources, then use this.
    b. Let's choose this as this has the best startup time.

Here is the docker-compose.yml file

    version: '2.2'

    services:
      postgres:
          image: postgres
          volumes:
            - postgres_data:/var/lib/postgresql/data
          environment:
            POSTGRES_DB: keycloak
            POSTGRES_USER: keycloak
            POSTGRES_PASSWORD: Keycloak@1234
            LOGSPOUT: ignore
          mem_limit: "512000000"
          ports:
            - 5432:5432
          healthcheck:
            test: ["CMD-SHELL", "pg_isready -U keycloak -d keycloak"]
            interval: 30s
            timeout: 15s
            retries: 10
          networks:
            keycloak-nw:
              aliases:
                - postgres
      keycloak:
          image: quay.io/keycloak/keycloak:latest
          healthcheck:
            test: ["CMD", "curl", "-I", "-XGET", "http://localhost:8080/auth/realms/master"]
            interval: 30s
            timeout: 30s
            retries: 15
          mem_limit: "1024000000"
          environment:
            DB_VENDOR: POSTGRES
            DB_ADDR: postgres
            mysql_dataBASE: keycloak
            DB_USER: keycloak
            DB_SCHEMA: public
            DB_PASSWORD: Keycloak@1234
            KEYCLOAK_USER: admin
            KEYCLOAK_PASSWORD: Admin@1234
            LOGSPOUT: ignore
            # Uncomment the line below if you want to specify JDBC parameters. The parameter below is just an example, and it shouldn't be used in production without knowledge. It is highly recommended that you read the PostgreSQL JDBC driver documentation in order to use it.
            #JDBC_PARAMS: "ssl=true"
          ports:
            - 9990:8080
          depends_on:
            postgres:
              condition: service_healthy
          networks:
            keycloak-nw:
              aliases:
                - keycloak
            conductor-boot-nw:
              aliases:
                - keycloak
      elasticsearch:
          image: elasticsearch:5.6
          restart: on-failure
          networks:
            conductor-boot-nw:
              aliases:
                - elasticsearch
            kibana-nw:
              aliases:
                - elasticsearch
          ports:
            - 9200:9200
            - 9300:9300
          healthcheck:
            test: ["CMD", "curl","-I" ,"-XGET", "http://localhost:9200/_cat/health"]
            interval: 30s
            timeout: 30s
            retries: 15
          mem_limit: "3096000000"
          volumes:
            - es_data:/var/lib/elasticsearch
      mysql:
          image: mysql:5.7
          restart: on-failure
          networks:
            conductor-boot-nw:
              aliases:
                - mysql
          ports:
            - 3306:3306
            - 33060:33060
          environment:
            MYSQL_ROOT_PASSWORD: Root@1234
            MYSQL_DATABASE: conductor
            MYSQL_USER: conductor
            MYSQL_PASSWORD: Conductor@1234
            MYSQL_INITDB_SKIP_TZINFO: NONE
            LOGSPOUT: ignore
          healthcheck:
            test: [ "CMD-SHELL", 'mysqladmin ping' ]
            interval: 60s
            timeout: 30s
            retries: 15
          mem_limit: "512000000"
          volumes:
            - mysql_data:/var/lib/mysql
      conductor-boot:
          image: zzzmahesh/conductorboot:latest
          restart: on-failure
          depends_on:
            mysql:
              condition: service_healthy
            elasticsearch:
              condition: service_healthy
            keycloak:
              condition: service_healthy
          networks:
            conductor-boot-nw:
              aliases:
                - conductor-boot
          ports:
            - 8080:8080
          environment:
            MYSQL_DATABASE_HOST: mysql
            MYSQL_DATABASE_PORT: 3306
            MYSQL_DATABASE: conductor
            MYSQL_USER: conductor
            MYSQL_PASSWORD: Conductor@1234
            ELASTICSEARCH_URL: http://elasticsearch:9200
            OAUTH2_USER_INFO_URL: http://keycloak:9990/auth/realms/conductor/protocol/openid-connect/userinfo
            SPRING_PROFILES_ACTIVE: basic,mysql,external-elasticsearch,external-oauth2,security,conductor
          healthcheck:
            test: ["CMD", "curl","-I" ,"-XGET", "http://localhost:8080/api/health"]
            interval: 60s
            timeout: 30s
            retries: 15
          mem_limit: "1536000000"
      kibana:
          image: kibana:5.6.16
          restart: on-failure
          links:
            - elasticsearch
          environment:
            ELASTICSEARCH_URL: http://elasticsearch:9200
            LOGSPOUT: ignore
          ports:
            - 5601:5601
          healthcheck:
            test: [ "CMD", "curl","-I", "-XGET", "http://localhost:5601/status" ]
            interval: 60s
            timeout: 30s
            retries: 15
          depends_on:
            elasticsearch:
              condition: service_healthy
          mem_limit: "512000000"
          networks:
            kibana-nw:
              aliases:
                - kibana
      logstash:
          image: logstash:5.6.8
          restart: on-failure
          environment:
            STDOUT: "true"
            LOGSPOUT: ignore
            http.host: 0.0.0.0
          ports:
            - 5000:5000
            - 9600:9600
          links:
            - elasticsearch
          depends_on:
            elasticsearch:
              condition: service_healthy
          command: 'logstash -e "input { udp { port => 5000 } } filter { grok { match => { message => \"\A\[%{LOGLEVEL:LOG_LEVEL}%{SPACE}]%{SPACE}%{TIMESTAMP_ISO8601:LOG_TIMESTAMP}%{SPACE}%{NOTSPACE:JAVA_CLASS}%{SPACE}-%{SPACE}%{GREEDYDATA:LOG_MESSAGE}\" } } } output { elasticsearch { hosts => elasticsearch } }"'
          mem_limit: "512000000"
          networks:
            conductor-boot-nw:
              aliases:
                - logstash
      logspout:
          image: gliderlabs/logspout:v3
          restart: on-failure
          command: 'udp://logstash:5000'
          links:
            - logstash
          volumes:
            - '/var/run/docker.sock:/tmp/docker.sock'
          environment:
            LOGSPOUT: ignore
          depends_on:
            elasticsearch:
              condition: service_healthy
            logstash:
              condition: service_started
          mem_limit: "512000000"
          networks:
            conductor-boot-nw:
              aliases:
                - logspout
    volumes:
      postgres_data:
        driver: local
        driver_opts:
          type: none
          device: $PWD/container/persistence/postgres
          o: bind
      es_data:
        driver: local
        driver_opts:
          type: none
          device: $PWD/container/persistence/elasticsearch
          o: bind
      mysql_data:
        driver: local
        driver_opts:
          type: none
          device: $PWD/container/persistence/mysql
          o: bind
    networks:
      keycloak-nw:
      conductor-boot-nw:
      kibana-nw:

To spin up the containers, let fire the docker-compose command. Replace the docker-compose-suite-XXX-startup.yml in the below command accordingly.

    docker-compose -f docker-compose-suite-parallel-startup.yml up -d
    
  Note: The entire start-up might take up to 15 minutes, depending on the host machine configuration.
  
After the start-up is successfully completed, the output of the above start-up command should look similar to below.

    Creating conductor-boot_postgres_1 ... done
    Creating conductor-boot_keycloak_1 ... done
    Creating conductor-boot_mysql_1         ... done
    Creating conductor-boot_elasticsearch_1 ... done
    Creating conductor-boot_logstash_1       ... done
    Creating conductor-boot_kibana_1         ... done
    Creating conductor-boot_conductor-boot_1 ... done
    Creating conductor-boot_logspout_1       ... done

Verify the container status with "docker ps"

    docker ps
    
    CONTAINER ID   IMAGE                              COMMAND                  CREATED          STATUS                            PORTS                                              NAMES
    ee0a606c9161   gliderlabs/logspout:v3             "/bin/logspout udp:/…"   5 minutes ago    Up 5 minutes                      8000/tcp                                           conductor-boot_logspout_1
    471fdcb6805f   zzzmahesh/conductorboot:latest     "/bin/bash /appln/sc…"   5 minutes ago    Up 5 minutes (healthy)            0.0.0.0:8080->8080/tcp                             conductor-boot_conductor-boot_1
    a2270d4c2113   kibana:5.6.16                      "/docker-entrypoint.…"   5 minutes ago    Up 5 minutes (health: starting)   0.0.0.0:5601->5601/tcp                             conductor-boot_kibana_1
    2f05a5d26e1d   logstash:5.6.8                     "/docker-entrypoint.…"   5 minutes ago    Up 5 minutes                      0.0.0.0:5000->5000/tcp, 0.0.0.0:9600->9600/tcp     conductor-boot_logstash_1
    984470ea0c5d   elasticsearch:5.6                  "/docker-entrypoint.…"   8 minutes ago    Up 7 minutes (healthy)            0.0.0.0:9200->9200/tcp, 0.0.0.0:9300->9300/tcp     conductor-boot_elasticsearch_1
    d68f5b943ba1   mysql:5.7                          "docker-entrypoint.s…"   8 minutes ago    Up 7 minutes (healthy)            0.0.0.0:3306->3306/tcp, 0.0.0.0:33060->33060/tcp   conductor-boot_mysql_1
    db7ce51c4e11   quay.io/keycloak/keycloak:latest   "/opt/jboss/tools/do…"   11 minutes ago   Up 11 minutes (healthy)           8443/tcp, 0.0.0.0:9990->8080/tcp                   conductor-boot_keycloak_1
    d4c2a4cc9101   postgres                           "docker-entrypoint.s…"   14 minutes ago   Up 14 minutes (healthy)           0.0.0.0:5432->5432/tcp                             conductor-boot_postgres_1

## Verification : Post Start-Up

Open browser (preferably Google Chrome) and verify all the UI URLs are up and running.

  a.	http://localhost:9990 : Keycloak Base URL
  
    i.	Click on the option Administration Console / Admin Console
   
    ii.	Use the username and password from the YML file : default admin/Admin@1234
    
  ![image](https://user-images.githubusercontent.com/24351133/111174877-d566b500-85cd-11eb-9d0a-0ac96c6f63f9.png)

  ![image](https://user-images.githubusercontent.com/24351133/111174969-ee6f6600-85cd-11eb-979e-dd04c3dd5814.png)

  b.	http://localhost:8080 : Integrated Conductor Swagger URL

   ![image](https://user-images.githubusercontent.com/24351133/111175089-0fd05200-85ce-11eb-8bf2-c63f4cae42cf.png)

  c.	http://localhost:5601 : Kibana Logs Viewer URL

   ![image](https://user-images.githubusercontent.com/24351133/111175125-18c12380-85ce-11eb-86e6-44dc03836b74.png)

## Configuration : Post Start-Up - Initial 

The below initial configurations , one-time, have to be configured.

### Keycloak Configuration : Security / OAuth 2.0

1.	Create new Realm as shown below
  
     a.	Add Realm
     
      ![image](https://user-images.githubusercontent.com/24351133/111176433-596d6c80-85cf-11eb-9d2c-a2614219e215.png)
      
     b. Enter realm details and Click Save
    
        i.	Name: conductor
      
      ![image](https://user-images.githubusercontent.com/24351133/111176477-62f6d480-85cf-11eb-800b-e9fe6b95b195.png)
      
      
    c.	Optional - Key-in the below details
    
      i.	Display Name: Conductor Realm
   
      ii.	Click Save

      ![image](https://user-images.githubusercontent.com/24351133/111176551-71dd8700-85cf-11eb-82af-a6f508854c08.png)

2.	Create a Client Secret at REALM Level. 
  
     a.	From the same dropdown where “Add Realm” was selected previously, switch back to “master” realm by selecting it. And then click on Clients from the side menu.

      ![image](https://user-images.githubusercontent.com/24351133/111191345-fa632400-85dd-11eb-8370-6f13077ea1ea.png)
      
     b.	Click on Clients and Select “conductor-realm”
     
      ![image](https://user-images.githubusercontent.com/24351133/111191667-5037cc00-85de-11eb-9cfc-8a70d0c5c90b.png)
      
     c.	Scroll all the way to the bottom of the screen and select the last section “Authentication Flow Overrides”
     
        i.	Set Browser Flow as “browser”
        ii.	Set Direct Grant Flow as “direct grant”
          
     d.	Click Save
  
     e.	The page should auto refresh and a new tab “Credentials” will be visible on the top of the page. Select this tab and make note of the secret displayed on screen.
  
      ![image](https://user-images.githubusercontent.com/24351133/111192967-a6f1d580-85df-11eb-813c-9b614428f7ea.png)
      
      ![image](https://user-images.githubusercontent.com/24351133/111193467-34352a00-85e0-11eb-8ff5-86fda19307e7.png)
      
      
3.	Switch back to Conductor Realm again and Configure new Client and User.

     a.	From the same dropdown where “Add Realm” was selected previously, switch back to “conductor” realm by selecting it.
     
     b. Click on Clients from the Side Menu and Click "Create" to add new client
     
      ![image](https://user-images.githubusercontent.com/24351133/111193980-c3424200-85e0-11eb-91ee-645832074466.png)
      
     c.	Key-in the new client details
      
        i.	Client ID : conductor_user_client
        ii.	Client Protocol : openid-connect
        iii.	Root URL : http://localhost:9990/ 
        
      d.	Click Save
     
      ![image](https://user-images.githubusercontent.com/24351133/111194537-511e2d00-85e1-11eb-9012-3120892575ab.png)
      
      e.	Set the Mapper configuration which would return the user role in their userinfo response. Click on “Mappers” and “Add Builtin”

      f.	Select “client roles” and “realm roles” from the checklist. 
      
      g.	Click “Add Selected”
      
      ![image](https://user-images.githubusercontent.com/24351133/111199235-6ba6d500-85e6-11eb-9897-36b648907f90.png)
      
      ![image](https://user-images.githubusercontent.com/24351133/111199343-86794980-85e6-11eb-8ede-b7b06205306a.png)
      
      h.	The selected options will now be visible under “Mappers” tab.
      
      i.	Select each one and repeat the below steps (first “client roles” and then “realm roles”)

        i.	Enable “Add to userinfo” and Save
      
      ![image](https://user-images.githubusercontent.com/24351133/111199462-a7419f00-85e6-11eb-9426-b8769420197b.png)
      
      ![image](https://user-images.githubusercontent.com/24351133/111199491-af014380-85e6-11eb-951a-b2da72e73849.png)
      
      j. Navigate to Roles tab and click on “Add Role” to add a new role.

      k.	Key-in the below details for role creation
        
        i.	Role Name : role_conductor_super_manager
        ii.	Description (Optional) : Conductor Admin / Super Manager Access
        
      ![image](https://user-images.githubusercontent.com/24351133/111197200-35685600-85e4-11eb-841c-e397ada7049b.png)
      
      ![image](https://user-images.githubusercontent.com/24351133/111197283-4dd87080-85e4-11eb-8de1-3088c4323aee.png)

      l. Click on Users from the side menu and Click "Add user"
      
      m. Key-in basic user details for new user creation and click Save
      
      ![image](https://user-images.githubusercontent.com/24351133/111201294-a01b9080-85e8-11eb-81d7-ae1887768533.png)
      
      ![image](https://user-images.githubusercontent.com/24351133/111201413-bfb2b900-85e8-11eb-955c-7b86bef8277e.png)
      
      n. Navigate to Credentials tab and set the user password. Disable "Temporary Password" (for the sake of this demo)
      
      ![image](https://user-images.githubusercontent.com/24351133/111201525-dbb65a80-85e8-11eb-8e11-a5c783602ce1.png)
      
      o. Navigate to Role Mappings tab, in the Client Roles drop-down, select newly created client "conductor_user_client".
      
      p. From the "Available Roles", select newly created role "role_conductor_super_manager" and click "Add selected".
      
      ![image](https://user-images.githubusercontent.com/24351133/111201640-f7b9fc00-85e8-11eb-9a58-ba5c6a48dfd4.png)
      
The security configuration is complete.
      
### Conductor API Security : Verification

As the user is now created and can be used to securely login and access Conductor APIs, verification can be done as below.

#### Using Postman Rest Client

  1) Perform a POST request to login with the newly created user and obtain access token.
  
    i) url : http://localhost:9990/auth/realms/conductor/protocol/openid-connect/token
    ii) body : x-www-form-urlencoded
    iii) client_id : conductor_user_client (newly created client id in above steps)
    iv) grant_type : password
    v) client_secret : XXXXXXXX (new obtained secret in above steps)
    vi) username : maheshy (newly created user in above steps)
    vii) password : password (newly set password in the above steps)
  
  ![image](https://user-images.githubusercontent.com/24351133/111203413-fbe71900-85ea-11eb-8f17-9e2c052d45f1.png)
  
  Copy the access_token value returned.
  
  2)	Perform a GET request to verify access to conductor metadata/taskdefs API to get the list of tasks (probably empty list as its newly spun up)
  
   a.	Without Access Token – Expect 401 Unauthorized
   
      i) url : http://localhost:8080/api/metadata/taskdefs
      ii) header : 
            Accept : application/json
   
   ![image](https://user-images.githubusercontent.com/24351133/111204402-153c9500-85ec-11eb-9552-c540b5a97c2c.png)
   
A 401 error would mean, the Conductor APIs are secure and cannot be accessed without a valid access_token.
   
   b.	With Access Token – Expect 200 Success and list of tasks (probably empty list as its newly spun up)
   
      i) url : http://localhost:8080/api/metadata/taskdefs
      ii) header : 
            Accept : application/json
            Authorization : Bearer <<ACCESS_TOKEN>>
            
Replace <<ACCESS_TOKEN>> with the actual access_token copied in above step. Also note that there is space after the word Bearer and before the access_token
   
   ![image](https://user-images.githubusercontent.com/24351133/111204855-8aa86580-85ec-11eb-895f-80045a6af6a4.png)

#### Additional Verification

  1)	Perform a GET request to keycloak userinfo url to check user profile details
  
    i) url : http://localhost:9990/auth/realms/conductor/protocol/openid-connect/userinfo
    ii) header : 
          Accept : application/json
          Authorization : Bearer <<ACCESS_TOKEN>>
  
  ![image](https://user-images.githubusercontent.com/24351133/111205840-af510d00-85ed-11eb-9295-8cead436e5a7.png)
  
  2)	Perform a GET request to conductor boot userinfo url to check user profile details
  
    i) url : http://localhost:8080/userinfo
    ii) header : 
          Accept : application/json
          Authorization : Bearer <<ACCESS_TOKEN>>
  
  ![image](https://user-images.githubusercontent.com/24351133/111205938-cb54ae80-85ed-11eb-9f83-f709d5da030a.png)

### Log Aggregation Configuration

  1) On the default Kibana page, initial step is to create the index which will hold the logs. 

  2) Select Time Filter Field Name : I don't wan't to use the Time Filter

  3) Click Save

  ![image](https://user-images.githubusercontent.com/24351133/111207553-c85abd80-85ef-11eb-90a6-5ff5073a55d9.png)

  4) Navigate to Discovery and all the logs from the conductorboot will be shown as below.

  ![image](https://user-images.githubusercontent.com/24351133/111207936-33a48f80-85f0-11eb-84a6-148ed66ecbb8.png)

  5) Select the below listed from the field selector for a better view
    
    i) LOG_TIMESTAMP
    ii) LOG_LEVEL
    iii) JAVA_CLASS
    iv) LOG_MESSAGE

  6) Add a filter - where LOG_MESSGE exists : This would help filter away empty lines.
  
  ![image](https://user-images.githubusercontent.com/24351133/111209354-e9241280-85f1-11eb-9999-eb7d030ddc09.png)

  ![image](https://user-images.githubusercontent.com/24351133/111209443-0527b400-85f2-11eb-9aa2-3940c1872e6f.png)

  7) Save the view as "Conductor Logs"

  ![image](https://user-images.githubusercontent.com/24351133/111209600-3607e900-85f2-11eb-9681-25ccfde16d31.png)
  
  ![image](https://user-images.githubusercontent.com/24351133/111209615-3acc9d00-85f2-11eb-952a-2a6d4aefe4a7.png)

  8) Click New view and repeat the step 5 and 6

  9) Add extra filter JAVA_CLASS : Logbook : This would help filter only the HTTP Requests and Reponses i.e. API Calls.

  10) Save the view as "HTTP Requests and Reponses"

  ![image](https://user-images.githubusercontent.com/24351133/111209728-68194b00-85f2-11eb-81d6-06036280430c.png)
  
  ![image](https://user-images.githubusercontent.com/24351133/111209766-736c7680-85f2-11eb-92a1-c273256f897f.png)

Once all the above steps are completed, the target state of an OAuth2.0 Secure, Containerized Microservice Orchestrator Netflix Conductor and a Log aggregator, is ready for use.
