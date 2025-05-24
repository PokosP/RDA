package plantie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class odobri_zahtjeve extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public odobri_zahtjeve() {
        setTitle("Odobravanje zahtjeva");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        model = new DefaultTableModel(new Object[]{"Korisničko ime", "Zahtjev", "Odobri"}, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return (column == 2) ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnSpremi = new JButton("Spremi promjene");
        btnSpremi.addActionListener(e -> obradiZahtjeve());
        add(btnSpremi, BorderLayout.SOUTH);

        ucitajZahtjeve();
    }

    private void ucitajZahtjeve() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT korisnicko_ime, zahtjev FROM Zahtjevi_za_admina")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("korisnicko_ime"),
                        rs.getString("zahtjev"),
                        false
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
        }
    }

    private void obradiZahtjeve() {
        ArrayList<String> odobreniKorisnici = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean odobreno = (Boolean) model.getValueAt(i, 2);
            if (odobreno) {
                String korisnickoIme = (String) model.getValueAt(i, 0);
                odobreniKorisnici.add(korisnickoIme);
            }
        }

        if (odobreniKorisnici.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nijedan zahtjev nije odobren.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11")) {

            for (String korisnik : odobreniKorisnici) {
                // Postavi admin = 1
                PreparedStatement update = conn.prepareStatement("UPDATE Korisnik SET admin = 1 WHERE korisnicko_ime = ?");
                update.setString(1, korisnik);
                update.executeUpdate();
                update.close();

                // Obriši zahtjev
                PreparedStatement delete = conn.prepareStatement("DELETE FROM Zahtjevi_za_admina WHERE korisnicko_ime = ?");
                delete.setString(1, korisnik);
                delete.executeUpdate();
                delete.close();
            }

            JOptionPane.showMessageDialog(this, "Zahtjevi uspješno obrađeni.");
            dispose();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri obradi zahtjeva: " + e.getMessage());
        }
    }
}
