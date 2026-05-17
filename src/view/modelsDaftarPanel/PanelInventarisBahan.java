package view.modelsDaftarPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import models.RawMaterial;

/**
 * Kartu satu jenis bahan baku di inventaris dapur restoran.
 */
public class PanelInventarisBahan extends JPanel {

    private static final Color BG_CARD     = new Color(255, 253, 228);
    private static final Color BG_HABIS    = new Color(72, 68, 78);
    private static final Color TXT_NAMA    = new Color(45, 35, 30);
    private static final Color TXT_QTY     = new Color(139, 69, 19);
    private static final Color TXT_HABIS   = new Color(200, 120, 120);

    private final RawMaterial bahan;
    private final JLabel lblIcon;
    private final JLabel lblNama;
    private final JLabel lblQty;
    private final JLabel lblSatuan;

    public PanelInventarisBahan(RawMaterial bahan, int jumlah) {
        this.bahan = bahan;

        setLayout(new BorderLayout(4, 4));
        setBackground(BG_CARD);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(139, 69, 19), 2, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        setPreferredSize(new Dimension(130, 110));
        setMinimumSize(new Dimension(120, 100));

        lblIcon = new JLabel(ikonUntuk(bahan.getName()), SwingConstants.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));

        lblNama = new JLabel(bahan.getName(), SwingConstants.CENTER);
        lblNama.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblNama.setForeground(TXT_NAMA);

        lblQty = new JLabel("", SwingConstants.CENTER);
        lblQty.setFont(new Font("SansSerif", Font.BOLD, 18));

        lblSatuan = new JLabel("unit", SwingConstants.CENTER);
        lblSatuan.setFont(new Font("SansSerif", Font.PLAIN, 10));
        lblSatuan.setForeground(new Color(100, 90, 80));

        JPanel bawah = new JPanel(new BorderLayout());
        bawah.setOpaque(false);
        bawah.add(lblQty, BorderLayout.CENTER);
        bawah.add(lblSatuan, BorderLayout.SOUTH);

        add(lblNama, BorderLayout.NORTH);
        add(lblIcon, BorderLayout.CENTER);
        add(bawah, BorderLayout.SOUTH);

        setJumlah(jumlah);
    }

    public RawMaterial getBahan() {
        return bahan;
    }

    public void setJumlah(int jumlah) {
        if (jumlah <= 0) {
            lblQty.setText("0");
            lblQty.setForeground(TXT_HABIS);
            lblSatuan.setText("habis");
            setBackground(BG_HABIS);
            lblNama.setForeground(new Color(220, 210, 200));
        } else {
            lblQty.setText(String.valueOf(jumlah));
            lblQty.setForeground(TXT_QTY);
            lblSatuan.setText(jumlah == 1 ? "unit" : "unit");
            setBackground(BG_CARD);
            lblNama.setForeground(TXT_NAMA);
        }
    }

    public static String ikonUntuk(String nama) {
        if (nama == null) return "📦";
        String n = nama.toLowerCase();
        if (n.contains("beras")) return "🍚";
        if (n.contains("telor") || n.contains("telur")) return "🥚";
        if (n.contains("ayam")) return "🍗";
        if (n.contains("kopi")) return "☕";
        if (n.contains("gula")) return "🧂";
        if (n.contains("tepung")) return "🌾";
        if (n.contains("bumbu")) return "🌶";
        if (n.contains("susu")) return "🥛";
        if (n.contains("buah")) return "🍎";
        return "📦";
    }
}
