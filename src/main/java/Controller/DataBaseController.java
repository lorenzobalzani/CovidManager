package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseController {
    private String connectionUrl;
    private Connection connection;

    public DataBaseController(String serverIP, int port, String database) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            setConnectionUrl(serverIP, port, database);
            connection = DriverManager.getConnection(connectionUrl, "root", "root");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void setConnectionUrl(String serverIP, int port, String database) {
        connectionUrl = "jdbc:mysql://" + serverIP + ":" + port +
                "/" + database + "?useUnicode=true";
    }
}
