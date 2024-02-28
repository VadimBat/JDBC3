package com.goit.jdbc.storage;

import com.goit.jdbc.property.PropertyReader;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final Database INSTANCE = new Database();
    private static Connection connection;

    private Database() {
        try {
            this.connection = DriverManager.getConnection(
                    PropertyReader.getPostgresConnectionUrl(),
                    PropertyReader.getPostgresUser(),
                    PropertyReader.getPostgresPassword());
            flyWayMigrate(
                    PropertyReader.getPostgresConnectionUrl(),
                    PropertyReader.getPostgresUser(),
                    PropertyReader.getPostgresPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public int executeUpdate(String sql) {
        try (Statement st = connection.createStatement()) {
            return st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void flyWayMigrate(String connectionUrl, String user, String password) {
        Flyway flyway = Flyway
                .configure()
                .dataSource(connectionUrl, user, password)
                .load();
        flyway.migrate();
    }
}
