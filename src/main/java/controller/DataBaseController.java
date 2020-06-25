package controller;

import utility.DatabaseInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseController {
    private String connectionUrl;
    private Connection connection;

    public DataBaseController() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setConnectionUrl();
        setConnection();
    }

    public void setConnectionUrl() {
        connectionUrl = "jdbc:mysql://" + DatabaseInfo.server + ":" + DatabaseInfo.port +
                "/" + DatabaseInfo.database + "?useUnicode=true";
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnection() {
        try {
            connection = DriverManager.getConnection(connectionUrl, DatabaseInfo.username, DatabaseInfo.password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
