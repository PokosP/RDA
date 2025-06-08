package plantie;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Kosarica {

    private JFrame frmKoarice;
    private JComboBox<String> comboBox; // za biljke
    private JComboBox<KorisnikItem> comboBoxKorisnici; // za korisnike s ID i imenom
    private JTextArea textArea;
    private JTextField quantityField;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Kosarica window = new Kosarica();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Kosarica() {
        initialize();
        loadComboBoxData();
        loadKorisnici();
    }

    private void initialize() {
        frmKoarice = new JFrame("Košarice");
        frmKoarice.setBounds(100, 100, 500, 400);
        frmKoarice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmKoarice.getContentPane().setLayout(new BorderLayout());

        Font font = new Font("SansSerif", Font.PLAIN, 16);

        JPanel topPanel = new JPanel();

        comboBoxKorisnici = new JComboBox<>();
        comboBoxKorisnici.setFont(font);
        topPanel.add(new JLabel("Korisnik:"));
        topPanel.add(comboBoxKorisnici);

        comboBox = new JComboBox<>();
        comboBox.setFont(font);
        topPanel.add(new JLabel("Biljka:"));
        topPanel.add(comboBox);

        quantityField = new JTextField(5);
        quantityField.setFont(font);
        topPanel.add(new JLabel("Količina:"));
        topPanel.add(quantityField);

        frmKoarice.getContentPane().add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setFont(font);
        JScrollPane scrollPane = new JScrollPane(textArea);
        frmKoarice.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton saveButton = new JButton("Spremi");
        saveButton.setFont(font);
        bottomPanel.add(saveButton);

        JButton cancelButton = new JButton("Odustani");
        cancelButton.setFont(font);
        bottomPanel.add(cancelButton);

        frmKoarice.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        // Action listener za spremanje stavke u bazu
        saveButton.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            String quantityText = quantityField.getText();

            try {
                int quantity = Integer.parseInt(quantityText);
                if (quantity <= 0) {
                    textArea.setText("Količina mora biti veća od 0.");
                    return;
                }
                boolean spremljeno = spremiStavkuUBazu(selected, quantity);
                if (spremljeno) {
                    // OTVORI NOVI PROZOR Stavka (pretpostavljam da je to neka druga klasa)
                    Stavka Stavka = new Stavka();
                    Stavka.setVisible(true);

                    // ZATVORI TRENUTNI PROZOR
                    frmKoarice.dispose();
                }
            } catch (NumberFormatException ex) {
                textArea.setText("Unesite ispravnu brojčanu količinu.");
            }
        });

        cancelButton.addActionListener(e -> frmKoarice.dispose());

        // Kad se promijeni korisnik ili biljka ili količina, update textArea
        comboBoxKorisnici.addActionListener(e -> updateTextArea());
        comboBox.addActionListener(e -> updateTextArea());

        quantityField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateTextArea();
            }

            public void removeUpdate(DocumentEvent e) {
                updateTextArea();
            }

            public void changedUpdate(DocumentEvent e) {
                updateTextArea();
            }
        });
    }

    // Učitavanje biljaka u comboBox
    private void loadComboBoxData() {
        String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
        String username = "ppokos";
        String password = "11";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            String query = "SELECT nazivBiljke FROM Biljka";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            comboBox.removeAllItems();
            while (rs.next()) {
                comboBox.addItem(rs.getString("nazivBiljke"));
            }

            rs.close();
            stmt.close();
            conn.close();

            updateTextArea();

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Greška pri dohvaćanju biljaka iz baze.");
        }
    }

    // Učitavanje korisnika u comboBox kao KorisnikItem objekte
    private void loadKorisnici() {
        String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
        String username = "ppokos";
        String password = "11";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, ime FROM Korisnik ORDER BY id")) {

            comboBoxKorisnici.removeAllItems();
            while (rs.next()) {
                int id = rs.getInt("id");
                String ime = rs.getString("ime");
                comboBoxKorisnici.addItem(new KorisnikItem(id, ime));
            }

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Greška pri dohvaćanju korisnika.");
        }
    }

    // Ažuriranje textArea da prikaže odabrane podatke
    private void updateTextArea() {
        String selectedBiljka = (String) comboBox.getSelectedItem();
        KorisnikItem odabraniKorisnik = (KorisnikItem) comboBoxKorisnici.getSelectedItem();
        String quantity = quantityField.getText();

        if (odabraniKorisnik != null) {
            textArea.setText("Korisnik ID: " + odabraniKorisnik.getId() + " - " + odabraniKorisnik.getIme()
                    + "\nOdabrano: " + selectedBiljka
                    + "\nKoličina: " + quantity);
        } else {
            textArea.setText("Nije odabran korisnik\nOdabrano: " + selectedBiljka + "\nKoličina: " + quantity);
        }
    }

    // Spremanje stavke u bazu sa korisničkim ID-em
    private boolean spremiStavkuUBazu(String nazivBiljke, int kolicina) {
        String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
        String username = "ppokos";
        String password = "11";

        KorisnikItem odabraniKorisnik = (KorisnikItem) comboBoxKorisnici.getSelectedItem();
        if (odabraniKorisnik == null) {
            textArea.setText("Odaberite korisnika.");
            return false;
        }
        int korisnikId = odabraniKorisnik.getId();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);

            String getIdQuery = "SELECT id_biljke FROM Biljka WHERE nazivBiljke = ?";
            PreparedStatement getIdStmt = conn.prepareStatement(getIdQuery);
            getIdStmt.setString(1, nazivBiljke);
            ResultSet rs = getIdStmt.executeQuery();

            if (rs.next()) {
                int id_biljke = rs.getInt("id_biljke");

                String insertQuery = "INSERT INTO Kosarica (id_korisnika, id_biljke, kolicina) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, korisnikId);
                insertStmt.setInt(2, id_biljke);
                insertStmt.setInt(3, kolicina);
                insertStmt.executeUpdate();

                insertStmt.close();
                rs.close();
                getIdStmt.close();
                conn.close();

                textArea.setText("Stavka spremljena u tablicu 'Kosarica':\n" + nazivBiljke + " - Količina: " + kolicina
                        + "\nKorisnik: " + odabraniKorisnik.getIme());
                return true;
            } else {
                textArea.setText("Greška: Nije pronađen ID za biljku: " + nazivBiljke);
            }

            rs.close();
            getIdStmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            textArea.setText("Greška pri spremanju u bazu.");
        }
        return false;
    }

    public void setVisible(boolean b) {
        frmKoarice.setVisible(b);
    }

    // Klasa za korisnika u comboBoxu
    private static class KorisnikItem {
        private int id;
        private String ime;

        public KorisnikItem(int id, String ime) {
            this.id = id;
            this.ime = ime;
        }

        public int getId() {
            return id;
        }

        public String getIme() {
            return ime;
        }

        @Override
        public String toString() {
            return ime; // prikazujemo ime korisnika
        }
    }
}
