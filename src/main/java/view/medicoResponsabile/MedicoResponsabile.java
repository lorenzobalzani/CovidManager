package view.medicoResponsabile;

import controller.DataBaseController;
import model.Cittadino;
import model.OperatoreSanitario;

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

public class MedicoResponsabile extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeMessage;
    private JTextField cfTextField;
    private JButton inserisciButton;
    private JComboBox<String> tipo;
    private JSpinner codiceGravita;
    private JTextField data;
    private JPanel repartoPanel;
    private JComboBox<String> ospedale;
    private JTextField piano;
    private JTextField reparto;

    public MedicoResponsabile(OperatoreSanitario operatoreSanitario) {
        setTitle("Medico responsabile");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repartoPanel.setBorder(BorderFactory.createTitledBorder("Reparto"));
        inserisciButton.addActionListener(e -> {});
        codiceGravita.setModel(
                new SpinnerNumberModel(0, 0, 10, 1));
        tipo.addItem("Inizio ricovero");
        tipo.addItem("Fine ricovero");
        tipo.addItem("Inizio terapia intensiva");
        tipo.addItem("Fine terapia intensiva");
        tipo.addItem("Decesso");
        ospedale.addItem("Seleziona ospedale...");
        queryOspedale();
        inserisciButton.addActionListener(e -> {
            if (String.valueOf(tipo.getSelectedItem()).equals("Decesso")) {
                inserisciDecesso();
            } else {
                inserisciRicovero();
            }
        });
    }

    private void inserisciDecesso() {
    }

    private void inserisciRicovero() {
        DataBaseController dataBaseController = new DataBaseController();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String inserisciRicovero = "INSERT INTO REFERTO_RICOVERO VALUES ('" +
                cfTextField.getText() + "', '" + tipo.getSelectedItem() + "', '" +
                codiceGravita.getValue() + "', '" + simpleDateFormat.format(calendar.getTime()) + "', (SELECT" +
                " idOspedale FROM OSPEDALE WHERE nomeOspedale = '" +
                ospedale.getSelectedItem() + "'), " + piano.getText() + ", " +
                reparto.getText() + ");";
        System.out.println(inserisciRicovero);
        try {
            dataBaseController.getConnection().prepareStatement(inserisciRicovero).executeUpdate();
            dataBaseController = null;
            JOptionPane.showMessageDialog(this,
                    "Referto inserito con successo!",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Prova a controllare le seguenti condizioni:" +
                            "\n1. Non inserire lo stesso referto più volte al giorno" +
                            "\n2. Inserire un piano e reparto valido",
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
    }

    private void queryOspedale() {
        DataBaseController dataBaseController = new DataBaseController();
        try {
            String statement = "SELECT * FROM OSPEDALE";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            while (rs.next()) {
                ospedale.addItem(rs.getString("nomeOspedale"));
            }
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
