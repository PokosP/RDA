package plantie;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class main {

	private JFrame frmPoetna;

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
		frmPoetna.setTitle("Početna");
		frmPoetna.setBounds(100, 100, 450, 300);
		frmPoetna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPoetna.getContentPane().setLayout(null);
		
		JButton btnNewButton_2 = new JButton("Unos korisnika");
		btnNewButton_2.setBounds(162, 22, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2);
		
		JButton btnNewButton_2_1 = new JButton("Unos biljaka");
		btnNewButton_2_1.setBounds(33, 95, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_1);
		
		JButton btnNewButton_2_1_1 = new JButton("Prikaz biljaka");
		btnNewButton_2_1_1.setBounds(33, 166, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_1_1);
		
		JButton btnNewButton_2_2 = new JButton("Košarica");
		btnNewButton_2_2.setBounds(293, 22, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_2);
		
		JButton btnNewButton_2_3 = new JButton("Prikaz korisnika");
		btnNewButton_2_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton_2_3.setBounds(162, 166, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_3);
		
		JButton btnNewButton_2_2_1 = new JButton("Stavke košarica");
		btnNewButton_2_2_1.setBounds(293, 166, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_2_1);
		
		JButton btnNewButton_2_4 = new JButton("O nama");
		btnNewButton_2_4.setBounds(33, 22, 121, 60);
		frmPoetna.getContentPane().add(btnNewButton_2_4);
	}
}
