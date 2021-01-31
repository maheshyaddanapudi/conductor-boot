package com.netflix.conductorboot.constants;

public class Constants {

    // General Constants
    public static final String NONE = "NONE";
    public static final String FALSE = "false";
    public static final String TRUE= "true";
    public static final String TEST = "test";
    public static final String STRING_INITIALIZR = "";
    public static final String COMMA = ",";
    public static final String CONDUCTOR = "conductor";

    // Embedded Elasticsearch Constants
    public static final String EMBEDDED_ELASTICSEARCH = "embedded-elasticsearch";
    public static final String SPRING_BOOT_EMBEDDED = "SPRING_BOOT_EMBEDDED";


    // Embedded MariaDB Constants
    public static final String MARIADB_ARGS_MAX_CONNECTIONS = "--max-connections=";
    public static final String MARIADB_ARGS_WAIT_TIMEOUT = "--wait-timeout=";
    public static final String MARIADB_ARGS_CONNECT_TIMEOUT = "--connect-timeout=";

    public static final String MARIADB_URL_EXTN_AUTO_RECONNECT = "?autoReconnect=true";

    public static final String DB = "db";
    public static final String MYSQL = "mysql";
    public static final String MARIADB4J = "mariadb4j";
    public static final String JDBC_URL = "jdbc.url";
    public static final String JDBC_USERNAME = "jdbc.username";
    public static final String JDBC_PASSWORD = "jdbc.password";

    public static final String FLYWAY_VALIDATE_ON_MIGRATE = "flyway.validate-on-migrate";
    public static final String FLYWAY_BASELINE_ON_MIGRATE = "flyway.baselineOnMigrate";
    public static final String FLYWAY_IGNORE_MISSING_MIGRATIONS = "flyway.ignore-missing-migrations";

    // Embedded OAuth2 Constants
    public static final String EMBEDDED_OAUTH2 = "embedded-oauth2";
    public static final String SECURITY = "security";

    public static final String resourceIds = "USER_ADMIN_RESOURCE";
    public static final String authorizedGrantTypes = "authorization_code,password,refresh_token,implicit";
    public static final String OAUTH_SECUIRTY = "oauth.security";

    public static final String EXTERNAL_OAUTH2 = "external-oauth2";
    public static final String EXTERNAL_ADFS = "external-adfs";
    public static final String OPTIONS = "OPTIONS";
}
