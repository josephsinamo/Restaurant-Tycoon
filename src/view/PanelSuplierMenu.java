package view;

import controller.GameManager;
import core.Supplier;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import models.RawMaterial;
import models.jimat.Jimat;
import view.modelsDaftarPanel.PanelUntukDaftarMenu;

public class PanelSuplierMenu extends javax.swing.JPanel {

    private static final Color BG_GACHA = new Color(45, 35, 65);

    private GameManager gm;
    private JLabel lblHasilGacha;
    private JLabel lblBiayaGacha;

    public PanelSuplierMenu() {
        initComponents();
    }

    public PanelSuplierMenu(GameManager gm) {
        this.gm = gm;
        initComponents();
        setupUi();
        loadBahanBaku();
    }

    private void setupUi() {
        jLabel1.setText("SUPPLIER — Bahan Baku & Gacha Jimat");
        jLabel1.setFont(new Font("SansSerif", Font.BOLD, 14));
        jLabel1.setForeground(new Color(255, 204, 100));
        jTabbedPane1.setTitleAt(0, "Bahan Baku");
        jTabbedPane1.setTitleAt(1, "Gacha Jimat");
        setupTabGacha();
    }

    private void loadBahanBaku() {
        jPanel3.removeAll();
        if (gm != null && gm.getSupplier() != null) {
            for (RawMaterial rw : gm.getSupplier().getBahanBaku()) {
                jPanel3.add(new PanelUntukDaftarMenu(rw, gm));
            }
        }
        jPanel3.revalidate();
        jPanel3.repaint();
    }

    private void setupTabGacha() {
        jPanel2.removeAll();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBackground(BG_GACHA);

        JPanel gacha = new JPanel(new GridBagLayout());
        gacha.setBackground(BG_GACHA);
        gacha.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel title = new JLabel("🎰 GACHA JIMAT", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(255, 220, 120));
        gacha.add(title, gbc);

        JLabel desc = new JLabel(
                "<html><center>Tarik jimat acak ke inventori.<br>"
                + "Pasang di tab <b>Jimat</b>.</center></html>",
                SwingConstants.CENTER);
        desc.setForeground(new Color(220, 210, 240));
        gacha.add(desc, gbc);

        lblBiayaGacha = new JLabel(
                "Biaya: Rp " + String.format("%.0f", Supplier.BIAYA_GACHA_JIMAT),
                SwingConstants.CENTER);
        lblBiayaGacha.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblBiayaGacha.setForeground(new Color(180, 255, 200));
        gacha.add(lblBiayaGacha, gbc);

        lblHasilGacha = new JLabel("Belum ada tarikan.", SwingConstants.CENTER);
        lblHasilGacha.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblHasilGacha.setForeground(Color.WHITE);
        gacha.add(lblHasilGacha, gbc);

        JButton btnGacha = new JButton("✨ TARIK JIMAT");
        btnGacha.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnGacha.setBackground(new Color(120, 80, 200));
        btnGacha.setForeground(Color.WHITE);
        btnGacha.setFocusPainted(false);
        btnGacha.addActionListener(e -> lakukanGacha());
        gbc.insets = new Insets(20, 8, 8, 8);
        gacha.add(btnGacha, gbc);

        gacha.add(Box.createVerticalStrut(8), gbc);

        JLabel hint = new JLabel("Charming · Cleaner · Security (power acak)", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setForeground(new Color(160, 150, 180));
        gacha.add(hint, gbc);

        jPanel2.add(gacha, BorderLayout.CENTER);
    }

    private void lakukanGacha() {
        if (gm == null || gm.getSupplier() == null || gm.getRestaurant() == null) {
            return;
        }

        Jimat hadiah = gm.getSupplier().gachaJimat(gm.getRestaurant());
        if (hadiah == null) {
            JOptionPane.showMessageDialog(this,
                    "Uang tidak cukup.\nBiaya gacha: Rp "
                            + String.format("%.0f", Supplier.BIAYA_GACHA_JIMAT),
                    "Gacha Gagal",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tipe = hadiah.getClass().getSimpleName();
        String ikon = switch (tipe) {
            case "Charming" -> "✨";
            case "Cleaner" -> "🧹";
            case "Security" -> "🛡";
            default -> "🧿";
        };
        lblHasilGacha.setText(String.format(
                "%s %s — Power: %.0f (masuk inventaris)",
                ikon, hadiah.getName(), hadiah.getPower()));

        refreshFrame();
        java.awt.Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame f) {
            f.appendLog("Gacha: dapat " + tipe + " \"" + hadiah.getName()
                    + "\" (power " + (int) hadiah.getPower() + ")");
        }
    }

    private void refreshFrame() {
        java.awt.Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof Frame f) {
            f.refreshAll();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(26, 30, 42));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new java.awt.BorderLayout());

        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 12, 1));
        add(jLabel1, java.awt.BorderLayout.PAGE_START);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 1, 1, 1));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel3);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jTabbedPane1.addTab("Bahan Baku", jPanel1);
        jTabbedPane1.addTab("Gacha Jimat", jPanel2);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }

    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
}
