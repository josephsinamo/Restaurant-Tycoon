package view.modelsDaftarPanel;

import controller.GameManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import models.RawMaterial;
import models.menu.Menu;
import view.Frame;

public class PanelUntukAturMenu extends javax.swing.JPanel {

    private Menu menu;
    private GameManager gm;
    private int harga = 0;

    public PanelUntukAturMenu() {
        initComponents();
    }

    public PanelUntukAturMenu(Menu menu) {
        this(menu, null);
    }

    public PanelUntukAturMenu(Menu menu, GameManager gm) {
        this.menu = menu;
        this.gm = gm;
        initComponents();
        if (menu != null) {
            jLabel2.setText(menu.getName());
            harga = (int) menu.getPrice();
            jTextField1.setText(String.valueOf(harga));
            jLabel3.setText("Harga jual saat ini");
            jButton3.setText("Atur Resep");
            jButton3.setToolTipText("Atur jumlah bahan baku per porsi");
        }
    }

    private void terapkanHarga() {
        if (menu == null || gm == null) return;
        try {
            harga = Integer.parseInt(jTextField1.getText().trim());
            if (harga < 0) {
                JOptionPane.showMessageDialog(this, "Harga tidak boleh negatif.");
                return;
            }
            gm.getRestaurant().setHarga(menu, harga);
            java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
            if (w instanceof Frame f) {
                f.appendLog("Harga " + menu.getName() + " → Rp " + harga);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan harga angka yang valid.");
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4),
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(80, 70, 90))));
        setMaximumSize(new java.awt.Dimension(32767, 76));
        setMinimumSize(new java.awt.Dimension(300, 76));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(500, 76));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("🍽");
        jLabel1.setFont(new java.awt.Font("Segoe UI Emoji", 0, 28));
        jLabel1.setMaximumSize(new java.awt.Dimension(50, 50));
        jLabel1.setMinimumSize(new java.awt.Dimension(50, 50));
        jLabel1.setPreferredSize(new java.awt.Dimension(50, 50));
        add(jLabel1);

        jPanel1.setOpaque(false);
        jPanel1.setLayout(null);

        jTextField1.setText("0");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jPanel1.add(jTextField1);
        jTextField1.setBounds(250, 20, 70, 22);

        jButton1.setText("-");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel1.add(jButton1);
        jButton1.setBounds(220, 18, 24, 24);

        jButton2.setText("+");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel1.add(jButton2);
        jButton2.setBounds(330, 18, 24, 24);

        jButton3.setText("Atur Resep");
        jButton3.addActionListener(this::jButton3ActionPerformed);
        jPanel1.add(jButton3);
        jButton3.setBounds(360, 16, 90, 28);

        jButton4.setText("Simpan Harga");
        jButton4.setFont(new java.awt.Font("SansSerif", 0, 10));
        jButton4.addActionListener(e -> terapkanHarga());
        jPanel1.add(jButton4);
        jButton4.setBounds(250, 44, 100, 22);

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 13));
        jLabel2.setText("Menu");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(20, 8, 180, 20);

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel3.setText("Harga jual");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(20, 32, 120, 16);

        add(jPanel1);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int currentVal = Integer.parseInt(jTextField1.getText().trim());
            harga = currentVal + 1000;
            jTextField1.setText(String.valueOf(harga));
            jButton1.setEnabled(true);
        } catch (NumberFormatException e) {
            harga = 0;
            jTextField1.setText("0");
        }
    }

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            harga = Integer.parseInt(jTextField1.getText().trim());
        } catch (NumberFormatException e) {
            jTextField1.setText(String.valueOf(harga));
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (menu == null) return;

        PanelPopUpAturMenu pop = new PanelPopUpAturMenu(menu);

        int pilihan = JOptionPane.showConfirmDialog(
                this,
                pop,
                "Resep — " + menu.getName(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (pilihan == JOptionPane.OK_OPTION) {
            List<RawMaterial> bahan = new ArrayList<>(menu.getDaftarBahan());
            List<Integer> nilai = pop.getSemuaNilai();
            for (int i = 0; i < bahan.size() && i < nilai.size(); i++) {
                int qty = Math.max(0, nilai.get(i));
                menu.setReceipt(bahan.get(i), qty);
                if (gm != null) {
                    gm.getRestaurant().racikMenu(menu, bahan.get(i), qty);
                }
            }
            terapkanHarga();
            JOptionPane.showMessageDialog(this,
                    "Resep & harga " + menu.getName() + " disimpan.",
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int currentVal = Integer.parseInt(jTextField1.getText().trim());
            if (currentVal > 1000) {
                harga = currentVal - 1000;
            } else {
                harga = 0;
            }
            jTextField1.setText(String.valueOf(harga));
            jButton1.setEnabled(harga > 0);
        } catch (NumberFormatException e) {
            harga = 0;
            jTextField1.setText("0");
        }
    }

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
}
