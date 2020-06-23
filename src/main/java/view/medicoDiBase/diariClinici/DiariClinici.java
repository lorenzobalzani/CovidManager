package view.medicoDiBase.diariClinici;

import controller.DataBaseController;
import model.Cittadino;
import model.OperatoreSanitario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DiariClinici extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox<Cittadino> patients;
    private JTextArea diario;
    private JButton saveButton;
    private JButton defaultButton;
    private JLabel selectLabel;
    private final OperatoreSanitario medicoDiBase;

    public DiariClinici(OperatoreSanitario medicoDiBase) {
        setTitle("Diari clinici");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        this.medicoDiBase = medicoDiBase;
        getPatients();
        queryDiary();
        patients.addActionListener(e -> queryDiary());
        defaultButton.addActionListener(e -> queryDiary());
        saveButton.addActionListener(e -> saveDiary());
    }

    private void getPatients(){
        DataBaseController dataBaseController = new DataBaseController();
        try {
            String statement = "SELECT * FROM CITTADINO" +
                    " WHERE PAZ_CF='" +
                    medicoDiBase.getCF() +
                    "';";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            while (rs.next()) {
                Cittadino cittadino = new Cittadino();
                cittadino.setCF(rs.getString("CF"));
                cittadino.setNome(rs.getString("nome"));
                cittadino.setCognome(rs.getString("cognome"));
                patients.addItem(cittadino);
            }
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void queryDiary(){
        DataBaseController dataBaseController = new DataBaseController();
        String CF = ((Cittadino) Objects.requireNonNull(patients.getSelectedItem())).getCF();
        try {
            String statement = "SELECT * FROM DIARIO_CLINICO" +
                    " WHERE CF='" + CF + "';";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            diario.setText("");
            while (rs.next()) {
                diario.setText(rs.getString("testoDiario"));
            }
            rs.close();
            dataBaseController = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void saveDiary() {
        DataBaseController dataBaseController = new DataBaseController();
        String CF = ((Cittadino) Objects.requireNonNull(patients.getSelectedItem())).getCF();
        Connection connection = dataBaseController.getConnection();
        try {
            String check = "SELECT * FROM DIARIO_CLINICO WHERE `CF` = '" + CF + "';";
            ResultSet rs = connection.prepareStatement(check).executeQuery();
            rs.beforeFirst();
            rs.last();
            String statement;
            if (rs.getRow()!=0) {
                statement = "UPDATE `DIARIO_CLINICO` SET `testoDiario` = '" +
                        diario.getText() + "' WHERE `CF` = '" + CF +"';";
            } else {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                statement = "INSERT INTO DIARIO_CLINICO (CF, dataCreazione, testoDiario)" +
                        " VALUES ('" + CF + "', '" + simpleDateFormat.format(calendar.getTime()) + "', '"
                        + diario.getText() + "');";
            }
            connection.prepareStatement(statement).executeUpdate();
            dataBaseController = null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
