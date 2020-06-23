package View.diariClinici;

import javax.swing.*;
import java.awt.*;

public class diariClinici extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox comboBox1;
    private JTextArea textArea1;
    private JButton salvaButton;
    private JButton defaultButton;
    private JLabel selectLabel;
    private JButton diariClinicButton;

    public diariClinici() {
        setTitle("Diari clinici");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        diariClinicButton.addActionListener(e -> {
            new diariClinici();
        });
    }
}
