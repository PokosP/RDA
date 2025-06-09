package plantie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class admin_page extends JFrame {
    private DefaultTableModel model;
    private JTable table;
    private JButton btnSpremi;  

    public admin_page() {
        setTitle("Administratorska kontrolna ploča");
        setSize(880, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel naslov = new JLabel("Dobrodošli, administratoru", SwingConstants.CENTER);
        naslov.setBounds(0, 0, 864, 30);
        naslov.setFont(new Font("Arial", Font.BOLD, 20));
        getContentPane().add(naslov);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 100, 820, 330);
        getContentPane().add(scrollPane);

        JButton btnKorisnici = new JButton("Prikaži korisnike");
        btnKorisnici.setBounds(20, 50, 180, 35);
        getContentPane().add(btnKorisnici);

        JButton btnBiljke = new JButton("Prikaži biljke");
        btnBiljke.setBounds(220, 50, 180, 35);
        getContentPane().add(btnBiljke);

        JButton btnZahtjevi = new JButton("Prikaži zahtjeve");
        btnZahtjevi.setBounds(420, 50, 180, 35);
        getContentPane().add(btnZahtjevi);

        JButton btnOdobravanje = new JButton("Odobravanje zahtjeva");
        btnOdobravanje.setBounds(620, 50, 220, 35);
        getContentPane().add(btnOdobravanje);

        btnSpremi = new JButton("Spremi promjene");
        btnSpremi.setBounds(350, 440, 180, 30);
        btnSpremi.setVisible(false);  // na početku skriven
        getContentPane().add(btnSpremi);

        btnKorisnici.addActionListener(e -> {
            prikaziKorisnike();
            btnSpremi.setVisible(false);  // sakrij gumb kod korisnika
        });
        btnBiljke.addActionListener(e -> {
            prikaziBiljke();
            btnSpremi.setVisible(false);  // sakrij gumb kod biljaka
        });
        btnZahtjevi.addActionListener(e -> {
            prikaziZahtjeve();
            btnSpremi.setVisible(false);  // sakrij gumb kod zahtjeva
        });
        btnOdobravanje.addActionListener(e -> {
            prikaziZahtjeveZaOdobravanje();
            btnSpremi.setVisible(true);  // prikaži gumb samo kod odobravanja zahtjeva
        });
        btnSpremi.addActionListener(e -> spremiPromjeneOdobravanja());
    }

    private Connection spojiNaBazu() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11");
    }

    private void prikaziKorisnike() {
        model = new DefaultTableModel(new Object[]{"Ime", "Prezime", "Korisničko ime", "Admin"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT ime, prezime, korisnicko_ime, admin FROM Korisnik")) {

            while (rs.next()) {
                String adminTekst = rs.getInt("admin") == 1 ? "Da" : "Ne";
                model.addRow(new Object[]{
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("korisnicko_ime"),
                        adminTekst
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju korisnika: " + e.getMessage());
        }
    }

    private void prikaziBiljke() {
        model = new DefaultTableModel(new Object[]{"Naziv", "Opis", "Vrsta", "Cijena"}, 0);
        table.setModel(model);

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT nazivBiljke, vrstaBiljke, opisBiljke, cijenaBiljke FROM Biljka")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("nazivBiljke"),
                        rs.getString("opisBiljke"),
                        rs.getString("vrstaBiljke"),
                        rs.getString("cijenaBiljke")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju biljaka: " + e.getMessage());
        }
    }

    private void prikaziZahtjeve() {
        model = new DefaultTableModel(new Object[]{"Korisničko ime", "Zahtjev"}, 0);
        table.setModel(model);

        try (Connection conn = spojiNaBazu();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT korisnicko_ime, zahtjev FROM Zahtjevi_za_admina")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("korisnicko_ime"),
                        rs.getString("zahtjev")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju zahtjeva: " + e.getMessage());
        }
    }

    private void prikaziZahtjeveZaOdobravanje() {
        model = new DefaultTableModel(new Object[]{"Korisničko ime", "Zahtjev", "Odobri"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Boolean.class;
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        table.setModel(model);

        try (Connection conn = spojiNaBazu();
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
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju zahtjeva za odobravanje: " + e.getMessage());
        }
    }

    private void spremiPromjeneOdobravanja() {
        if (model.getColumnCount() != 3) {
            JOptionPane.showMessageDialog(this, "Trenutno niste u prikazu odobravanja zahtjeva.");
            return;
        }

        try (Connection conn = spojiNaBazu()) {
            conn.setAutoCommit(false);

            PreparedStatement psUpdate = conn.prepareStatement(
                    "UPDATE Korisnik SET admin = 1 WHERE korisnicko_ime = ?");
            PreparedStatement psDelete = conn.prepareStatement(
                    "DELETE FROM Zahtjevi_za_admina WHERE korisnicko_ime = ? AND zahtjev = ?");

            for (int i = 0; i < model.getRowCount(); i++) {
                String korisnickoIme = (String) model.getValueAt(i, 0);
                String zahtjev = (String) model.getValueAt(i, 1);
                boolean odobreno = (Boolean) model.getValueAt(i, 2);

                if (odobreno) {
                    // samo ako je odobreno postavi admin i obrise zahtjev
                    psUpdate.setString(1, korisnickoIme);
                    psUpdate.addBatch();

                    psDelete.setString(1, korisnickoIme);
                    psDelete.setString(2, zahtjev);
                    psDelete.addBatch();
                }
            }

            psUpdate.executeBatch();
            psDelete.executeBatch();
            conn.commit();

            JOptionPane.showMessageDialog(this, "Promjene su uspješno spremljene.");

            prikaziZahtjeveZaOdobravanje(); 

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Greška pri spremanju promjena: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            admin_page frame = new admin_page();
            frame.setVisible(true);
        });
    }
}