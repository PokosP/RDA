package Proba;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Kosarica {

	private JFrame frmKoarice;
	private JComboBox<String> comboBox;
	private JTextArea textArea;
	private JTextField quantityField;

	private int idKorisnika = 1; // Testni ID korisnika

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
	}

	private void initialize() {
		frmKoarice = new JFrame("Košarice");
		frmKoarice.setBounds(100, 100, 500, 350);
		frmKoarice.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmKoarice.getContentPane().setLayout(new BorderLayout());

		Font font = new Font("SansSerif", Font.PLAIN, 16);

		JPanel topPanel = new JPanel();

		comboBox = new JComboBox<>();
		comboBox.setFont(font);
		topPanel.add(comboBox);

		quantityField = new JTextField(5);
		quantityField.setFont(font);
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

		// 👇 OVDJE JE DODANA FUNKCIONALNOST OTVARANJA Stavka_kosarice
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
					// ✅ OTVORI NOVI PROZOR
					Stavka_kosarice stavka_kosarice = new Stavka_kosarice();
					stavka_kosarice.setVisible(true);

					// ✅ ZATVORI TRENUTNI PROZOR
					frmKoarice.dispose();
				}
			} catch (NumberFormatException ex) {
				textArea.setText("Unesite ispravnu brojčanu količinu.");
			}
		});

		cancelButton.addActionListener(e -> frmKoarice.dispose());

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

			updateTextArea();

		} catch (Exception e) {
			e.printStackTrace();
			textArea.setText("Greška pri dohvaćanju podataka iz baze.");
		}
	}

	private void updateTextArea() {
		String selected = (String) comboBox.getSelectedItem();
		String quantity = quantityField.getText();
		textArea.setText("Odabrano: " + selected + "\nKoličina: " + quantity);
	}

	private boolean spremiStavkuUBazu(String nazivBiljke, int kolicina) {
		String url = "jdbc:mysql://ucka.veleri.hr/ppokos";
		String username = "ppokos";
		String password = "11";

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
				insertStmt.setInt(1, idKorisnika);
				insertStmt.setInt(2, id_biljke);
				insertStmt.setInt(3, kolicina);
				insertStmt.executeUpdate();

				insertStmt.close();
				rs.close();
				getIdStmt.close();
				conn.close();

				textArea.setText("Stavka spremljena u tablicu 'Kosarica':\n" + nazivBiljke + " - Količina: " + kolicina);
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
}
