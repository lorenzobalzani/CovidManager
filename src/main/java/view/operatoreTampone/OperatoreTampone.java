package view.operatoreTampone;

import controller.DataBaseController;
import model.OperatoreSanitario;
import utility.EncryptionUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class OperatoreTampone extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeMessage;
    private JTextField cfTextField;
    private JButton inserisciButton;
    private JComboBox<String> esiti;

    public OperatoreTampone(OperatoreSanitario operatoreSanitario) {
        setTitle("Operatore di tampone");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        esiti.addItem("Positivo");
        esiti.addItem("Negativo");
        inserisciButton.addActionListener(e -> inserisciEsito(operatoreSanitario.getCF()));
    }

    private void inserisciEsito(String operatorCF) {
        DataBaseController dataBaseController = new DataBaseController();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String statement = "INSERT INTO TAMPONE VALUES (" +
                "'" + cfTextField.getText() + "', '" + simpleDateFormat.format(calendar.getTime()) + "', '" +
                esiti.getSelectedItem() + "', (SELECT ID_OPE " +
                "FROM OPERATORE_DI_TAMPONE OPE WHERE OPE.CF = '" + operatorCF + "'));";
        try {
            dataBaseController.getConnection().prepareStatement(statement).executeUpdate();
            dataBaseController = null;
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Il codice fiscale non corrisponde a nessun cittadino",
                    "CF errato",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
    }

    private void aggiornaStatoSalute() {

    }
}
