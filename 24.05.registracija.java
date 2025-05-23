package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class registracija extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JTextField tfime;
	private JTextField tfprezime;
	private JTextField tfkorisnicko_ime;
	private JTextField tflozinka;
	private JTextField tfadresa;
	private JTextField tfbroj_telefona;

	public static void main(String[] args) {
		try {
			registracija dialog = new registracija();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public registracija() {
		setBounds(100, 100, 500, 400);
		getContentPane().setLayout(new BorderLayout());

		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblIme = new JLabel("Ime:");
		lblIme.setBounds(37, 59, 80, 20);
		contentPanel.add(lblIme);

		tfime = new JTextField();
		tfime.setBounds(137, 59, 200, 20);
		contentPanel.add(tfime);

		JLabel lblPrezime = new JLabel("Prezime:");
		lblPrezime.setBounds(37, 99, 80, 20);
		contentPanel.add(lblPrezime);

		tfprezime = new JTextField();
		tfprezime.setBounds(137, 99, 200, 20);
		contentPanel.add(tfprezime);

		JLabel lblKorisnickoIme = new JLabel("Korisničko ime:");
		lblKorisnickoIme.setBounds(37, 139, 100, 20);
		contentPanel.add(lblKorisnickoIme);

		tfkorisnicko_ime = new JTextField();
		tfkorisnicko_ime.setBounds(137, 139, 200, 20);
		contentPanel.add(tfkorisnicko_ime);

		JLabel lblLozinka = new JLabel("Lozinka:");
		lblLozinka.setBounds(37, 179, 80, 20);
		contentPanel.add(lblLozinka);

		tflozinka = new JTextField();
		tflozinka.setBounds(137, 179, 200, 20);
		contentPanel.add(tflozinka);

		JLabel lblAdresa = new JLabel("Adresa:");
		lblAdresa.setBounds(37, 219, 80, 20);
		contentPanel.add(lblAdresa);

		tfadresa = new JTextField();
		tfadresa.setBounds(137, 219, 200, 20);
		contentPanel.add(tfadresa);

		JLabel lblBrojTelefona = new JLabel("Broj telefona:");
		lblBrojTelefona.setBounds(37, 259, 100, 20);
		contentPanel.add(lblBrojTelefona);

		tfbroj_telefona = new JTextField();
		tfbroj_telefona.setBounds(137, 259, 200, 20);
		contentPanel.add(tfbroj_telefona);

		JLabel lblUnos_Novog_Korisnika = new JLabel("Dobrodošli novi korisnik ->");
		lblUnos_Novog_Korisnika.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUnos_Novog_Korisnika.setBounds(39, 10, 217, 26);
		contentPanel.add(lblUnos_Novog_Korisnika);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("Spremi");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ime = tfime.getText();
				String prezime = tfprezime.getText();
				String korisnicko_ime = tfkorisnicko_ime.getText();
				String lozinka = tflozinka.getText();
				String adresa = tfadresa.getText();
				String broj_telefona = tfbroj_telefona.getText();

				try {
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://student.veleri.hr/ppokos?user=ppokos&password=11");

					PreparedStatement stmt = conn.prepareStatement(
							"INSERT INTO Korisnik (ime, prezime, korisnicko_ime, lozinka, adresa, broj_telefona, admin) VALUES (?, ?, ?, ?, ?, ?, 0)",
							PreparedStatement.RETURN_GENERATED_KEYS);
					stmt.setString(1, ime);
					stmt.setString(2, prezime);
					stmt.setString(3, korisnicko_ime);
					stmt.setString(4, lozinka);
					stmt.setString(5, adresa);
					stmt.setString(6, broj_telefona);
					stmt.executeUpdate();

					ResultSet rs = stmt.getGeneratedKeys();
					int korisnikId = -1;
					if (rs.next()) {
						korisnikId = rs.getInt(1);
					}
					stmt.close();

					int odgovor = JOptionPane.showConfirmDialog(null,
							"Želite li poslati zahtjev za administratorska prava?",
							"Zahtjev za admina", JOptionPane.YES_NO_OPTION);

					if (odgovor == JOptionPane.YES_OPTION && korisnikId != -1) {
						String zahtjevTekst = JOptionPane.showInputDialog("Unesite razlog zahtjeva:");
						if (zahtjevTekst != null && !zahtjevTekst.trim().isEmpty()) {
							PreparedStatement zahtjevStmt = conn.prepareStatement(
									"INSERT INTO Zahtjevi_za_admina (korisnicko_ime, zahtjev) VALUES (?, ?)");
							zahtjevStmt.setString(1, korisnicko_ime);
							zahtjevStmt.setString(2, zahtjevTekst.trim());
							zahtjevStmt.executeUpdate();
							zahtjevStmt.close();
							JOptionPane.showMessageDialog(null, "Zahtjev uspješno poslan.");
						}
					}
					conn.close();
					System.out.println("Korisnik uspješno unesen u bazu.");
					dispose();
				} catch (Exception ex) {
					System.out.println("Greška prilikom unosa: " + ex.getMessage());
				}
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Odustani");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}
}
