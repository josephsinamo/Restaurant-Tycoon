package view.modelsDaftarPanel;

import controller.GameManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import models.RawMaterial;
import view.Frame;

/**
 * Satu baris item di tab Supplier — nama bahan, harga, kontrol qty, tombol BUY.
 */
public class PanelUntukDaftarMenu extends JPanel {

    private static final Color BG_ROW_EVEN  = new Color(248, 248, 252);
    private static final Color BG_ROW_ODD   = new Color(238, 236, 248);
    private static final Color TEXT_DARK    = new Color(40, 35, 60);
    private static final Color ACCENT       = new Color(255, 140, 30);
    private static final Color ACCENT2      = new Color(60, 180, 120);

    // ── Nama bahan → path gambar ──────────────────────────────────────────
    private static String getAsetPath(String nama) {
        return switch (nama.toLowerCase()) {
            case "beras"          -> "/view/Aset/Beras.png";
            case "telor","telur"  -> "/view/Aset/Telur.png";
            case "ayam"           -> "/view/Aset/Ayam.png";
            case "kopi"           -> "/view/Aset/Kopi.png";
            case "gula"           -> "/view/Aset/Gula.png";
            case "tepung"         -> "/view/Aset/Tepung.png";
            case "bumbu"          -> "/view/Aset/Bumbu.png";
            default               -> "/view/Aset/makanan.png";
        };
    }

    private GameManager gm;
    private RawMaterial rw;
    private int qty = 0;
    private JTextField txtQty;
    private JButton btnMinus;

    // ── Constructor default ───────────────────────────────────────────────
    public PanelUntukDaftarMenu() {
        setOpaque(false);
    }

    // ── Constructor utama (bahan baku + GameManager) ──────────────────────
    public PanelUntukDaftarMenu(RawMaterial rw, GameManager gm) {
        this.rw = rw;
        this.gm = gm;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        setPreferredSize(new Dimension(400, 70));
        setBorder(new CompoundBorder(
            new EmptyBorder(3, 4, 3, 4),
            new LineBorder(new Color(200, 195, 215), 1)));
        setBackground(BG_ROW_EVEN);
        setOpaque(true);

        // ── Gambar bahan (kiri) ───────────────────────────────────────────
        JLabel lblImg;
        java.net.URL url = getClass().getResource(getAsetPath(rw.getName()));
        if (url != null) {
            Image img = new ImageIcon(url).getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lblImg = new JLabel(new ImageIcon(img), JLabel.CENTER);
        } else {
            lblImg = new JLabel("🥚", JLabel.CENTER);
            lblImg.setFont(new Font("Serif", Font.PLAIN, 24));
        }
        lblImg.setPreferredSize(new Dimension(50, 50));
        lblImg.setBorder(new EmptyBorder(0, 6, 0, 0));
        add(lblImg, BorderLayout.WEST);

        // ── Info nama + harga (tengah) ────────────────────────────────────
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(8, 8, 8, 0));

        JLabel lblNama = new JLabel(rw.getName());
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblNama.setForeground(TEXT_DARK);

        double harga = gm != null ? gm.getSupplier().getHargaBahanBaku(rw) : 0;
        JLabel lblHarga = new JLabel("Rp " + String.format("%.0f", harga));
        lblHarga.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblHarga.setForeground(new Color(120, 90, 40));

        info.add(lblNama);
        info.add(Box.createVerticalStrut(2));
        info.add(lblHarga);
        add(info, BorderLayout.CENTER);

        // ── Kontrol qty + tombol BUY (kanan) ─────────────────────────────
        JPanel ctrl = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        ctrl.setOpaque(false);
        ctrl.setBorder(new EmptyBorder(0, 0, 0, 8));

        btnMinus = new JButton("...");
        btnMinus.setPreferredSize(new Dimension(28, 28));
        btnMinus.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnMinus.setFocusPainted(false);
        btnMinus.setEnabled(false);
        btnMinus.addActionListener(e -> adjustQty(-1));

        txtQty = new JTextField("0", 4);
        txtQty.setHorizontalAlignment(JTextField.CENTER);
        txtQty.setPreferredSize(new Dimension(48, 28));
        txtQty.addActionListener(e -> {
            try { qty = Math.max(0, Integer.parseInt(txtQty.getText().trim())); }
            catch (NumberFormatException ex) { qty = 0; }
            txtQty.setText(String.valueOf(qty));
            btnMinus.setEnabled(qty > 0);
        });

        JButton btnPlus = new JButton("...");
        btnPlus.setPreferredSize(new Dimension(28, 28));
        btnPlus.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnPlus.setFocusPainted(false);
        btnPlus.addActionListener(e -> adjustQty(1));

        JButton btnBuy = new JButton("BUY");
        btnBuy.setFont(new Font("SansSerif", Font.BOLD, 11));
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setBackground(ACCENT2);
        btnBuy.setOpaque(true);
        btnBuy.setBorderPainted(false);
        btnBuy.setFocusPainted(false);
        btnBuy.setPreferredSize(new Dimension(50, 28));
        btnBuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnBuy.addActionListener(e -> doBuy());

        ctrl.add(btnMinus);
        ctrl.add(txtQty);
        ctrl.add(btnPlus);
        ctrl.add(btnBuy);
        add(ctrl, BorderLayout.EAST);
    }

    private void adjustQty(int delta) {
        qty = Math.max(0, qty + delta);
        txtQty.setText(String.valueOf(qty));
        btnMinus.setEnabled(qty > 0);
    }

    private void doBuy() {
        if (qty <= 0 || gm == null || rw == null) return;
        boolean ok = gm.getRestaurant().beli(gm.getSupplier(), rw, qty);
        if (ok) {
            qty = 0;
            txtQty.setText("0");
            btnMinus.setEnabled(false);
            java.awt.Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof Frame f) f.refreshAll();
        } else {
            JOptionPane.showMessageDialog(this,
                "Saldo tidak cukup untuk membeli " + qty + "x " + rw.getName(),
                "Transaksi Gagal", JOptionPane.WARNING_MESSAGE);
        }
    }
}
