package com.goit.jdbc.client;

import com.goit.jdbc.storage.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientServiceTest {
    private Connection connection;
    private ClientService clientService;

    @BeforeEach
    void beforeEach() throws SQLException {
        connection = Database.getInstance().getConnection();
        clientService = new ClientService(connection);
        clientService.clear();
    }

    @AfterEach
    void afterEach() throws SQLException {
        connection.close();
    }

    @Test
    void testThatClientCreatedCorrectly() throws SQLException {

        //Given
        Client client = new Client();
        client.setName("TestName");
        String originalName = client.getName();

        //When
        long id = clientService.create(originalName);
        String savedName = clientService.getById(id);

        //Then
        Assertions.assertEquals(originalName, savedName);

    }

    @Test
    void testThatNameValidatedCorrectly() throws SQLException {

        //Given
        List<Client> originalClients = new ArrayList<>();

        Client badClient1 = new Client();
        badClient1.setName("B");
        Client badClient2 = new Client();
        badClient1.setName("Bfhjsdhfdskjfhskfhksdfhjdkfhjdkshfkdhfhsdjkfh");
        Client badClient3 = new Client();
        badClient1.setName(null);

        originalClients.add(badClient1);
        originalClients.add(badClient2);
        originalClients.add(badClient3);

        //When & Then
        for (Client originalClient : originalClients) {
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> clientService
                            .create(originalClient
                                    .getName()));
        }
    }

    @Test
    void setNameTest() throws SQLException {
        //Given
        Client originalClient = new Client();
        originalClient.setName("TestName");
        String originalName = originalClient.getName();

        //When
        long id = clientService.create(originalName);
        originalClient.setId(id);
        originalClient.setName("New name");
        String newName = originalClient.getName();
        clientService.setName(id, newName);

        //Then
        String expected = clientService.getById(id);
        Assertions.assertEquals(expected, originalClient.getName());
    }

    @Test
    void deleteById() throws SQLException {

        //Given
        Client expected = new Client();
        expected.setName("TestName");
        String originalName = expected.getName();

        //When
        long id = clientService.create(expected.getName());
        clientService.deleteById(id);

        //Then
        Assertions.assertNull(clientService.getById(id));

    }

    @Test
    void listAllTest() throws SQLException {

        //Given
        Client expected = new Client();
        expected.setName("TestName");

        //When
        long id = clientService.create(expected.getName());
        expected.setId(id);

        List<Client> expectedClients = Collections.singletonList(expected);
        List<Client> actualClients = clientService.listAll();

        //Then
        Assertions.assertEquals(expectedClients.size(), actualClients.size());
    }
}