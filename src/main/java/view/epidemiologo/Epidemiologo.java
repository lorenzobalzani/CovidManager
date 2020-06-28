package view.epidemiologo;

import controller.DataBaseController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Epidemiologo extends JFrame {
    private JPanel mainPanel;
    private JPanel filterPanel;
    private JPanel etaPanel;
    private JComboBox<String> genereComboBox;
    private JComboBox<String> comuneComboBox;
    private JTable tabellaDati;
    private JButton queryButton;
    private JButton resetButton;
    private JTextField dataMin;
    private JTextField dataMax;

    private String filter="";

    public Epidemiologo()  {
        setTitle("Gestione Epidemiologo");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtri"));
        etaPanel.setBorder(BorderFactory.createTitledBorder("Intervallo date di nascita"));
        genereComboBox.addItem("All");
        genereComboBox.addItem("Femmina");
        genereComboBox.addItem("Maschio");
        comuneComboBox.addItem("All");
        queryCities();
        queryDati();
        updateFilter();
        resetButton.addActionListener(e -> {
            genereComboBox.setSelectedItem("All");
            comuneComboBox.setSelectedItem("All");
            dataMin.setText("1900-01-01");
            dataMax.setText("2015-01-01");
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
        String tamponiQuery = "SELECT DISTINCT esito, COUNT(esito) AS ContaEsito " +
                "FROM TAMPONE T, CITTADINO C WHERE T.data = (SELECT MAX(T1.data) " +
                "FROM TAMPONE T1 " +
                "WHERE T1.CF = T.CF AND C.CF = T1.CF ) " + filter +
                " GROUP BY esito";
        String decessiQuery = "SELECT DISTINCT tipo, COUNT(tipo) AS ContaEsito " +
                "FROM REFERTO R, CITTADINO C WHERE R.data = (SELECT MAX(R1.data) " +
                "FROM REFERTO R1 " +
                "WHERE R1.CF = R.CF AND C.CF = R1.CF) " + filter +
                " GROUP BY tipo";
        System.out.println(tamponiQuery);
        try {
            String negativi = "";
            String positivi = "";
            String decessi = "";
            ResultSet rsTamponi = dataBaseController.getConnection().prepareStatement(tamponiQuery).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            if (rsTamponi.next()) {
                negativi = rsTamponi.getString("ContaEsito");
            }
            if (rsTamponi.next()) {
                positivi = rsTamponi.getString("ContaEsito");
            }
            ResultSet rsReferti = dataBaseController.getConnection().prepareStatement(decessiQuery).executeQuery();
            if (rsReferti.next()) {
                decessi = rsReferti.getString("ContaEsito");
            }
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
        switch (genere) {
            case "All":
                genere = "(genere = 'Maschio'" +
                        " OR genere = 'Femmina')";
                break;
            case "Femmina":
                genere = "(genere = 'Femmina')";
                break;
            case "Maschio":
                genere = "(genere = 'Maschio')";
                break;
        }
        comune = (comune.equals("All")) ? "" : comune;
        filter = " AND dataDiNascita >= '" + dataMin.getText() + "' AND dataDiNascita <= '" +
                dataMax.getText() + "' AND " + genere + comune;
    }
}
