package com.goit.jdbc.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {
    private static Logger log = LoggerFactory.getLogger(PropertyReader.class);
    private static Properties property = new Properties();

    private PropertyReader() {
    }

    public static String getPostgresConnectionUrl() {


        return new StringBuilder("jdbc:postgresql://")
                .append(getInput().getProperty("postgres.db.host"))
                .append(":")
                .append(getInput().getProperty("postgres.db.port"))
                .append("/")
                .append(getInput().getProperty("postgres.db.database"))
                .append("?currentSchema=public")
                .toString();

    }

    public static String getPostgresUser() {

        return getInput().getProperty("postgres.db.username");
    }

    public static String getPostgresPassword() {

        return getInput().getProperty("postgres.db.password");

    }

    private static Properties getInput() {

        try (InputStream input = PropertyReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                log.info("Property file doesn't exist!");
                return new Properties();
            }

            property.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
            return new Properties();
        }
        return property;
    }
}
