package View.epidemiologo;

import javax.swing.*;
import java.awt.*;

public class Epidemiologo extends JFrame {
    private JPanel mainPanel;
    private JLabel riepilogoDatiLabel;
    private JTable datiPazienti;
    private JLabel cercaLabel;
    private JTextField cercaDati;

    public Epidemiologo()  {
        setTitle("Gestione View.epidemiologo");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
