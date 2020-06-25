package utility;

import controller.DataBaseController;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EncryptionUtility {

    public static boolean checkPassword(String username, String password) {
        DataBaseController dataBaseController = new DataBaseController();
        String checkOldPassword = "SELECT hashedPassword FROM CREDENZIALI" +
                " WHERE username = '" + username + "'";
        try {
            ResultSet rs = dataBaseController.getConnection().prepareStatement(checkOldPassword).executeQuery();
            if (rs.next()) {
                return EncryptionUtility.checkPasswords(password,
                        rs.getString("hashedPassword"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    private static boolean checkPasswords(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
