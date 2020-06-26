package view.medicoDiBase.referti;

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
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class Referti extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JComboBox<Cittadino> patients;
    private JLabel selectLabel;
    private JTable tabellaDati;
    private final OperatoreSanitario medicoDiBase;

    public Referti(OperatoreSanitario medicoDiBase) {
        setTitle("Referti");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        this.medicoDiBase = medicoDiBase;
        queryPatient();
        queryInfo();
        patients.addActionListener(e -> {
            queryInfo();
        });
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
        String[] columnNames = {"Tipo", "Codice gravità", "Data inizio", "Data fine",
        "Ospedale", "Piano", "Reparto Covid"};
        DataBaseController dataBaseController = new DataBaseController();
        String CF = ((Cittadino) Objects.requireNonNull(patients.getSelectedItem())).getCF();
        try {
            String statement = "SELECT * " +
            "FROM REFERTO_RICOVERO R JOIN OSPEDALE " +
            "WHERE CF = '" + CF + "' ORDER BY dataFine DESC;";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String tipo = rs.getString("tipo");
                String codiceGravita = rs.getString("codiceGravita");
                String dataInizio = rs.getString("dataInizio");
                String dataFine = rs.getString("dataFine");
                String ospedale = rs.getString("nomeOspedale");
                String numeroPiano = rs.getString("numeroPiano");
                String idRepartoCovid = rs.getString("idReparto");
                tableModel.addRow(new String[]{tipo, codiceGravita, dataInizio,
                        dataFine, ospedale, numeroPiano, idRepartoCovid});
            }
            tabellaDati.setModel(tableModel);
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
