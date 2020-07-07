package view.medicoDiBase;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import controller.DataBaseController;
import model.OperatoreSanitario;
import view.Impostazioni;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicoDiBase extends JFrame {
    private JPanel mainPanel;
    private JTable tabellaDati;
    private JScrollPane pazienti;
    private JTextField cercaTextField;
    private JButton datiPazientiButton;
    private JLabel cercaLabel;
    private JButton cercaButton;
    private JButton impostazioniButton;
    private OperatoreSanitario medicoDiBase;

    public MedicoDiBase(OperatoreSanitario medicoDiBase) {
        setTitle("Gestione medico di base");
        setContentPane(mainPanel);
        setSize((int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 1.5f),
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pazienti.setBorder(BorderFactory.createTitledBorder("Pazienti in cura"));
        this.medicoDiBase = medicoDiBase;
        queryPatients("SELECT * FROM CITTADINO C" +
                " WHERE ID_MED = (SELECT ID_MED FROM MEDICO_DI_BASE M WHERE M.CF = '" +
                medicoDiBase.getCF() +
                "')");
        datiPazientiButton.addActionListener(e -> new DatiPazienti(medicoDiBase));
        cercaTextField.addActionListener(e -> search(cercaTextField.getText()));
        cercaButton.addActionListener(e -> search(cercaTextField.getText()));
        impostazioniButton.addActionListener(e -> new Impostazioni(medicoDiBase));
    }

    private void search(String text) {
        queryPatients("SELECT * FROM CITTADINO C" +
                " WHERE ID_MED = (SELECT ID_MED FROM MEDICO_DI_BASE M" +
                " WHERE M.CF = '" + medicoDiBase.getCF() + "') AND (nome LIKE '%" + text + "%' OR cognome LIKE '%" +
                text + "%' OR telefono LIKE '%" + text + "' OR comuneResidenza LIKE '%" + text + "')");
    }

    private void queryPatients(String statement) {
        String[] columnNames = {"CF", "Nome", "Cognome", "Comune", "Telefono"};
        DataBaseController dataBaseController = new DataBaseController();
        try {
            ResultSet rs = dataBaseController.getConnection().prepareStatement(statement).executeQuery();
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
            while (rs.next()) {
                String CF = rs.getString("C.CF");
                String nome = rs.getString("C.nome");
                String cognome = rs.getString("C.cognome");
                String comuneResidenza = rs.getString("C.comuneResidenza");
                String telefono = rs.getString("C.telefono");
                tableModel.addRow(new String[]{CF, nome, cognome, comuneResidenza, telefono});
            }
            tabellaDati.setModel(tableModel);
            dataBaseController = null;
            rs.close();
        } catch (SQLException throwables) {
            JOptionPane.showMessageDialog(this,
                    "Errore provando a ottenere i pazienti del medico di base con CF: "
                            + medicoDiBase.getCF(),
                    "Errore",
                    JOptionPane.WARNING_MESSAGE);
            throwables.printStackTrace();
        }
    }

}
