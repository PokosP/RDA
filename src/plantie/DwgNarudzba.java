package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JScrollBar;

public class DwgNarudzba extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DwgNarudzba dialog = new DwgNarudzba();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DwgNarudzba() {
		setBounds(100, 100, 809, 577);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblNarudzba = new JLabel("Narudžba");
		lblNarudzba.setBounds(10, 10, 183, 48);
		lblNarudzba.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNarudzba.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(lblNarudzba);
		{
			JLabel lblIme = new JLabel("Ime:");
			lblIme.setBounds(20, 68, 45, 13);
			lblIme.setFont(new Font("Tahoma", Font.PLAIN, 13));
			contentPanel.add(lblIme);
		}
		
		textField = new JTextField();
		textField.setBounds(113, 68, 200, 19);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblPrezime = new JLabel("Prezime");
		lblPrezime.setBounds(20, 120, 59, 13);
		lblPrezime.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPanel.add(lblPrezime);
		
		textField_1 = new JTextField();
		textField_1.setBounds(113, 118, 200, 19);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblAdresa = new JLabel("Adresa:");
		lblAdresa.setBounds(20, 170, 59, 13);
		lblAdresa.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPanel.add(lblAdresa);
		
		textField_2 = new JTextField();
		textField_2.setBounds(113, 168, 200, 19);
		contentPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblDrzava = new JLabel("Država");
		lblDrzava.setBounds(20, 227, 80, 13);
		lblDrzava.setFont(new Font("Tahoma", Font.PLAIN, 13));
		contentPanel.add(lblDrzava);
		
		textField_3 = new JTextField();
		textField_3.setBounds(113, 225, 200, 19);
		contentPanel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblTel_broj = new JLabel("Tel. broj");
		lblTel_broj.setBounds(20, 290, 69, 13);
		contentPanel.add(lblTel_broj);
		
		textField_4 = new JTextField();
		textField_4.setBounds(113, 287, 200, 19);
		contentPanel.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblNaziv_ulice = new JLabel("Naziv ulice");
		lblNaziv_ulice.setBounds(20, 349, 80, 13);
		contentPanel.add(lblNaziv_ulice);
		
		textField_5 = new JTextField();
		textField_5.setBounds(113, 346, 200, 19);
		contentPanel.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblPoš_br = new JLabel("Poštanski\r\nbroj");
		lblPoš_br.setHorizontalAlignment(SwingConstants.LEFT);
		lblPoš_br.setBounds(20, 400, 80, 13);
		contentPanel.add(lblPoš_br);
		
		textField_6 = new JTextField();
		textField_6.setBounds(110, 397, 203, 19);
		contentPanel.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel lblMjesto = new JLabel("Mjesto");
		lblMjesto.setBounds(20, 454, 45, 13);
		contentPanel.add(lblMjesto);
		
		textField_7 = new JTextField();
		textField_7.setBounds(111, 454, 202, 19);
		contentPanel.add(textField_7);
		textField_7.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
