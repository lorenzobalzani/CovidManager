package controller;

import model.OperatoreSanitario;
import model.Ruoli;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    private final Connection connection;

    public LoginController(final String serverIP, final int port, final String database) {
        DataBaseController dataBaseController = new DataBaseController(serverIP, port, database,
                "balzanilo", "cambiami");
        connection = dataBaseController.getConnection();
    }

    /**
     * It queries the DB checking for user presence.
     * @param inputUsername -- clear username
     * @param password -- hashed password
     * @return the model of the user
     */
    public Optional<OperatoreSanitario> login(final String inputUsername, final String password) {
        var operatoreSanitario = new OperatoreSanitario();
        try {
            ResultSet rs = connection.prepareStatement("SELECT CF, tipo FROM CREDENZIALI WHERE " +
                    "username='" + inputUsername + "' AND hashedPassword='" + password + "';").executeQuery();
            boolean noResult = true;
            while (rs.next()) {
                operatoreSanitario.setCF(rs.getString("CF"));
                switch (rs.getString("tipo")) {
                    case "MEDICO_DI_BASE":
                        operatoreSanitario.setTipo(Ruoli.MEDICO_DI_BASE);
                        break;
                    case "MEDICO_RESPONSABILE":
                        operatoreSanitario.setTipo(Ruoli.MEDICO_RESPONSABILE);
                        break;
                    case "OPERATORE_DI_TAMPONE":
                        operatoreSanitario.setTipo(Ruoli.OPERATORE_DI_TAMPONE);
                        break;
                    case "EPIDEMIOLOGO":
                        operatoreSanitario.setTipo(Ruoli.EPIDEMIOLOGO);
                        break;
                }
               noResult = false;
            }
            rs.close();
            connection.close();
            if (noResult) {
                return Optional.empty();
            } else {
                return Optional.of(operatoreSanitario);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
}
