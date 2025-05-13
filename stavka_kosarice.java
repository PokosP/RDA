package src;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class stavka_kosarice {

	private JFrame frame;
	private JComboBox<String> comboBox;
	private JTextArea textArea;
	private JTextField quantityField;

	/**
	 * Pokretanje aplikacije.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				stavka_kosarice window = new stavka_kosarice();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Konstruktor.
	 */
	public stavka_kosarice() {
		initialize();
		loadComboBoxData(); // Učitavanje biljaka iz baze
	}

	/**
	 * Inicijalizacija GUI-ja.
	 */
	private void initialize() {
		frame = new JFrame("Stavka košarice");
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 16); // Povećani font

		// Gornji panel: izbornik i količina
		JPanel topPanel = new JPanel();

		comboBox = new JComboBox<>();
		comboBox.setFont(font);
		topPanel.add(comboBox);

		quantityField = new JTextField(5);
		quantityField.setFont(font);
		topPanel.add(quantityField);

		frame.getContentPane().add(topPanel, BorderLayout.NORTH);

		// Sredina: tekstualno područje
		textArea = new JTextArea();
		textArea.setFont(font);
		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		// Donji panel: gumb Spremi
		JPanel bottomPanel = new JPanel(new BorderLayout());
		JButton saveButton = new JButton("Spremi");
		saveButton.setFont(font);
		bottomPanel.add(saveButton, BorderLayout.EAST);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

		// Event: klik na Spremi
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = (String) comboBox.getSelectedItem();
				String quantityText = quantityField.getText();

				try {
					int quantity = Integer.parseInt(quantityText);
					if (quantity <= 0) {
						textArea.setText("Količina mora biti veća od 0.");
						return;
					}
					spremiStavkuUBazu(selected, quantity);
				} catch (NumberFormatException ex) {
					textArea.setText("Unesite ispravnu brojčanu količinu.");
				}
			}
		});

		// Event: promjena izbora u comboBox-u
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateTextArea();
			}
		});

		// Event: unos količine
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

	/**
	 * Dohvaćanje biljaka iz baze i punjenje JComboBox-a.
	 */
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

			while (rs.next()) {
				comboBox.addItem(rs.getString("nazivBiljke"));
			}

			rs.close();
			stmt.close();
			conn.close();

			// Prikaz prve stavke automatski
			updateTextArea();

		} catch (Exception e) {
			e.printStackTrace();
			textArea.setText("Greška pri dohvaćanju podataka iz baze.");
		}
	}

	/**
	 * Ažuriranje prikaza u tekstualnom području.
	 */
	private void updateTextArea() {
		String selected = (String) comboBox.getSelectedItem();
		String quantity = quantityField.getText();
		textArea.setText("Odabrano: " + selected + "\nKoličina: " + quantity);
	}

	/**
	 * Spremanje stavke u bazu.
	 */
	private void spremiStavkuUBazu(String nazivBiljke, int kolicina) {
		String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
		String username = "ppokos";
		String password = "11";

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection(url, username, password);

			// 1. Dohvati ID biljke na temelju naziva
			String getIdQuery = "SELECT id_biljke FROM Biljka WHERE nazivBiljke = ?";
			PreparedStatement getIdStmt = conn.prepareStatement(getIdQuery);
			getIdStmt.setString(1, nazivBiljke);
			ResultSet rs = getIdStmt.executeQuery();

			if (rs.next()) {
				int id_biljke = rs.getInt("id_biljke");

				// 2. Unesi podatke u StavkaNarudzbe
				String insertQuery = "INSERT INTO StavkaNarudzbe (id_biljke, kolicina) VALUES (?, ?)";
				PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
				insertStmt.setInt(1, id_biljke);
				insertStmt.setInt(2, kolicina);
				insertStmt.executeUpdate();

				textArea.setText("Stavka spremljena u bazu:\n" + nazivBiljke + " - Količina: " + kolicina);

				insertStmt.close();
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
	}
}

