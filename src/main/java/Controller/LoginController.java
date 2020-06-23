package Controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    private final Connection connection;

    public LoginController(final String serverIP, final int port, final String database) {
        DataBaseController dataBaseController = new DataBaseController(serverIP, port, database,
                "root", "root");
        connection = dataBaseController.getConnection();
    }

    /**
     * It queries the DB checking for user presence.
     * @param username -- clear username
     * @param password -- hashed password
     * @return the rule of the user
     */
    public Ruoli login(final String username, final String password) {
        try {
            ResultSet rs = connection.prepareStatement("SHOW TABLES;").executeQuery();
            while(rs.next()){
                String s = rs.getString(1);
                System.out.println(s);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //TODO
        if (true) {
            return Ruoli.EPIDEMIOLOGO;
        } else {
            return Ruoli.NESSUN_RUOLO;
        }
    }
}
