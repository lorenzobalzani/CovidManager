package view;

import controller.LoginController;
import model.OperatoreSanitario;
import view.epidemiologo.Epidemiologo;
import view.medicoDiBase.MedicoDiBase;
import view.medicoResponsabile.MedicoResponsabile;
import view.operatoreTampone.OperatoreTampone;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class CovidManager extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JLabel welcomeLabel;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private JLabel usernameLabel;
    private JButton registrazioneButton;

    public CovidManager() {
        setTitle("Login CovidManager");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 3,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginButton.addActionListener(e -> {
            Optional<OperatoreSanitario> operatoreSanitario =
                    new LoginController()
                    .login(usernameField.getText(), String.valueOf(passwordField.getPassword()));
            if (operatoreSanitario.isPresent()) {
                switch (operatoreSanitario.get().getTipo()) {
                    case EPIDEMIOLOGO:
                        //TODO classe epidemiologo
                        new Epidemiologo();
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
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Prego ricontrollare le credenziali!",
                        "Credenziali errate",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        registrazioneButton.addActionListener(e -> new Registrazione());
    }

    public static void main(String[] args) {
        new CovidManager();
    }
}
