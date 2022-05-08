package com.es.phoneshop.dao.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@NoArgsConstructor
@Slf4j
public class DBConnector {
    private static final String DB_PATH = "jdbc:mysql://localhost:3306/phone_shop";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "admin";

    @Getter
    private Connection connection;

    public <T> T execute(String dbQuery, ThrowableDBFunction<T> action) {
        connect();
        try (PreparedStatement preparedStatement = connection.prepareStatement(dbQuery)) {
            T returnValue = action.execute(preparedStatement);
            connection.commit();
            return returnValue;
        } catch (Exception ex) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
            throw new IllegalArgumentException("Unable to execute statement.", ex);
        } finally {
            try {
                connection.close();
            } catch (SQLException ex) {
                log.error("Unable to close connection.");
            }
        }
    }

    public void execute(String dbQuery, ThrowableVoidDBFunction action) {
        execute(dbQuery, (preparedStatement -> {
            action.execute(preparedStatement);
            return null;
        }));
    }

    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_PATH, DB_USERNAME, DB_PASSWORD);
            }
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            if (connection != null) {
                log.error("Database error occurred.");
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Unable to close connection.");
                }
            } else log.error("Cannot establish connection!");
            throw new IllegalArgumentException("Cannot establish connection!", ex);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }
}
