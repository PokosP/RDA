package plantie;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Pocetna extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    public static void main(String[] args) {
        try {
            Pocetna frame = new Pocetna();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Pocetna() {
        setTitle("Plantie - Početna");
        setBounds(100, 100, 778, 506);
        setLocationRelativeTo(null); // centriraj prozor na ekran
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblPocetna_str = new JLabel("Početna stranica");
        lblPocetna_str.setBounds(29, 78, 155, 31);
        lblPocetna_str.setFont(new Font("Tahoma", Font.PLAIN, 18));
        contentPanel.add(lblPocetna_str);

        JLabel lblNaslov = new JLabel("Plantie");
        lblNaslov.setHorizontalAlignment(SwingConstants.CENTER);
        lblNaslov.setBounds(309, 10, 108, 31);
        lblNaslov.setFont(new Font("Tahoma", Font.PLAIN, 25));
        contentPanel.add(lblNaslov);

        JButton btn_Administrator = new JButton("Prijava kao administrator");
        btn_Administrator.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgPrijava dlg = new DwgPrijava();
                dlg.setVisible(true);
            }
        });
        btn_Administrator.setBounds(128, 129, 165, 62);
        contentPanel.add(btn_Administrator);

        JButton btn_Registracija = new JButton("Registracija");
        btn_Registracija.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgRegistracija dlg = new DwgRegistracija();
                dlg.setVisible(true);
            }
        });
        btn_Registracija.setBounds(128, 232, 165, 62);
        contentPanel.add(btn_Registracija);

        JButton btn_Prijava = new JButton("Prijava");
        btn_Prijava.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgPrijava dlg = new DwgPrijava();
                dlg.setVisible(true);
            }
        });
        btn_Prijava.setBounds(128, 335, 165, 62);
        contentPanel.add(btn_Prijava);

        JButton btn_O_nama = new JButton("O nama");
        btn_O_nama.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgO_Nama dlg = new DwgO_Nama();
                dlg.setVisible(true);
            }
        });
        btn_O_nama.setBounds(450, 129, 155, 62);
        contentPanel.add(btn_O_nama);

        JButton btn_Popis_biljaka = new JButton("Popis biljaka");
        btn_Popis_biljaka.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgPopis_Biljaka dlg = new DwgPopis_Biljaka();
                dlg.setVisible(true);
            }
        });
        btn_Popis_biljaka.setBounds(450, 232, 155, 62);
        contentPanel.add(btn_Popis_biljaka);

        JButton btn_Kosarica = new JButton("Košarica");
        btn_Kosarica.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DwgKosarica dlg = new DwgKosarica();
                dlg.setVisible(true);
            }
        });
        btn_Kosarica.setBounds(450, 335, 155, 62);
        contentPanel.add(btn_Kosarica);
    }
}
