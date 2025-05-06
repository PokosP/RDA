package plantie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Color;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Onama extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	JTextArea textArea = new JTextArea();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			Onama dialog = new Onama();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Onama() {
		setForeground(new Color(219, 112, 147));
		setBackground(new Color(219, 112, 147));
		setTitle("O nama ");
		setFont(new Font("Courier New", Font.PLAIN, 15));
		setBounds(100, 100, 586, 523);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(219, 112, 147));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 10, 552, 445);
			contentPanel.add(scrollPane);
			{
				JTextArea txtrMiSmoTim = new JTextArea();
				txtrMiSmoTim.setForeground(new Color(139, 0, 0));
				txtrMiSmoTim.setBackground(Color.PINK);
				txtrMiSmoTim.setFont(new Font("Century", Font.PLAIN, 15));
				txtrMiSmoTim.setEditable(false);
				txtrMiSmoTim.setText("Upoznajte naš tim :)\r\nMi smo tim od četiri entuzijastične žene\r\n koje dijele ljubav prema biljkama i tehnologiji. \r\nPlantie smo stvorile kako bismo vam olakšale\r\n pronalazak savršene biljke, \r\npružile korisne informacije o njezi\r\n i inspirirale vas da svoj prostor učinite zelenijim. \r\nSvaka od nas doprinosi aplikaciji svojim\r\n znanjem i strašću – od dizajna i razvoja\r\n do botanike i komunikacije. \r\nNaša misija je učiniti svijet biljaka\r\n dostupnijim, jednostavnijim i zabavnijim za sve.\r\n\r\n\r\nKontaktirajte nas:\r\n\r\nImate pitanje, prijedlog ili trebate pomoć? Slobodno nas kontaktirajte \r\nputem jedne od sljedećih adresa:\r\n\r\nOpća podrška\r\nsupport@plantie.app\r\nZa sva pitanja vezana uz korištenje \r\naplikacije, narudžbe i korisničku podršku.\r\n\r\nPitanja o biljkama i savjeti za njegu\r\nplants@plantie.app\r\nNiste sigurni kako njegovati svoju biljku?\r\n Pošaljite nam upit i rado ćemo pomoći!\r\n\r\nSuradnje i partnerstva\r\npartnerships@plantie.app\r\nOtvoreni smo za suradnje s ljubiteljima biljaka, trgovinama i brendovima.\r\n\r\n\r\n");
				scrollPane.setViewportView(txtrMiSmoTim);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(219, 112, 147));
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
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
