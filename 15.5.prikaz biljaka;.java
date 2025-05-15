package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

public class prikaz_biljaka extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<String> comboBox;
	private JTextField textVrsta;
	private JTextField textOpis;
	private JTextField textCijena;

	// Podaci za bazu
	private final String url = "jdbc:mysql://ucka.veleri.hr:3306/ppokos";
	private final String user = "ppokos";
	private final String password = "11";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			prikaz_biljaka dialog = new prikaz_biljaka();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public prikaz_biljaka() {
		setBackground(new Color(40, 151, 34));
		setForeground(Color.BLACK);
		setTitle("Prikaz biljaka");
		setBounds(100, 100, 450, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(125, 181, 102));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Odaberi biljku:");
		lblNewLabel.setBounds(10, 20, 150, 20);
		contentPanel.add(lblNewLabel);

		comboBox = new JComboBox<>();
		comboBox.setBounds(170, 20, 200, 25);
		contentPanel.add(comboBox);

		JLabel lblVrsta = new JLabel("Vrsta biljke:");
		lblVrsta.setBounds(10, 70, 150, 20);
		contentPanel.add(lblVrsta);

		textVrsta = new JTextField();
		textVrsta.setBounds(170, 70, 200, 25);
		textVrsta.setEditable(false);
		contentPanel.add(textVrsta);

		JLabel lblOpis = new JLabel("Opis biljke:");
		lblOpis.setBounds(10, 120, 150, 20);
		contentPanel.add(lblOpis);

		textOpis = new JTextField();
		textOpis.setBounds(170, 120, 200, 60);
		textOpis.setEditable(false);
		contentPanel.add(textOpis);

		JLabel lblCijena = new JLabel("Cijena biljke (EUR):");
		lblCijena.setBounds(10, 200, 150, 20);
		contentPanel.add(lblCijena);

		textCijena = new JTextField();
		textCijena.setBounds(170, 200, 200, 25);
		textCijena.setEditable(false);
		contentPanel.add(textCijena);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(40, 151, 34));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // Zatvara prozor
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		// Popuni combo box prilikom pokretanja
		popuniComboBox();

		// Listener za prikaz detalja biljke
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String odabranaBiljka = (String) comboBox.getSelectedItem();
				if (odabranaBiljka != null && !odabranaBiljka.isEmpty()) {
					prikaziDetaljeBiljke(odabranaBiljka);
				} else {
					// Ako je prazno, očisti polja
					textVrsta.setText("");
					textOpis.setText("");
					textCijena.setText("");
				}
			}
		});
	}

	private void popuniComboBox() {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT nazivBiljke FROM Biljka";
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			// Prva prazna opcija
			comboBox.addItem("");

			while (rs.next()) {
				comboBox.addItem(rs.getString("nazivBiljke"));
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Greška pri dohvaćanju biljaka iz baze.");
		}
	}

	private void prikaziDetaljeBiljke(String nazivBiljke) {
		try (Connection conn = DriverManager.getConnection(url, user, password)) {
			String sql = "SELECT vrstaBiljke, opisBiljke, cijenaBiljke FROM Biljka WHERE nazivBiljke = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, nazivBiljke);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				textVrsta.setText(rs.getString("vrstaBiljke"));
				textOpis.setText(rs.getString("opisBiljke"));
				textCijena.setText(rs.getString("cijenaBiljke") + " €");
			}
			stmt.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Greška pri dohvaćanju podataka o biljci.");
		}
	}
}
