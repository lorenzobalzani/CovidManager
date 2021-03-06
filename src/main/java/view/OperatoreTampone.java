package view;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.DataBaseController;
import model.OperatoreSanitario;
import view.Impostazioni;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class OperatoreTampone extends JFrame {
    private JPanel mainPanel;
    private JTextField cfTextField;
    private JButton inserisciButton;
    private JComboBox<String> esiti;
    private JButton impostazioniButton;

    public OperatoreTampone(OperatoreSanitario operatoreSanitario) {
        setTitle("Gestione operatore di tampone");
        setContentPane(mainPanel);
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3.5),
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3.5));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        esiti.addItem("Positivo");
        esiti.addItem("Negativo");
        inserisciButton.addActionListener(e -> inserisciEsito(operatoreSanitario.getCF()));
        impostazioniButton.addActionListener(e -> new Impostazioni(operatoreSanitario));
    }

    private void inserisciEsito(String operatorCF) {
        DataBaseController dataBaseController = new DataBaseController();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String inserisciEsito = "INSERT INTO TAMPONE VALUES (" +
                "'" + cfTextField.getText() + "', '" + simpleDateFormat.format(calendar.getTime()) + "', '" +
                esiti.getSelectedItem() + "', (SELECT ID_OPE " +
                "FROM OPERATORE_DI_TAMPONE OPE WHERE OPE.CF = '" + operatorCF + "'));";
        try {
            dataBaseController.getConnection().prepareStatement(inserisciEsito).executeUpdate();
            dataBaseController = null;
            JOptionPane.showMessageDialog(this,
                    "Tampone eseguito con successo",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Prova a controllare le seguenti condizioni:" +
                            "\n1. Non inserire più di un tampone al giorno" +
                            "\n2. Inserire un CF valido",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
    }

}
