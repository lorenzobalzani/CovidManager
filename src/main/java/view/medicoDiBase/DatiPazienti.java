package view.medicoDiBase;

import controller.DataBaseController;
import model.Cittadino;
import model.OperatoreSanitario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DatiPazienti extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox<Cittadino> patients;
    private JTextArea diario;
    private JButton saveButton;
    private JButton defaultButton;
    private JLabel selectLabel;
    private JTable tabellaDati;
    private JTable tabellaReferti;
    private JScrollPane refertiPanel;
    private JScrollPane statiSalutePanel;
    private final OperatoreSanitario medicoDiBase;

    public DatiPazienti(OperatoreSanitario medicoDiBase) {
        setTitle("Dati pazienti");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 1.5f));
        setVisible(true);
        this.medicoDiBase = medicoDiBase;
        queryPatient();
        queryInfo();
        queryReferti();
        queryDiary();
        statiSalutePanel.setBorder(BorderFactory.createTitledBorder("Tamponi"));
        refertiPanel.setBorder(BorderFactory.createTitledBorder("Ricoveri"));
        patients.addActionListener(e -> {
            queryInfo();
            queryReferti();
            queryDiary();
        });
        defaultButton.addActionListener(e -> queryDiary());
        saveButton.addActionListener(e -> saveDiary());
    }

    private void queryPatient(){
        DataBaseController dataBaseController = new DataBaseController();
        try {
            String statement = "SELECT * FROM CITTADINO" +
                    " WHERE ID_MED = (SELECT ID_MED FROM MEDICO_DI_BASE M" +
                    " WHERE M.CF = '" + medicoDiBase.getCF() + "');";
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

    private void queryInfo() {
        String[] columnNames = {"Esito", "Data"};
        DataBaseController dataBaseController = new DataBaseController();
        String CF = ((Cittadino) Objects.requireNonNull(patients.getSelectedItem())).getCF();
        try {
            String statement = "SELECT * FROM TAMPONE" +
                    " WHERE CF='" + CF + "' ORDER BY data DESC;";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String tipo = rs.getString("esito");
                String data = rs.getString("data");
                tableModel.addRow(new String[]{tipo, data});
            }
            tabellaDati.setModel(tableModel);
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void queryReferti() {
        String[] columnNames = {"Tipo ricovero", "Codice gravità", "Data",
                "Ospedale", "Piano", "Reparto Covid"};
        DataBaseController dataBaseController = new DataBaseController();
        String CF = ((Cittadino) Objects.requireNonNull(patients.getSelectedItem())).getCF();
        try {
            String statement = "SELECT * " +
                    "FROM REFERTO_RICOVERO R JOIN OSPEDALE " +
                    "WHERE CF = '" + CF + "' ORDER BY data DESC, tipo;";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String codiceGravita = rs.getString("codiceGravita");
                String data = rs.getString("data");
                String ospedale = rs.getString("nomeOspedale");
                String numeroPiano = rs.getString("numeroPiano");
                String idRepartoCovid = rs.getString("idReparto");
                tableModel.addRow(new String[]{tipo, codiceGravita, data, ospedale, numeroPiano, idRepartoCovid});
            }
            tabellaReferti.setModel(tableModel);
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
