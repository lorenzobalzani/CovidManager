package view;

import controller.DataBaseController;
import utility.EncryptionUtility;
import model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Impostazioni extends JFrame {
    private JPanel mainPanel;
    private JButton salvaPasswordButton;
    private JButton salvaUsernameButton;
    private JTextField newUsernameTextField;
    private JPasswordField oldPasswordTextField;
    private JPasswordField newPasswordTextField;
    private final OperatoreSanitario operatoreSanitario;

    public Impostazioni(OperatoreSanitario operatoreSanitario) throws HeadlessException {
        setTitle("Impostazioni");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        this.operatoreSanitario = operatoreSanitario;
        salvaUsernameButton.addActionListener(e-> updateUsername(newUsernameTextField.getText()));
        salvaPasswordButton.addActionListener(e-> updatePassword(String.valueOf(oldPasswordTextField.getPassword()),
                String.valueOf(newPasswordTextField.getPassword())));
    }

    private void updatePassword(String oldPassword, String newPassword) {
        DataBaseController dataBaseController = new DataBaseController();
        if (EncryptionUtility.checkPassword(operatoreSanitario.getUsername(), oldPassword)) {
            String hashedPassword = EncryptionUtility.hashPassword(newPassword);
            String updatePassword = "UPDATE CREDENZIALI SET hashedPassword='" + hashedPassword + "' WHERE CF='"
                    + operatoreSanitario.getCF() + "';";
            try {
                dataBaseController.getConnection().prepareStatement(updatePassword).executeUpdate();
                dataBaseController = null;
                JOptionPane.showMessageDialog(this,
                        "Password cambiata con successo",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException throwables) {
                JOptionPane.showMessageDialog(this,
                        "Errore nel cambiamento della password",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE);
                throwables.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Prego ricontrollare la vecchia password!",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateUsername(String newUsername) {
        DataBaseController dataBaseController = new DataBaseController();
        String statement = "UPDATE CREDENZIALI SET username='" + newUsername + "' WHERE CF='"
                + operatoreSanitario.getCF() + "';";
        try {
            dataBaseController.getConnection().prepareStatement(statement).executeUpdate();
            dataBaseController = null;
            JOptionPane.showMessageDialog(this,
                    "Username cambiato con successo",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Errore nell'aggiornare lo username",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
    }
}
