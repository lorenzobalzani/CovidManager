package view.medicoDiBase;

import model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;

public class impostazioniMedicoDiBase extends JFrame {
    private JPanel mainPanel;
    private JButton salvaPasswordButton;
    private JButton salvaUsernameButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private OperatoreSanitario medicoDiBase;

    public impostazioniMedicoDiBase(OperatoreSanitario medicoDiBase) throws HeadlessException {
        setTitle("Impostazioni medico di base");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 3);
        setVisible(true);
        this.medicoDiBase = medicoDiBase;
    }
}
