package com.goit;

import com.goit.jdbc.client.ClientService;
import com.goit.jdbc.storage.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class App {
    private static Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        Connection connection = Database.getInstance().getConnection();
        ClientService clientService = new ClientService(connection);

        try {
            log.info("New user {} ", clientService.create("Oleg"));
            log.info("Get user {} ", clientService.getById(30));
            log.info("All users {} ", clientService.listAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
