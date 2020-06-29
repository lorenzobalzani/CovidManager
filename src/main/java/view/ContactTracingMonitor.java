package view;

import model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;

public class ContactTracingMonitor extends JFrame   {
    private JPanel mainPanel;

    public ContactTracingMonitor(OperatoreSanitario operatoreSanitario) {
        setTitle("Gestione contact tracing");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
