package view.medicoDiBase;

import controller.DataBaseController;
import model.OperatoreSanitario;
import view.impostazioni;
import view.medicoDiBase.diariClinici.DiariClinici;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicoDiBase extends JFrame {
    private JPanel mainPanel;
    private JLabel riepilogoDatiLabel;
    private JTable tabellaDati;
    private JScrollPane scrollPane1;
    private JTextField cercaTextField;
    private JButton diariClinici;
    private JLabel cercaLabel;
    private JButton cercaButton;
    private JButton impostazioniButton;
    private OperatoreSanitario medicoDiBase;

    public MedicoDiBase(OperatoreSanitario medicoDiBase) {
        setTitle("Gestione medico di base");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.medicoDiBase = medicoDiBase;
        queryPatients("SELECT DISTINCT * FROM CITTADINO C, STATO_SALUTE S" +
                " WHERE ID_MED = (SELECT ID_MED FROM MEDICO_DI_BASE M WHERE M.CF = '" +
                medicoDiBase.getCF() +
                "') AND S.CF = C.CF AND S.data = (SELECT MAX(data)\n" +
                "            FROM STATO_SALUTE S\n" +
                "            WHERE S.CF = C.CF);");
        diariClinici.addActionListener(e -> new DiariClinici(medicoDiBase));
        cercaButton.addActionListener(e -> search(cercaTextField.getText()));
        impostazioniButton.addActionListener(e -> new impostazioni(medicoDiBase));
    }

    private void search(String text) {
        queryPatients("SELECT DISTINCT * FROM CITTADINO C, STATO_SALUTE S" +
                        " WHERE ID_MED = (SELECT ID_MED FROM MEDICO_DI_BASE M" +
                " WHERE M.CF = '" + medicoDiBase.getCF() + "') AND (nome LIKE '%" + text +"%' OR cognome LIKE '%" +
                text + "%' OR tipo LIKE '%" + text + "%' OR data LIKE '%" + text + "%' OR C.CF LIKE '%" +
                text + "%') AND S.CF = C.CF AND S.data = (SELECT MAX(data)\n" +
                " FROM STATO_SALUTE S\n" +
                " WHERE S.CF = C.CF);");
    }

    private void queryPatients(String statement) {
        String[] columnNames = {"CF", "Nome", "Cognome", "Stato salute", "Data"};
        DataBaseController dataBaseController = new DataBaseController();
        try {
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String CF = rs.getString("C.CF");
                String nome = rs.getString("C.nome");
                String cognome = rs.getString("C.cognome");
                String statoSalute = rs.getString("S.tipo");
                String dataStatoSalute = rs.getString("S.data");
                tableModel.addRow(new String[]{CF, nome, cognome, statoSalute,
                        dataStatoSalute});
            }
            tabellaDati.setModel(tableModel);
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
