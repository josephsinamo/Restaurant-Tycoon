package view;

import controller.GameManager;
import core.Restaurant;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import models.RawMaterial;

public class PanelRestoranMenu extends JPanel {

    private static final Color BG_DARK      = new Color(26, 30, 42);
    private static final Color ACCENT       = new Color(255, 180, 50);
    private static final Color ACCENT2      = new Color(80, 200, 140);
    private static final Color TEXT_MAIN    = new Color(230, 230, 240);
    private static final Color BORDER_BROWN = new Color(139, 69, 19);

    private static String getAsetPath(String namaBahan) {
        return switch (namaBahan.toLowerCase()) {
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
    private JPanel container;
    private JLabel lblKapasitasInfo;

    public PanelRestoranMenu(GameManager gm) {
        this.gm = gm;
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setLayout(new BorderLayout(0, 8));
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildInventori(), BorderLayout.CENTER);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        bar.setBackground(new Color(34, 40, 56));
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 2, 0, ACCENT),
            new EmptyBorder(4, 8, 4, 8)));

        int kap = gm != null ? gm.getRestaurant().getKapasitas() : 0;
        lblKapasitasInfo = new JLabel("🏠 Kapasitas Restoran: " + kap + " kursi");
        lblKapasitasInfo.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblKapasitasInfo.setForeground(TEXT_MAIN);

        JButton btnUpgrade = styledButton("⬆ Upgrade Kapasitas (+5 kursi  |  Rp 10.000)", ACCENT, new Color(34, 40, 56), 12);
        btnUpgrade.addActionListener(e -> {
            if (gm == null) return;
            double biaya = 10000;
            Restaurant resto = gm.getRestaurant();
            if (resto.getMoney() < biaya) {
                JOptionPane.showMessageDialog(this, "Uang tidak cukup! Dibutuhkan Rp " + (int)biaya, "Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }
            resto.kurangiUang(biaya);
            resto.upgradeKapasitas(5);
            int kapBaru = resto.getKapasitas();
            lblKapasitasInfo.setText("🏠 Kapasitas Restoran: " + kapBaru + " kursi");
            java.awt.Window w = SwingUtilities.getWindowAncestor(this);
            if (w instanceof Frame f) f.refreshAll();
            JOptionPane.showMessageDialog(this, "Kapasitas naik jadi " + kapBaru + " kursi!", "Upgrade Berhasil", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnRefresh = styledButton("🔄 Refresh", ACCENT2, new Color(34, 40, 56), 12);
        btnRefresh.addActionListener(e -> refreshInventori());

        bar.add(lblKapasitasInfo);
        bar.add(Box.createHorizontalStrut(16));
        bar.add(btnUpgrade);
        bar.add(btnRefresh);
        return bar;
    }

    private JPanel buildInventori() {
        JPanel wrap = new JPanel(new BorderLayout(0, 6));
        wrap.setBackground(BG_DARK);
        wrap.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel judul = new JLabel("INVENTORI", SwingConstants.CENTER);
        judul.setFont(new Font("Showcard Gothic", Font.BOLD, 18));
        judul.setForeground(Color.WHITE);
        judul.setBorder(new EmptyBorder(0, 0, 8, 0));
        wrap.add(judul, BorderLayout.NORTH);

        container = new JPanel(new GridLayout(0, 4, 12, 12));
        container.setBackground(new Color(41, 33, 45));
        container.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_BROWN, 3));
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getViewport().setBackground(new Color(41, 33, 45));
        wrap.add(scroll, BorderLayout.CENTER);

        loadInventori();
        return wrap;
    }

    public void loadInventori() {
        if (container == null || gm == null) return;
        container.removeAll();
        for (RawMaterial rw : gm.getRestaurant().getStok().keySet()) {
            container.add(createItemCard(rw));
        }
        container.revalidate();
        container.repaint();
    }

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
}
