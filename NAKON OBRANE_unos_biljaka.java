package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import java.awt.Color;

public class unos_biljaka extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4; 

	public static void main(String[] args) {
		try {
			unos_biljaka dialog = new unos_biljaka();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public unos_biljaka() {
		setTitle("Unos biljaka");
		setBounds(100, 100, 450, 360);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(125, 181, 102));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Unos naziva biljaka:");
		lblNewLabel.setBounds(10, 11, 150, 23);
		contentPanel.add(lblNewLabel);

		textField = new JTextField();
		textField.setBounds(233, 9, 120, 26);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Unos vrste biljke:");
		lblNewLabel_1.setBounds(10, 45, 150, 20);
		contentPanel.add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setBounds(233, 46, 120, 26);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Unos opisa biljke:");
		lblNewLabel_2.setBounds(10, 97, 150, 14);
		contentPanel.add(lblNewLabel_2);

		textField_2 = new JTextField();
		textField_2.setBounds(233, 97, 120, 58);
		contentPanel.add(textField_2);
		textField_2.setColumns(10);

		JLabel lblNewLabel_2_1 = new JLabel("Unos cijene biljke:");
		lblNewLabel_2_1.setBounds(10, 169, 150, 14);
		contentPanel.add(lblNewLabel_2_1);

		textField_3 = new JTextField();
		textField_3.setBounds(233, 166, 120, 26);
		contentPanel.add(textField_3);
		textField_3.setColumns(10);

		JLabel lblNewLabel_2_1_1 = new JLabel("Unos podrijetla biljke:");
		lblNewLabel_2_1_1.setBounds(10, 209, 150, 14);
		contentPanel.add(lblNewLabel_2_1_1);

		textField_4 = new JTextField();
		textField_4.setBounds(233, 203, 120, 26);
		contentPanel.add(textField_4);
		textField_4.setColumns(10);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(40, 151, 34));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String naziv = textField.getText().trim();
				String vrsta = textField_1.getText().trim();
				String opis = textField_2.getText().trim();
				String cijenaStr = textField_3.getText().trim();
				String podrijetlo = textField_4.getText().trim();

				if (naziv.isEmpty() || vrsta.isEmpty() || opis.isEmpty() || cijenaStr.isEmpty() || podrijetlo.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Morate popuniti sva polja!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
					return;
				}

				double cijena;
				try {
					cijena = Double.parseDouble(cijenaStr);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Unesite ispravnu cijenu (npr. 2.99)!", "Upozorenje", JOptionPane.WARNING_MESSAGE);
					return;
				}

				String url = "jdbc:mysql://ucka.veleri.hr:3306/ppokos";
				String user = "ppokos";
				String password = "11";

				try {
					Connection conn = DriverManager.getConnection(url, user, password);
					String sql = "INSERT INTO Biljka (nazivBiljke, vrstaBiljke, opisBiljke, cijenaBiljke, podrijetloBiljke) VALUES (?, ?, ?, ?, ?)";
					PreparedStatement stmt = conn.prepareStatement(sql);
					stmt.setString(1, naziv);
					stmt.setString(2, vrsta);
					stmt.setString(3, opis);
					stmt.setDouble(4, cijena);
					stmt.setString(5, podrijetlo);

					stmt.executeUpdate();
					stmt.close();
					conn.close();

					JDialog successDialog = new JDialog();
					successDialog.getContentPane().setLayout(new BorderLayout());
					successDialog.getContentPane().add(new JLabel("Biljka je uspješno unesena!", JLabel.CENTER), BorderLayout.CENTER);
					successDialog.setSize(300, 100);
					successDialog.setLocationRelativeTo(null);
					successDialog.setModal(false);
					successDialog.setVisible(true);

					new Timer(2500, new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							successDialog.dispose();
						}
					}).start();

					textField.setText("");
					textField_1.setText("");
					textField_2.setText("");
					textField_3.setText("");
					textField_4.setText("");

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Greška pri unosu u bazu!");
				}
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}
}
