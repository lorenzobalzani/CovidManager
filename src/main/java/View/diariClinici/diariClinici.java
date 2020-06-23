package View.diariClinici;

import Controller.DataBaseController;
import Model.Cittadino;
import Model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class diariClinici extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox<Cittadino> patients;
    private JTextArea textArea1;
    private JButton salvaButton;
    private JButton defaultButton;
    private JLabel selectLabel;
    private final DataBaseController dataBaseController;
    private final OperatoreSanitario medicoDiBase;

    public diariClinici(OperatoreSanitario medicoDiBase) {
        this.medicoDiBase = medicoDiBase;
        setTitle("Diari clinici");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        dataBaseController = new DataBaseController("localhost", 3306,
                "CovidManager", "root", "root");
        getPatients();
    }

    private void getPatients(){
       Connection connection = dataBaseController.getConnection();
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM CITTADINO WHERE " +
                    "PAZ_CF='" + medicoDiBase.getCF() + "';").executeQuery();
            while (rs.next()) {
                Cittadino cittadino = new Cittadino();
                cittadino.setNome(rs.getString("nome"));
                cittadino.setCognome(rs.getString("cognome"));
                patients.addItem(cittadino);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
