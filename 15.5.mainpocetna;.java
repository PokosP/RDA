package plantie;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Color;

public class main {

	private JFrame frmPoetna;
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frmPoetna.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPoetna = new JFrame();
		frmPoetna.getContentPane().setBackground(new Color(207, 228, 199));
		frmPoetna.setTitle("Početna");
		frmPoetna.setBounds(100, 100, 371, 332);
		frmPoetna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPoetna.getContentPane().setLayout(null);
		
		JButton btnNewButton_2 = new JButton("Unos korisnika");
		btnNewButton_2.setBounds(189, 22, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_2_1 = new JButton("Unos biljaka");
		btnNewButton_2_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unos_biljaka unos_biljaka=new unos_biljaka();
				unos_biljaka.setVisible(true);
			}
		});
		btnNewButton_2_1.setBounds(21, 115, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_1);
		
		JButton btnNewButton_2_1_1 = new JButton("Prikaz biljaka");
		btnNewButton_2_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				prikaz_biljaka prikaz_biljaka=new prikaz_biljaka();
				prikaz_biljaka.setVisible(true);
			}
		});
		btnNewButton_2_1_1.setBounds(21, 208, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_1_1);
		
		JButton btnNewButton_2_2 = new JButton("Košarica");
		btnNewButton_2_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					kosarica kosarica=new kosarica();
					kosarica.setVisible(true);
			}
		});
		btnNewButton_2_2.setBounds(189, 208, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_2);
		
		JButton btnNewButton_2_3 = new JButton("Prikaz korisnika");
		btnNewButton_2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2_3.setBounds(189, 115, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_3);
		
		JButton btnNewButton_2_4 = new JButton("O nama");
		btnNewButton_2_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				o_nama o_nama=new o_nama();
				o_nama.setVisible(true);
			}
		});
		btnNewButton_2_4.setBounds(21, 22, 146, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_4);
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
}