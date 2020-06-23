package View.diariClinici;

import Controller.DataBaseController;
import Model.Cittadino;
import Model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiariClinici extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox<Cittadino> patients;
    private JTextArea textArea1;
    private JButton salvaButton;
    private JButton defaultButton;
    private JLabel selectLabel;
    private final DataBaseController dataBaseController;
    private final OperatoreSanitario medicoDiBase;

    public DiariClinici(OperatoreSanitario medicoDiBase) {
        this.medicoDiBase = medicoDiBase;
        dataBaseController = new DataBaseController("localhost", 3306, "CovidManager",
                "root", "root");
        getPatients();
        setTitle("Diari clinici");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
    }

    private void getPatients(){
       Connection connection = dataBaseController.getConnection();
        try {
            String statement = "SELECT * FROM CITTADINO" +
                    " WHERE PAZ_CF='" +
                    medicoDiBase.getCF() +
                    "';";
            ResultSet rs = connection.prepareStatement(statement).executeQuery();
            while (rs.next()) {
                Cittadino cittadino = new Cittadino();
                cittadino.setNome(rs.getString("nome"));
                cittadino.setCognome(rs.getString("cognome"));
                System.out.println(rs.getString("PAZ_CF"));
                patients.addItem(cittadino);
            }
            connection.close();
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
