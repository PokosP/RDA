package plantie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class prikaz_zahtjeva extends JFrame {

    public prikaz_zahtjeva() {
        setTitle("Pregled zahtjeva za admina");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Tablica i model
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("ID zahtjeva");
        model.addColumn("Korisničko ime");
        model.addColumn("Zahtjev");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id_zahtjeva, korisnicko_ime, zahtjev FROM Zahtjevi_za_admina")) {

            while (rs.next()) {
                int id = rs.getInt("id_zahtjeva");
                String korisnicko_ime = rs.getString("korisnicko_ime");
                String zahtjev = rs.getString("zahtjev");
                model.addRow(new Object[]{id, korisnicko_ime, zahtjev});
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška: " + e.getMessage());
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new prikaz_zahtjeva().setVisible(true);
        });
    }
}
