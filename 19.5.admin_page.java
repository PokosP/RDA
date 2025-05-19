package plantie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class admin_page extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public admin_page() {
        setTitle("Administratorska kontrolna ploča");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        // Naslov
        JLabel naslov = new JLabel("Dobrodošli, administratoru", SwingConstants.CENTER);
        naslov.setBounds(0, 0, 786, 30);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(naslov);

        // Model i tabela
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 750, 330);
        getContentPane().add(scrollPane);

        // Gumbi
        JButton btnKorisnici = new JButton("Prikaži korisnike");
        btnKorisnici.setBounds(20, 50, 180, 35);
        getContentPane().add(btnKorisnici);

        JButton btnBiljke = new JButton("Prikaži biljke");
        btnBiljke.setBounds(220, 50, 180, 35);
        getContentPane().add(btnBiljke);

        JButton btnZahtjevi = new JButton("Prikaži zahtjeve");
        btnZahtjevi.setBounds(420, 50, 180, 35);
        getContentPane().add(btnZahtjevi);

        // Akcije
        btnKorisnici.addActionListener(e -> prikaziKorisnike());
        btnBiljke.addActionListener(e -> prikaziBiljke());
        btnZahtjevi.addActionListener(e -> prikaziZahtjeve());

        // Prikaži korisnike po defaultu na startu
        prikaziKorisnike();
    }

    private Connection spojiNaBazu() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11");
    }

    private void prikaziKorisnike() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{"Ime", "Prezime", "Korisničko ime", "Admin"});

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ime, prezime, korisnicko_ime, admin FROM Korisnik")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("korisnicko_ime"),
                        rs.getInt("admin") == 1 ? "DA" : "NE"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška prilikom učitavanja korisnika: " + e.getMessage());
        }
    }

    private void prikaziBiljke() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{"Naziv", "Opis", "Vrsta"});

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nazivBiljke, vrstaBiljke, opisBiljke FROM Biljka")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nazivBiljke"),
                        rs.getString("vrstaBiljke"),
                        rs.getString("opisBiljke")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška prilikom učitavanja biljaka: " + e.getMessage());
        }
    }

    private void prikaziZahtjeve() {
        model.setRowCount(0);
        model.setColumnIdentifiers(new Object[]{"Korisnik ID", "Zahtjev"});

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT korisnik_id, zahtjev FROM Zahtjevi_za_admina")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("korisnik_id"),
                        rs.getString("zahtjev")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška prilikom učitavanja zahtjeva: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            admin_page frame = new admin_page();
            frame.setVisible(true);
        });
    }
}
