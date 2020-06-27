package view.epidemiologo;

import controller.DataBaseController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Epidemiologo extends JFrame {
    private JPanel mainPanel;
    private JPanel filterPanel;
    private JPanel etaPanel;
    private JComboBox<String> genereComboBox;
    private JComboBox<String> comuneComboBox;
    private JSpinner etaMinima;
    private JSpinner etaMassima;
    private JTable tabellaDati;
    private JButton queryButton;
    private JButton resetButton;

    private String filter="";

    public Epidemiologo()  {
        setTitle("Gestione Epidemiologo");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtri"));
        etaPanel.setBorder(BorderFactory.createTitledBorder("Intervallo età"));
        genereComboBox.addItem("All");
        genereComboBox.addItem("Femmina");
        genereComboBox.addItem("Maschio");
        comuneComboBox.addItem("All");
        etaMinima.setModel(
                new SpinnerNumberModel(0, 0, 130, 1));
        etaMassima.setModel(
                new SpinnerNumberModel(130, 0, 130, 1));
        queryCities();
        queryDati();
        resetButton.addActionListener(e -> {
            genereComboBox.setSelectedItem("All");
            comuneComboBox.setSelectedItem("All");
            etaMinima.setValue(0);
            etaMassima.setValue(130);
        });
        queryButton.addActionListener(e -> {
            updateFilter();
            queryDati();
        });
    }

    private void queryCities() {
        DataBaseController dataBaseController = new DataBaseController();
        String statement = "SELECT DISTINCT comuneResidenza FROM CITTADINO";
        try {
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            while (rs.next()) {
                comuneComboBox.addItem(rs.getString("comuneResidenza"));
            }
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void queryDati() {
        String[] columnNames = {"Positivi", "Negativi", "Decessi"};
        DataBaseController dataBaseController = new DataBaseController();
        String tamponiQuery = "SELECT esito, COUNT(esito) AS ContaEsito " +
                "FROM TAMPONE T WHERE T.data = (SELECT MAX(T1.data) " +
                "FROM TAMPONE T1 " +
                "WHERE T1.CF = T.CF) " + filter +
                " GROUP BY esito";
        String decessiQuery = "SELECT tipo, COUNT(tipo) AS ContaEsito " +
                "FROM REFERTO R WHERE R.data = (SELECT MAX(R1.data) " +
                "FROM REFERTO R1 " +
                "WHERE R1.CF = R.CF) " + filter +
                " GROUP BY tipo";
        try {
            ResultSet rsTamponi = dataBaseController.getConnection().prepareStatement(tamponiQuery).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            rsTamponi.next();
            String negativi = rsTamponi.getString("ContaEsito");
            rsTamponi.next();
            String positivi = rsTamponi.getString("ContaEsito");

            ResultSet rsReferti = dataBaseController.getConnection().prepareStatement(decessiQuery).executeQuery();
            rsReferti.next();
            String decessi = rsReferti.getString("ContaEsito");

            tableModel.addRow(new String[]{positivi, negativi, decessi});
            tabellaDati.setModel(tableModel);
            dataBaseController = null;
            rsTamponi.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void updateFilter() {
        String genere = String.valueOf(genereComboBox.getSelectedItem());
        String comune = String.valueOf(comuneComboBox.getSelectedItem());
        genere = (genere.equals("All")) ? "" : genere;
        comune = (comune.equals("All")) ? "" : comune;
        String etaMin = String.valueOf(etaMinima.getValue());
        String etaMax = String.valueOf(etaMassima.getValue());
    }
}
