package view.medicoDiBase;

import controller.DataBaseController;
import model.OperatoreSanitario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class impostazioniMedicoDiBase extends JFrame {
    private JPanel mainPanel;
    private JButton salvaPasswordButton;
    private JButton salvaUsernameButton;
    private JTextField newUsernameTextField;
    private JTextField oldPasswordTextField;
    private JTextField newPasswordTextField;
    private OperatoreSanitario medicoDiBase;

    public impostazioniMedicoDiBase(OperatoreSanitario medicoDiBase) throws HeadlessException {
        setTitle("Impostazioni medico di base");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        this.medicoDiBase = medicoDiBase;
        salvaUsernameButton.addActionListener(e-> updateUsername(newUsernameTextField.getText()));
    }

    private void updateUsername(String newUsername) {
        DataBaseController dataBaseController = new DataBaseController();
        String statement = "UPDATE CREDENZIALI SET username='" + newUsername + "' WHERE CF='"
                + medicoDiBase.getCF() + "';";
        try {
            dataBaseController.getConnection().prepareStatement(statement).executeUpdate();
            dataBaseController = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
