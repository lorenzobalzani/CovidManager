package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseController {
    private String connectionUrl;
    private final String username;
    private final String password;
    private Connection connection;

    public DataBaseController(final String serverIP, final int port, final String database, final String username,
                              final String password) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.username = username;
        this.password = password;
        setConnectionUrl(serverIP, port, database);
        setConnection();
    }

    public void setConnectionUrl(final String serverIP, final int port, final String database) {
        connectionUrl = "jdbc:mysql://" + serverIP + ":" + port +
                "/" + database + "?useUnicode=true";
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection() {
        try {
            connection = DriverManager.getConnection(connectionUrl, username, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
