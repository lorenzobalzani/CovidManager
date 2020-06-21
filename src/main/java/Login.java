import javax.swing.*;
import java.awt.*;

public class Login {
    private JPanel panel;
    private JPasswordField passwordField;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;

    public static void main(String[] args) {
        JFrame myFrame = new JFrame("Login Covid19 Manager");
        myFrame.setContentPane(new Login().panel);
        myFrame.setVisible(true);
        myFrame.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()/2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()/4);
    }
}
