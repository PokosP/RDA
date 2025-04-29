package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;

public class DwgRegistracija extends JDialog {

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
			DwgRegistracija dialog = new DwgRegistracija();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DwgRegistracija() {
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

				System.out.println(ime + " " + prezime + " " + korisnicko_ime + " " + lozinka + " " + adresa + " " + broj_telefona);

				try {
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
					Connection conn = DriverManager.getConnection(
							"jdbc:mysql://student.veleri.hr/dhaskic?user=dhaskic&password=11");

					PreparedStatement stmt = conn.prepareStatement(
							"INSERT INTO korisnik (ime, prezime, korisnicko_ime, lozinka, adresa, broj_telefona) VALUES (?, ?, ?, ?, ?, ?)");

					stmt.setString(1, ime);
					stmt.setString(2, prezime);
					stmt.setString(3, korisnicko_ime);
					stmt.setString(4, lozinka);
					stmt.setString(5, adresa);
					stmt.setString(6, broj_telefona);

					stmt.executeUpdate();
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
