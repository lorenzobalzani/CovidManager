package view.operatoreTampone;

import model.Esiti;

import javax.swing.*;
import java.awt.*;

public class OperatoreTampone extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeMessage;
    private JTextField textField1;
    private JButton inserisciButton;
    private JComboBox<String> esiti;

    public OperatoreTampone() {
        setTitle("Operatore di tampone");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
    }
}
