package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class prikaz_korisnika extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JComboBox<String> comboBox;
	private JTextArea textArea;

	// Podaci za konekciju
	private final String url = "jdbc:mysql://ucka.veleri.hr:3306/ppokos";
	private final String user = "ppokos";
	private final String password = "11";

	public static void main(String[] args) {
		try {
			prikaz_korisnika dialog = new prikaz_korisnika();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public prikaz_korisnika() {
		setBounds(100, 100, 650, 480);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblpopis_korisnika = new JLabel("Popis korisnika");
		lblpopis_korisnika.setBounds(24, 10, 150, 25);
		lblpopis_korisnika.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPanel.add(lblpopis_korisnika);

		comboBox = new JComboBox<>();
		comboBox.setBounds(24, 53, 200, 21);
		contentPanel.add(comboBox);

		JLabel lbluloga = new JLabel("Uloga korisnika:");
		lbluloga.setBounds(24, 100, 150, 25);
		lbluloga.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPanel.add(lbluloga);

		textField = new JTextField();
		textField.setBounds(24, 127, 200, 93);
		contentPanel.add(textField);
		textField.setColumns(10);

		JButton btnPrikaziZahtjeve = new JButton("Prikaži zahtjeve za administracijsku ulogu");
		btnPrikaziZahtjeve.setBounds(24, 338, 350, 35);
		contentPanel.add(btnPrikaziZahtjeve);

		JLabel lblprikaz_kosarice = new JLabel("Stavke u košarici:");
		lblprikaz_kosarice.setBounds(250, 100, 200, 25);
		lblprikaz_kosarice.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPanel.add(lblprikaz_kosarice);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(250, 127, 350, 150);
		contentPanel.add(scrollPane);

		btnPrikaziZahtjeve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					prikaz_zahtjeva dialog = new prikaz_zahtjeva();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Greška pri otvaranju prikaza zahtjeva.");
				}
			}
		});

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Zatvori");
		okButton.setActionCommand("OK");
		okButton.addActionListener(e -> dispose());
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		popuniComboBoxKorisnici();

		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String odabraniKorisnik = (String) comboBox.getSelectedItem();
				if (odabraniKorisnik != null && !odabraniKorisnik.isEmpty()) {
					prikaziUloguKorisnika(odabraniKorisnik);
				} else {
					textField.setText("");
					textArea.setText("");
				}
			}
		});
	}

	private void popuniComboBoxKorisnici() {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT korisnicko_ime FROM Korisnik";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			comboBox.addItem("");

			while (rs.next()) {
				comboBox.addItem(rs.getString("korisnicko_ime"));
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Greška pri dohvaćanju korisnika iz baze.");
		}
	}

	private void prikaziUloguKorisnika(String korisnickoIme) {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT id, admin FROM Korisnik WHERE korisnicko_ime = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, korisnickoIme);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int adminValue = rs.getInt("admin");
				int idKorisnika = rs.getInt("id");

				String uloga = (adminValue == 1) ? "Administrator" : "Korisnik";
				textField.setText(uloga);

				prikaziKosaricuKorisnika(idKorisnika);
			} else {
				textField.setText("");
				textArea.setText("");
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Greška pri dohvaćanju uloge korisnika.");
		}
	}

	private void prikaziKosaricuKorisnika(int korisnikId) {
		StringBuilder kosaricaText = new StringBuilder();
		String sql = """
			SELECT b.nazivBiljke, b.cijenaBiljke, k.kolicina
			FROM Kosarica k
			JOIN Biljka b ON k.id_biljke = b.id_biljke
			WHERE k.id_korisnika = ?
		""";

		try (Connection conn = DriverManager.getConnection(url, user, password);
			 PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, korisnikId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String naziv = rs.getString("nazivBiljke");
				double cijena = rs.getDouble("cijenaBiljke");
				int kolicina = rs.getInt("kolicina");

				kosaricaText.append(String.format("- %s | %.2f € | %d kom\n", naziv, cijena, kolicina));
			}

			if (kosaricaText.length() == 0) {
				kosaricaText.append("Košarica je prazna.");
			}

			textArea.setText(kosaricaText.toString());

		} catch (Exception ex) {
			ex.printStackTrace();
			textArea.setText("Greška pri dohvaćanju košarice.");
		}
	}
}
