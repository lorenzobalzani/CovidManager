package view.medicoResponsabile;

import model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;

public class MedicoResponsabile extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeMessage;
    private JTextField cfTextField;
    private JButton inserisciButton;

    public MedicoResponsabile(OperatoreSanitario operatoreSanitario) {
        setTitle("Medico responsabile");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        inserisciButton.addActionListener(e -> {});
    }
}
