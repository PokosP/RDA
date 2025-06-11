package Proba;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Stavka_kosarice extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JPasswordField passwordField;
    private JTextField textField_1;
    private JTextField textField_2;
    private JTextField pdvField;
    private JTextField ukupnoSPDVField;
    private final JButton btnNewButton_1 = new JButton("Spremi");
    private final JComboBox<String> comboBox_1 = new JComboBox<>();

    public static void main(String[] args) {
        try {
            testDatabaseConnection();
            Stavka_kosarice dialog = new Stavka_kosarice();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testDatabaseConnection() {
        String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
        String user = "ppokos";
        String password = "11";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Uspješno povezan s bazom podataka!");
        } catch (SQLException e) {
            System.err.println("Greška pri povezivanju s bazom:");
            e.printStackTrace();
        }
    }

    public Stavka_kosarice() {
        setTitle("Stavka košarice");
        setBounds(100, 100, 500, 400);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JComboBox<Integer> comboBox = new JComboBox<>();
        comboBox.setBounds(244, 26, 105, 21);
        contentPanel.add(comboBox);

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://ucka.veleri.hr/ppokos?user=ppokos&password=11");
             PreparedStatement ps = con.prepareStatement("SELECT id FROM Korisnik");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                comboBox.addItem(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        textField = new JTextField();
        textField.setEditable(false);
        textField.setBounds(244, 57, 105, 21);
        contentPanel.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Korisničko ime");
        lblNewLabel.setBounds(35, 59, 115, 13);
        contentPanel.add(lblNewLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(244, 88, 105, 19);
        contentPanel.add(passwordField);

        JLabel lblNewLabel_1 = new JLabel("Lozinka");
        lblNewLabel_1.setBounds(35, 82, 79, 13);
        contentPanel.add(lblNewLabel_1);

        textField_1 = new JTextField();
        textField_1.setBounds(244, 117, 105, 19);
        contentPanel.add(textField_1);
        textField_1.setColumns(10);

        JButton btnNewButton = new JButton("Izračunaj ukupnu cijenu");
        btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
        btnNewButton.setBounds(28, 149, 186, 21);
        contentPanel.add(btnNewButton);

        textField_2 = new JTextField();
        textField_2.setEditable(false);
        textField_2.setBounds(244, 146, 105, 19);
        contentPanel.add(textField_2);
        textField_2.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("Odaberi ID korisnika");
        lblNewLabel_2.setBounds(35, 30, 155, 13);
        contentPanel.add(lblNewLabel_2);

        JLabel lblNewLabel_3 = new JLabel("Datum narudžbe (YYYY-MM-DD)");
        lblNewLabel_3.setBounds(35, 117, 227, 13);
        contentPanel.add(lblNewLabel_3);

        JLabel lblNewLabel_4 = new JLabel("Sažetak narudžbe");
        lblNewLabel_4.setBounds(35, 193, 129, 13);
        contentPanel.add(lblNewLabel_4);

        comboBox_1.setBounds(244, 189, 200, 21);
        contentPanel.add(comboBox_1);

        JLabel lblPDV = new JLabel("PDV (25%)");
        lblPDV.setBounds(35, 225, 129, 13);
        contentPanel.add(lblPDV);

        pdvField = new JTextField();
        pdvField.setEditable(false);
        pdvField.setBounds(244, 222, 105, 19);
        contentPanel.add(pdvField);
        pdvField.setColumns(10);

        JLabel lblUkupnoSPDV = new JLabel("Ukupno s PDV-om");
        lblUkupnoSPDV.setBounds(35, 255, 129, 13);
        contentPanel.add(lblUkupnoSPDV);

        ukupnoSPDVField = new JTextField();
        ukupnoSPDVField.setEditable(false);
        ukupnoSPDVField.setBounds(244, 252, 105, 19);
        contentPanel.add(ukupnoSPDVField);
        ukupnoSPDVField.setColumns(10);

        comboBox.addActionListener(e -> {
            Integer selectedId = (Integer) comboBox.getSelectedItem();
            if (selectedId != null) {
                try (Connection con = DriverManager.getConnection(
                        "jdbc:mysql://ucka.veleri.hr/ppokos?user=ppokos&password=11");
                     PreparedStatement ps = con.prepareStatement(
                             "SELECT korisnicko_ime, lozinka FROM Korisnik WHERE id = ?")) {
                    ps.setInt(1, selectedId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            textField.setText(rs.getString("korisnicko_ime"));
                            passwordField.setText(rs.getString("lozinka"));
                        } else {
                            textField.setText("");
                            passwordField.setText("");
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnNewButton.addActionListener(e -> {
            comboBox_1.removeAllItems();
            Integer selectedId = (Integer) comboBox.getSelectedItem();
            String datumUnos = textField_1.getText().trim();

            if (selectedId == null || datumUnos.isEmpty()) {
                textField_2.setText("Nedostaje unos");
                return;
            }

            String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
            String user = "ppokos";
            String password = "11";

            String query = """
                SELECT b.nazivBiljke, b.cijenaBiljke, k.kolicina
                FROM Kosarica k
                JOIN Biljka b ON k.id_biljke = b.id_biljke
                WHERE k.id_korisnika = ?
                  AND DATE(k.datumDodavanja) = ?
                """;

            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setInt(1, selectedId);
                ps.setDate(2, java.sql.Date.valueOf(datumUnos));

                try (ResultSet rs = ps.executeQuery()) {
                    double ukupno = 0.0;
                    while (rs.next()) {
                        String naziv = rs.getString("nazivBiljke");
                        double cijena = rs.getDouble("cijenaBiljke");
                        int kolicina = rs.getInt("kolicina");
                        ukupno += cijena * kolicina;

                        String item = naziv + " - " + String.format("%.2f €", cijena) + " - " + kolicina + " kom";
                        comboBox_1.addItem(item);
                    }

                    double pdv = ukupno * 0.25;
                    double ukupnoSPDV = ukupno + pdv;

                    textField_2.setText(String.format("%.2f €", ukupno));
                    pdvField.setText(String.format("%.2f €", pdv));
                    ukupnoSPDVField.setText(String.format("%.2f €", ukupnoSPDV));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                textField_2.setText("Greška");
            }
        });

        btnNewButton_1.addActionListener(e -> {
            Integer selectedId = (Integer) comboBox.getSelectedItem();
            String datumUnos = textField_1.getText().trim();

            if (selectedId == null || datumUnos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Molimo unesite sve podatke.", "Upozorenje", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
            String user = "ppokos";
            String password = "11";

            String selectQuery = """
                SELECT b.id_biljke, b.nazivBiljke, b.cijenaBiljke, k.kolicina
                FROM Kosarica k
                JOIN Biljka b ON k.id_biljke = b.id_biljke
                WHERE k.id_korisnika = ?
                  AND DATE(k.datumDodavanja) = ?
            """;

            String insertQuery = """
                INSERT INTO Stavka_kosaricee (id_korisnika, id_biljke, kolicina, ukupna_cijena)
                VALUES (?, ?, ?, ?)
            """;

            try (Connection con = DriverManager.getConnection(url, user, password);
                 PreparedStatement selectPs = con.prepareStatement(selectQuery);
                 PreparedStatement insertPs = con.prepareStatement(insertQuery)) {

                selectPs.setInt(1, selectedId);
                selectPs.setDate(2, java.sql.Date.valueOf(datumUnos));

                try (ResultSet rs = selectPs.executeQuery()) {
                    boolean hasData = false;
                    while (rs.next()) {
                        hasData = true;
                        int idBiljke = rs.getInt("id_biljke");
                        int kolicina = rs.getInt("kolicina");
                        double ukupno = rs.getDouble("cijenaBiljke") * kolicina;

                        insertPs.setInt(1, selectedId);
                        insertPs.setInt(2, idBiljke);
                        insertPs.setInt(3, kolicina);
                        insertPs.setDouble(4, ukupno);

                        insertPs.executeUpdate();
                    }
                    if (hasData) {
                        JOptionPane.showMessageDialog(this, "Stavke spremljene u bazu.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Nema stavki za spremiti za uneseni datum.", "Informacija", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Greška pri spremanju stavki.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(e -> dispose());

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(btnNewButton_1);
        buttonPane.add(btnCancel);
        getContentPane().add(buttonPane, BorderLayout.SOUTH);
    }
}
