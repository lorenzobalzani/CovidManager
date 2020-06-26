package view.operatoreTampone;

import controller.DataBaseController;
import model.OperatoreSanitario;
import utility.EncryptionUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        esiti.addItem("Positivo");
        esiti.addItem("Negativo");
        inserisciButton.addActionListener(e -> inserisciEsito(operatoreSanitario.getCF()));
    }

    private void inserisciEsito(String operatorCF) {
        DataBaseController dataBaseController = new DataBaseController();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inserisciEsito = "INSERT INTO TAMPONE VALUES (" +
                "'" + cfTextField.getText() + "', '" + simpleDateFormat.format(calendar.getTime()) + "', '" +
                esiti.getSelectedItem() + "', (SELECT ID_OPE " +
                "FROM OPERATORE_DI_TAMPONE OPE WHERE OPE.CF = '" + operatorCF + "'));";

        try {
            dataBaseController.getConnection().prepareStatement(inserisciEsito).executeUpdate();
            dataBaseController = null;
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Prova a controllare le seguenti condizioni:" +
                            "\n1. Non inserire più di un tampone al giorno" +
                            "\n2. Inserire un CF valido",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
        inserisciStatoSalute(String.valueOf(esiti.getSelectedItem()));
        checkGuarito();
    }

    private void checkGuarito() {
        int tamponiNegativi = 0;
        DataBaseController dataBaseController = new DataBaseController();
        String checkGuarito = "";
        try {
            ResultSet rs = dataBaseController.getConnection().prepareStatement(checkGuarito).executeQuery();
            tamponiNegativi = Integer.parseInt(rs.getString(0));
            if (tamponiNegativi == 2) {
                inserisciStatoSalute("Guarito");
            }
            dataBaseController = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void inserisciStatoSalute(String esito) {
        DataBaseController dataBaseController = new DataBaseController();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inserisciStatoSalute = "INSERT INTO STATO_SALUTE VALUES (" +
                "'" + cfTextField.getText() + "', '" + simpleDateFormat.format(calendar.getTime()) + "', '" +
                esito + "');";
        try {
            dataBaseController.getConnection().prepareStatement(inserisciStatoSalute).executeUpdate();
            dataBaseController = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
