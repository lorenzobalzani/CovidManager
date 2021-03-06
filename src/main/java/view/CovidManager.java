package view;

import controller.LoginController;
import model.OperatoreSanitario;
import view.medicoDiBase.MedicoDiBase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class CovidManager extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton registrazioneButton;

    public CovidManager() {
        setTitle("Login CovidManager");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginButton.addActionListener(e -> login());
        registrazioneButton.addActionListener(e -> new Registrazione());
        usernameField.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
    }

    private void login() {
        Optional<OperatoreSanitario> operatoreSanitario =
                new LoginController()
                        .login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
        if (operatoreSanitario.isPresent()) {
            switch (operatoreSanitario.get().getTipo()) {
                case EPIDEMIOLOGO:
                    //TODO classe epidemiologo
                    new Epidemiologo(operatoreSanitario.get());
                    break;
                case MEDICO_DI_BASE:
                    new MedicoDiBase(operatoreSanitario.get());
                    break;
                case MEDICO_RESPONSABILE:
                    new MedicoResponsabile(operatoreSanitario.get());
                    break;
                case OPERATORE_DI_TAMPONE:
                    new OperatoreTampone(operatoreSanitario.get());
                    break;
                case CONTACT_TRACING:
                    new ContactTracingMonitor(operatoreSanitario.get());
                    break;
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Prego ricontrollare le credenziali!",
                    "Credenziali errate",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CovidManager();
    }
}
