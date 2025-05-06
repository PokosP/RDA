package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class DwgPrijava extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfID_korisnika;
	private JTextField tfLozinka;

	public static void main(String[] args) {
		try {
			DwgPrijava dialog = new DwgPrijava();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DwgPrijava() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblID_korisnika = new JLabel("ID_korisnika");
		lblID_korisnika.setHorizontalAlignment(SwingConstants.CENTER);
		lblID_korisnika.setBounds(50, 85, 90, 21);
		contentPanel.add(lblID_korisnika);

		tfID_korisnika = new JTextField();
		tfID_korisnika.setBounds(163, 86, 210, 19);
		contentPanel.add(tfID_korisnika);
		tfID_korisnika.setColumns(10);

		JLabel lblLozinka = new JLabel("Lozinka");
		lblLozinka.setHorizontalAlignment(SwingConstants.CENTER);
		lblLozinka.setBounds(68, 144, 61, 13);
		contentPanel.add(lblLozinka);

		tfLozinka = new JTextField();
		tfLozinka.setBounds(163, 141, 210, 19);
		contentPanel.add(tfLozinka);
		tfLozinka.setColumns(10);

		JLabel lblPrijava = new JLabel("Prijava");
		lblPrijava.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPrijava.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrijava.setBounds(30, 10, 137, 30);
		contentPanel.add(lblPrijava);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String korisnikID = tfID_korisnika.getText();
				String lozinka = tfLozinka.getText();

				try {
					Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
					Connection conn = DriverManager.getConnection(
						"jdbc:mysql://student.veleri.hr/dhaskic?user=dhaskic&password=11");

					PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM korisnik WHERE korisnicko_ime = ? AND lozinka = ?");
					stmt.setString(1, korisnikID);
					stmt.setString(2, lozinka);

					ResultSet rs = stmt.executeQuery();

					if (rs.next()) {
						JOptionPane.showMessageDialog(null, "Uspješna prijava!");
						dispose(); // zatvaranje prozora ako je prijava uspjesna
					} else {
						JOptionPane.showMessageDialog(null, "Neispravno korisničko ime ili lozinka.", "Greška", JOptionPane.ERROR_MESSAGE);
					}

					rs.close();
					stmt.close();
					conn.close();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Greška prilikom spajanja na bazu: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		buttonPane.add(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> dispose());
		buttonPane.add(cancelButton);
	}
}
