package view.epidemiologo;

import controller.DataBaseController;

import javax.swing.*;
import java.awt.*;
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
                new SpinnerNumberModel(100, 0, 130, 1));
        queryCities();
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
}
