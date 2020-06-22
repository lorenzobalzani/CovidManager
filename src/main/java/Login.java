

import epidemiologo.Epidemiologo;
import medicoDiBase.MedicoDiBase;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login extends JFrame {
    private JPanel mainPanel;
    private JPasswordField passwordField;
    private JLabel welcomeLabel;
    private JLabel usernameLabel;
    private JButton loginButton;
    private JTextField usernameField;
    private JLabel passwordLabel;
    private final String connectionUrl = "jdbc:mysql://localhost:3306/users?useUnicode=true&characterEncoding=UTF-8&user=root&password=root";

    public Login() throws ClassNotFoundException, SQLException {
        setTitle("Login Covid19 Manager");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 4);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectionUrl);
        loginButton.addActionListener(e -> {
            ResultSet rs = null;
            new Epidemiologo();
            new MedicoDiBase();
            try {
                rs = conn.prepareStatement("").executeQuery();
                while(rs.next()){
                    String s = rs.getString(1);
                    System.out.println(s);
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            dispose();
        });
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        new Login();
    }

}
