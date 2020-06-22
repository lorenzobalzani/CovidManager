package medicoDiBase;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

public class MedicoDiBase extends JFrame {
    private JPanel mainPanel;
    private JLabel riepilogoDatiLabel;
    private JLabel cercaLabel;
    private JTable tabellaDati;
    private JTextField textField2;
    private JScrollPane scrollPane1;

    public MedicoDiBase() {
        setTitle("Gestione medico di base");
        setContentPane(mainPanel);
        setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2,
                (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        TableModel dataModel = new
                AbstractTableModel() {
                    public int getColumnCount() {
                        return 10;
                    }

                    public int getRowCount() {
                        return 10;
                    }

                    public Object getValueAt(int row, int col) {
                        return row * col;
                    }
                };
        tabellaDati.setModel(dataModel);
    }
}
