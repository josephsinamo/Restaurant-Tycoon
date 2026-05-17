package view;

import controller.GameManager;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import models.RawMaterial;
import view.modelsDaftarPanel.PanelUntukDaftarMenu;

public class PanelSuplierMenu extends JPanel {

    private static final Color BG_DARK      = new Color(26, 30, 42);
    private static final Color ACCENT       = new Color(255, 180, 50);
    private static final Color TEXT_MAIN    = new Color(230, 230, 240);
    private static final Color TEXT_DIM     = new Color(140, 145, 165);

    private GameManager gm;
    private JPanel listBahan;   // tab1: daftar bahan baku

    public PanelSuplierMenu(GameManager gm) {
        this.gm = gm;
        setBackground(BG_DARK);
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setLayout(new BorderLayout(0, 6));

        // Judul
        JLabel judul = new JLabel("🛒 Toko Supplier", SwingConstants.LEFT);
        judul.setFont(new Font("SansSerif", Font.BOLD, 14));
        judul.setForeground(ACCENT);
        judul.setBorder(new EmptyBorder(0, 2, 6, 0));
        add(judul, BorderLayout.NORTH);

        // Tabbed pane: tab Bahan Baku
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(BG_DARK);
        tabs.setForeground(TEXT_MAIN);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        tabs.addTab("🧂 Bahan Baku", buildTabBahanBaku());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildTabBahanBaku() {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG_DARK);
        wrap.setBorder(new EmptyBorder(5, 5, 5, 5));

        listBahan = new JPanel();
        listBahan.setBackground(Color.WHITE);
        listBahan.setBorder(new EmptyBorder(3, 1, 1, 1));
        listBahan.setLayout(new BoxLayout(listBahan, BoxLayout.Y_AXIS));

        if (gm != null) {
            for (RawMaterial rw : gm.getSupplier().getBahanBaku()) {
                listBahan.add(new PanelUntukDaftarMenu(rw, gm));
            }
        }

        JScrollPane scroll = new JScrollPane(listBahan);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        wrap.add(scroll, BorderLayout.CENTER);
        return wrap;
    }
}
