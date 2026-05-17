package view;

import controller.GameManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import models.RawMaterial;
import view.modelsDaftarPanel.PanelInventarisBahan;

/**
 * Panel dapur restoran: inventaris bahan baku & upgrade.
 */
public class PanelRestoranMenu extends javax.swing.JPanel {

    private static final int BIAYA_UPGRADE_KAPASITAS = 25000;
    private static final int TAMBAH_KAPASITAS = 5;

    private GameManager gm;

    public PanelRestoranMenu() {
        initComponents();
    }

    public PanelRestoranMenu(GameManager gm) {
        this.gm = gm;
        initComponents();
        jLabel1.setText("INVENTORI BAHAN BAKU");
        jLabel2.setText("UPGRADE KAPASITAS");
        jButton1.setText("UPGRADE\n+" + TAMBAH_KAPASITAS + " slot");
        refreshInventori();
    }

    /** Muat ulang kartu stok dari restoran (supplier + stok yang ada). */
    public void refreshInventori() {
        container.removeAll();
        if (gm == null || gm.getRestaurant() == null) {
            container.revalidate();
            container.repaint();
            return;
        }

        Map<String, Integer> stokNama = gm.getRestaurant().getStokBahanBaku();
        Set<RawMaterial> semuaBahan = new LinkedHashSet<>();

        if (gm.getSupplier() != null) {
            semuaBahan.addAll(gm.getSupplier().getBahanBaku());
        }
        for (RawMaterial rm : gm.getRestaurant().getStok().keySet()) {
            semuaBahan.add(rm);
        }

        if (semuaBahan.isEmpty()) {
            JPanel kosong = new JPanel();
            kosong.setOpaque(false);
            kosong.add(new javax.swing.JLabel(
                    "<html><center>Belum ada bahan.<br>Beli di tab Supplier.</center></html>",
                    javax.swing.SwingConstants.CENTER));
            container.setLayout(new BorderLayout());
            container.add(kosong, BorderLayout.CENTER);
        } else {
            container.setLayout(new GridLayout(0, 4, 12, 12));
            for (RawMaterial bahan : semuaBahan) {
                int qty = stokNama.getOrDefault(bahan.getName(), 0);
                container.add(new PanelInventarisBahan(bahan, qty));
            }
        }

        container.revalidate();
        container.repaint();
    }

<<<<<<< HEAD
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (gm == null || gm.getRestaurant() == null) return;

        boolean ok = gm.getRestaurant().upgradeKapasitasRestoran(
                TAMBAH_KAPASITAS, BIAYA_UPGRADE_KAPASITAS);
        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Uang tidak cukup. Biaya upgrade: Rp "
                            + String.format("%.0f", (double) BIAYA_UPGRADE_KAPASITAS),
                    "Upgrade Gagal",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        java.awt.Window w = javax.swing.SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame f) {
            f.refreshAll();
            f.appendLog("Kapasitas restoran +"+ TAMBAH_KAPASITAS);
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        container = new javax.swing.JPanel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jPanel1);

        setBackground(new java.awt.Color(26, 30, 42));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        jSplitPane2.setBackground(new java.awt.Color(26, 30, 46));
        jSplitPane2.setDividerLocation(280);
        jSplitPane2.setDividerSize(6);
        jSplitPane2.setResizeWeight(0.35);
        jSplitPane2.setEnabled(true);

        jPanel3.setLayout(new java.awt.GridLayout(1, 0));
        jButton2.setVisible(false);
        jPanel3.add(jButton2);
        jLabel3.setVisible(false);
        jPanel3.add(jLabel3);
        jSplitPane2.setRightComponent(jPanel3);

        jPanel4.setLayout(new java.awt.GridLayout(2, 1, 4, 4));
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel4.add(jButton1);
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 11));
        jPanel4.add(jLabel2);
        jSplitPane2.setLeftComponent(jPanel4);

        add(jSplitPane2, java.awt.BorderLayout.PAGE_START);

        jPanel2.setBackground(new java.awt.Color(26, 30, 42));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 4, 8, 4));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 16));
        jLabel1.setForeground(new java.awt.Color(255, 204, 100));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("INVENTORI BAHAN BAKU");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 10, 0));
        jPanel2.add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jScrollPane2.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 3));
        jScrollPane2.getVerticalScrollBar().setUnitIncrement(16);

        container.setBackground(new java.awt.Color(41, 33, 45));
        container.setLayout(new GridLayout(0, 4, 12, 12));
        jScrollPane2.setViewportView(container);

        jPanel2.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JPanel container;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane2;
=======
    public void refreshInventori() {
        if (gm != null && lblKapasitasInfo != null)
            lblKapasitasInfo.setText("🏠 Kapasitas Restoran: " + gm.getRestaurant().getKapasitas() + " kursi");
        loadInventori();
    }

    private JPanel createItemCard(RawMaterial rw) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(new Color(255, 253, 228));
        card.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 100), 1));

        JLabel lblNama = new JLabel(rw.getName(), SwingConstants.LEFT);
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblNama.setForeground(new Color(60, 40, 20));
        lblNama.setBorder(new EmptyBorder(4, 6, 0, 0));

        JLabel lblIcon;
        java.net.URL url = getClass().getResource(getAsetPath(rw.getName()));
        if (url != null) {
            Image scaled = new ImageIcon(url).getImage().getScaledInstance(52, 52, Image.SCALE_SMOOTH);
            lblIcon = new JLabel(new ImageIcon(scaled), JLabel.CENTER);
        } else {
            lblIcon = new JLabel("🥚", JLabel.CENTER);
            lblIcon.setFont(new Font("Serif", Font.PLAIN, 36));
        }

        int qty = gm != null ? gm.getRestaurant().getStok().getOrDefault(rw, 0) : 0;
        JLabel lblQty = new JLabel(qty + "  ", JLabel.RIGHT);
        lblQty.setFont(new Font("Arial", Font.BOLD, 15));
        lblQty.setForeground(new Color(80, 60, 20));
        lblQty.setBorder(new EmptyBorder(0, 0, 4, 4));

        card.add(lblNama, BorderLayout.NORTH);
        card.add(lblIcon, BorderLayout.CENTER);
        card.add(lblQty,  BorderLayout.SOUTH);
        return card;
    }

    private JButton styledButton(String text, Color fg, Color bg, int size) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, size));
        b.setForeground(fg); b.setBackground(bg); b.setOpaque(true);
        b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        return b;
    }
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13
}
