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
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class prikaz_korisnika extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JComboBox<String> comboBox;

	// Dodaj podatke za konekciju s bazom
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
		setBounds(100, 100, 500, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblpopis_korisnika = new JLabel("Popis korisnika");
		lblpopis_korisnika.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblpopis_korisnika.setBounds(24, 10, 150, 25);
		contentPanel.add(lblpopis_korisnika);

		comboBox = new JComboBox<>();
		comboBox.setBounds(24, 53, 200, 21);
		contentPanel.add(comboBox);

		JLabel lbluloga = new JLabel("Uloga korisnika:");
		lbluloga.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lbluloga.setBounds(24, 100, 150, 25);
		contentPanel.add(lbluloga);

		textField = new JTextField();
		textField.setBounds(24, 127, 200, 87);
		contentPanel.add(textField);
		textField.setColumns(10);

		JButton btnPrikaziZahtjeve = new JButton("Prikaži zahtjeve za administracijsku ulogu");
		btnPrikaziZahtjeve.setBounds(24, 237, 326, 35);
		contentPanel.add(btnPrikaziZahtjeve);

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
			String sql = "SELECT admin FROM Korisnik WHERE korisnicko_ime = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, korisnickoIme);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int adminValue = rs.getInt("admin");
				String uloga = (adminValue == 1) ? "Administrator" : "Korisnik";
				textField.setText(uloga);
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Greška pri dohvaćanju uloge korisnika.");
		}
	}
}
