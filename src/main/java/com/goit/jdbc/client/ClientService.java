package com.goit.jdbc.client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private PreparedStatement createSt;
    private PreparedStatement getByIdSt;
    private PreparedStatement selectMaxIdSt;
    private PreparedStatement setNameSt;
    private PreparedStatement deleteByIdSt;
    private PreparedStatement getListAllSt;
    private PreparedStatement clearSt;

    public ClientService(Connection connection) {
        try {
            createSt = connection.prepareStatement("INSERT INTO client (name) VALUES (?)");
            getByIdSt = connection.prepareStatement("SELECT name FROM client WHERE id = ?");
            selectMaxIdSt = connection.prepareStatement("SELECT MAX(id) AS maxId FROM client");
            setNameSt = connection.prepareStatement("UPDATE client SET name = ? WHERE id = ?");
            deleteByIdSt = connection.prepareStatement("DELETE FROM client WHERE id = ?");
            getListAllSt = connection.prepareStatement("SELECT * FROM client");
            clearSt = connection.prepareStatement("DELETE FROM client");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long create(String name) throws SQLException {
        if (!isValidated(name)) {
            throw new IllegalArgumentException("Incorrect name type");
        }
        createSt.setString(1, name);
        createSt.executeUpdate();
        long id;
        try (ResultSet rs = selectMaxIdSt.executeQuery()) {
            rs.next();
            id = rs.getLong("maxId");
        }
        return id;
    }


    public String getById(long id) throws SQLException {
        getByIdSt.setLong(1, id);

        try (ResultSet rs = getByIdSt.executeQuery()) {

            if (!rs.next()) {
                return null;
            }
            Client client = new Client();
            client.setId(id);
            String name = rs.getString("name");
            if (isValidated(name)) {
                client.setName(rs.getString("name"));
            }
            return client.getName();
        }

    }

    public void setName(long id, String name) throws SQLException {
        if (!isValidated(name)) {
            throw new IllegalArgumentException("Incorrect name type");
        }
        setNameSt.setString(1, name);
        setNameSt.setLong(2, id);
        setNameSt.executeUpdate();
    }

    public void deleteById(long id) throws SQLException {
        deleteByIdSt.setLong(1, id);
        deleteByIdSt.executeUpdate();
    }

    public List<Client> listAll() throws SQLException {
        try (ResultSet rs = getListAllSt.executeQuery()) {
            List<Client> clients = new ArrayList<>();
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getLong("id"));
                String name = rs.getString("name");
                if (isValidated(name)) {
                    client.setName(rs.getString("name"));
                }
                clients.add(client);
            }
            return clients;
        }
    }

    private boolean isValidated(String name) {
        if (name == null || name.length() < 2 || name.length() > 30) {
            return false;
        }
        return true;
    }

    public void clear() throws SQLException {
        clearSt.executeUpdate();
    }
}
