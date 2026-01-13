package gui;

import model.Auto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class GarageGui extends JFrame {

    private ArrayList<Auto> autoListe = new ArrayList<>();

    private JTextField txtMarke, txtModel, txtBaujahr, txtPreis, txtFilter;
    private JCheckBox chkElektro;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public GarageGui() {

        setTitle("Garage");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initObjekte();
        initGUI();

        pack();                         // ✅
        setLocationRelativeTo(null);    // ✅ zentrieren
        setMinimumSize(new Dimension(800, 550)); // ✅ optional, aber gut
    }

    public void initObjekte() {
        autoListe.add(new Auto("BMW", "M3", 2018, 25000, false));
        autoListe.add(new Auto("Tesla", "Model Y",2022, 45000, true));
        autoListe.add(new Auto("VW", "Polo", 1992, 4000, false));
        autoListe.add(new Auto("Ferrari","812 Superfast", 2016, 180000, false));
        autoListe.add(new Auto("Audi", "RS6", 1990, 25000, false));
        autoListe.add(new Auto("Xiaomi","SU7", 2025, 40000, true));
    }

    private void initGUI() {
        ((JPanel) getContentPane()).setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        );

        txtMarke = new JTextField();
        txtModel = new JTextField();
        txtBaujahr = new JTextField();
        txtPreis = new JTextField();
        chkElektro = new JCheckBox();

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        form.add(new JLabel("Marke:"));
        form.add(txtMarke);
        form.add(new JLabel("Model"));
        form.add(txtModel);
        form.add(new JLabel("Baujahr:"));
        form.add(txtBaujahr);
        form.add(new JLabel("Preis:"));
        form.add(txtPreis);
        form.add(new JLabel("Elektro:"));
        form.add(chkElektro);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));

        JButton btnSave = new JButton("Speichern");
        btnSave.addActionListener(e -> speichern());
        getRootPane().setDefaultButton(btnSave);

        JButton btnOldtimer = new JButton("Oldtimer anzeigen");
        btnOldtimer.addActionListener(e -> filterOldtimer());

        JButton btnAlle = new JButton("Alle anzeigen");
        btnAlle.addActionListener(e -> resetFilter());

        JButton btnWert = new JButton("Garagenwert");
        btnWert.addActionListener(e -> zeigeGaragenwert());

        JButton btnDelete = new JButton("Ausgewähltes löschen");
        btnDelete.addActionListener(e -> loeschen());

        buttons.add(btnSave);
        buttons.add(btnOldtimer);
        buttons.add(btnAlle);
        buttons.add(btnWert);
        buttons.add(btnDelete);

        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.add(form, BorderLayout.NORTH);
        top.add(buttons, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
                new String[]{"Marke","Model", "Baujahr", "Preis", "Elektro"}, 0
        );
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getColumnModel().getColumn(4).setCellRenderer((t, value, sel, focus, row, col) -> {
            JLabel l = new JLabel();
            l.setOpaque(true);

            boolean elektro = Boolean.TRUE.equals(value); // null-sicher
            l.setText(elektro ? "Ja" : "Nein");

            if (sel) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                l.setBackground(t.getBackground());
                l.setForeground(t.getForeground());
            }

            l.setHorizontalAlignment(SwingConstants.CENTER);
            return l;
        });

        table.getColumnModel().getColumn(3).setCellRenderer((t, value, sel, focus, row, col) -> {
            JLabel l = new JLabel();
            l.setOpaque(true);

            if (value instanceof Number) {
                l.setText(String.format("%.2f €", ((Number) value).doubleValue()));
            }

            if (sel) {
                l.setBackground(t.getSelectionBackground());
                l.setForeground(t.getSelectionForeground());
            } else {
                l.setBackground(t.getBackground());
                l.setForeground(t.getForeground());
            }

            l.setHorizontalAlignment(SwingConstants.RIGHT);
            return l;
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        txtFilter = new JTextField(10);
        JButton btnFilter = new JButton("Filter Marke");

        btnFilter.addActionListener(e ->
                sorter.setRowFilter(RowFilter.regexFilter(txtFilter.getText(), 0))
        );

        filterPanel.add(new JLabel("Marke filtern:"));
        filterPanel.add(txtFilter);
        filterPanel.add(btnFilter);

        add(filterPanel, BorderLayout.SOUTH);

        aktualisiereTabelle();
    }

    private void speichern() {
        String marke = txtMarke.getText().trim();
        String model = txtModel.getText().trim();
        String baujahrText = txtBaujahr.getText().trim();
        String preisText = txtPreis.getText().trim();

        if (marke.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Bitte eine Marke eingeben.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (model.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Bitte ein Model eingeben.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int baujahr;
        double preis;

        try {
            baujahr = Integer.parseInt(baujahrText);
            preis = Double.parseDouble(preisText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Baujahr und Preis müssen Zahlen sein.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int aktuellesJahr = java.time.Year.now().getValue();
        if (baujahr < 1886 || baujahr > aktuellesJahr) {
            JOptionPane.showMessageDialog(this,
                    "Baujahr muss zwischen 1886 und " + aktuellesJahr + " liegen.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (preis <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Preis muss größer als 0 sein.",
                    "Fehler",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Auto auto = new Auto(
                marke,
                model,
                baujahr,
                preis,
                chkElektro.isSelected()
        );
        autoListe.add(auto);
        aktualisiereTabelle();

        JOptionPane.showMessageDialog(this,
                "Auto gespeichert.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);

        txtMarke.setText("");
        txtModel.setText("");
        txtBaujahr.setText("");
        txtPreis.setText("");
        chkElektro.setSelected(false);
        txtMarke.requestFocusInWindow();
    }



        private void filterOldtimer() {
        sorter.setRowFilter(
                RowFilter.numberFilter(RowFilter.ComparisonType.BEFORE, 1996, 2)
        );
    }

    private void aktualisiereTabelle() {
        tableModel.setRowCount(0);
        for (Auto a : autoListe) {
            tableModel.addRow(new Object[]{
                    a.getMarke(),
                    a.getModel(),
                    a.getBaujahr(),
                    a.getPreis(),
                    a.isElektro()
            });
        }
    }

    private void resetFilter() {
        sorter.setRowFilter(null);
    }


    private void zeigeGaragenwert() {
        if (autoListe.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Keine Autos vorhanden.");
            return;
        }

        double summe = 0.0;
        for (Auto a : autoListe) {
            summe += a.getPreis();
        }

        JOptionPane.showMessageDialog(this,
                "Garagenwert: " + String.format("%.2f €", summe),
                "Auswertung",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void loeschen() {
        int viewRow = table.getSelectedRow();

        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "Bitte zuerst ein Auto in der Tabelle auswählen.",
                    "Hinweis",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);
        autoListe.remove(modelRow);
        aktualisiereTabelle();
    }
}
