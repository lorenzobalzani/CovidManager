package View;

import Controller.LoginController;
import Controller.Ruoli;
import Controller.DataBaseController;
import View.epidemiologo.Epidemiologo;
import View.medicoDiBase.MedicoDiBase;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;


public class Login extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;

    public Login() {
        setTitle("Login Covid19 Manager");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginButton.addActionListener(e -> {
            DataBaseController dbController = new DataBaseController("localhost", 3306,
                    "users");
            Ruoli risultatoQuery =  LoginController.login(usernameField.getText(),
               String.valueOf(passwordField.getPassword()), dbController.getConnection());
            switch (risultatoQuery) {
                case EPIDEMIOLOGO:
                    new Epidemiologo();
                    break;
                case MEDICO_DI_BASE:
                    new MedicoDiBase();
                    break;
                case MEDICO_RESPOSNABILE:
                    break;
                case OPERATORE_DI_TAMPONE:
                    break;
                case NESSUN_RUOLO:
                    JOptionPane.showMessageDialog(this,
                            "Prego rivedere le proprie credenziali!",
                            "Credenziali errate",
                            JOptionPane.WARNING_MESSAGE);
                    break;
            }
            dispose();
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        new Login();
    }

}
