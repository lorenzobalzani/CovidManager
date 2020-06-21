import javax.swing.*;

public class Epidemiologo {
    private JPanel mainPanel;
    private JLabel riepilogoDatiLabel;
    private JTable table1;

    public static void main(String[] args){
        JFrame frame = new JFrame("test");
        frame.setContentPane(new Epidemiologo().mainPanel);
        frame.setVisible(true);
    }

}
