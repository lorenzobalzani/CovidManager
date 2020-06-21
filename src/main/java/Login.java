import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;

    public Login() {
        setTitle("Login Covid19 Manager");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        loginButton.addActionListener(e -> {
            new Epidemiologo();
            dispose();
        });
    }

    public static void main(String[] args) {
        new Login();
    }

}
