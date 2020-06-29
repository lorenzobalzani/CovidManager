package view;

import controller.DataBaseController;
import model.Cittadino;
import model.OperatoreSanitario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ContactTracingMonitor extends JFrame   {
    private JPanel mainPanel;
    private JTable contattiTable;
    private JButton impostazioniButton;

    public ContactTracingMonitor(OperatoreSanitario operatoreSanitario) {
        setTitle("Gestione contact tracing");
        setContentPane(mainPanel);
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5f),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        impostazioniButton.addActionListener(e -> new Impostazioni(operatoreSanitario));
        queryContacts();
    }

    private void queryContacts() {
        String[] columnNames = {"Data inizio", "Data fine",
            "Durata esposizione", "Posizione"};
        DataBaseController dataBaseController = new DataBaseController();
        try {
            String statement = "SELECT *" +
                    " FROM CONTATTO_REGISTRATO";
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String dataInizio = rs.getString("dataInizio");
                String dataFine = rs.getString("dataFine");
                String durataEsposizione = rs.getString("durataEsposizione");
                String positionLink = "http://www.google.com/maps/place/" +
                    rs.getString("latitudine") + "," +
                        rs.getString("longitudine");
                tableModel.addRow(new Object[]{dataInizio,
                    dataFine, durataEsposizione, positionLink});
            }
            contattiTable.setModel(tableModel);
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
