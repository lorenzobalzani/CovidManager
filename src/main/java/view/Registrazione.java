package view;

import controller.DataBaseController;
import model.OperatoreSanitario;
import model.Ruoli;
import utility.EncryptionUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Registrazione extends JFrame {
    private JPanel mainPanel;
    private JButton registraCittadinoButton;
    private JButton creaCredenzialiButton;
    private JTextField cognome;
    private JTextField nome;
    private JTextField cf;
    private JTextField dataDiNascita;
    private JTextField genere;
    private JTextField comune;
    private JTextField telefono;
    private JTextField username;
    private JPasswordField passwordField;
    private JComboBox<String> tipo;
    private JTextField cfCredenziali;

    public Registrazione() throws HeadlessException {
        setTitle("Registrazione");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5));
        setVisible(true);
        registraCittadinoButton.addActionListener(e -> creaCittadino());
        tipo.addItem("MEDICO_DI_BASE");
        tipo.addItem("MEDICO_RESPONSABILE");
        tipo.addItem("OPERATORE_DI_TAMPONE");
        tipo.addItem("EPIDEMIOLOGO");
        tipo.addItem("CONTACT_TRACING");
        creaCredenzialiButton.addActionListener(e -> creaCredenziali());
    }

    private void creaCittadino() {
        DataBaseController dataBaseController = new DataBaseController();
        String creaCittadino = "INSERT INTO CITTADINO VALUES(" +
                "'" + cf.getText() + "', " +
                "'" + nome.getText() + "', " +
                "'" + cognome.getText() + "', " +
                "'" + dataDiNascita.getText() + "', " +
                "'" + genere.getText() + "', " +
                "'" + comune.getText() + "', " +
                "'" + telefono.getText() + "', " +
                "null);";
        JOptionPane.showMessageDialog(this,
                "Inserimento avvenuto con successo!",
                "Cittadino creato con successo",
                JOptionPane.INFORMATION_MESSAGE);
        cf.setText("");
        nome.setText("");
        cognome.setText("");
        dataDiNascita.setText("");
        genere.setText("");
        comune.setText("");
        telefono.setText("");
        try {
            dataBaseController.getConnection().prepareStatement(creaCittadino).executeUpdate();
            dataBaseController = null;
            } catch (SQLException throwables) {
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Inserimento non andato a buon fine! Ritentare tra qualche minuto!",
                    "Problema generico",
                    JOptionPane.WARNING_MESSAGE);
            }
    }

    private void creaCredenziali() {
        DataBaseController dataBaseController = new DataBaseController();
        String hashedPassword = EncryptionUtility.hashPassword(String.valueOf(passwordField.getPassword()));
        String creaCittadino = "INSERT INTO CREDENZIALI VALUES(" +
                "'" + username.getText() + "', " +
                "'" + cfCredenziali.getText() + "', " +
                "'" + hashedPassword + "', " +
                "'" + tipo.getSelectedItem() + "');";
        String aggiungiTipo = "INSERT INTO " + tipo.getSelectedItem() + " (CF) VALUES (" +
                "'" + cfCredenziali.getText() + "')";
        try {
            dataBaseController.getConnection().prepareStatement(creaCittadino).executeUpdate();
            if (tipo.getSelectedItem() != "CONTACT_TRACING") {
                dataBaseController.getConnection().prepareStatement(aggiungiTipo).executeUpdate();
            }
            dataBaseController = null;
            JOptionPane.showMessageDialog(this,
                    "Inserimento avvenuto con successo!",
                    "Credenziali create con successo",
                    JOptionPane.INFORMATION_MESSAGE);
            cfCredenziali.setText("");
            username.setText("");
            passwordField.setText("");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Inserimento non andato a buon fine! Ritentare tra qualche minuto!",
                    "Problema generico",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
