package view.epidemiologo;

import controller.DataBaseController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat;
    private List<String> cities = new ArrayList<>();

    public Epidemiologo()  {
        setTitle("Gestione Epidemiologo");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"), Locale.ITALY);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dataMax.setText(simpleDateFormat.format(calendar.getTime()));
        genereComboBox.addItem("All");
        genereComboBox.addItem("Femmina");
        genereComboBox.addItem("Maschio");
        comuneComboBox.addItem("All");
        queryCities();
        queryDati();
        updateFilter();
        resetButton.addActionListener(e -> {
            resetFilter();
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
                cities.add(rs.getString("comuneResidenza"));
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
        try {
            String negativi = "";
            String positivi = "";
            String decessi = "";
            ResultSet rsTamponi = dataBaseController.getConnection().prepareStatement(tamponiQuery).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            if (rsTamponi.next()) {
                negativi = rsTamponi.getString("ContaEsito");
            } else {
                negativi = String.valueOf(0);
            }
            if (rsTamponi.next()) {
                positivi = rsTamponi.getString("ContaEsito");
            } else {
                positivi = String.valueOf(0);
            }
            ResultSet rsReferti = dataBaseController.getConnection().prepareStatement(decessiQuery).executeQuery();
            if (rsReferti.next()) {
                decessi = rsReferti.getString("ContaEsito");
            } else {
                decessi = String.valueOf(0);
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
        if ("All".equals(genere)) {
            genere = " AND genere LIKE '%'";
        } else {
            genere = "(genere = '" + genere + "')";
        }
        if (comune.equals("All")) {
            comune = " AND comuneResidenza LIKE '%'";
        } else {
            comune = " AND (comuneResidenza='" + comune + "')";
        }
        filter = " AND dataDiNascita >= '" + dataMin.getText() + "' AND dataDiNascita <= '" +
                dataMax.getText() + "' AND " + genere + comune;
    }

    private void resetFilter() {
        genereComboBox.setSelectedItem("All");
        comuneComboBox.setSelectedItem("All");
        dataMin.setText("1900-01-01");
        dataMax.setText(simpleDateFormat.format(calendar.getTime()));
    }
}
