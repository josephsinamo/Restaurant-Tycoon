package view;

import view.modelsDaftarPanel.PanelRestoranMenu;
import controller.GameManager;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import models.jimat.Jimat;

import view.modelsDaftarPanel.*;

public class Frame extends JFrame {

    // ── Card layout constants ──────────────────────────────────────────────
    public static final String KARTU_MENU       = "menu";
    public static final String KARTU_PERMAINAN  = "permainan";

    private CardLayout cardLayout;
    private JPanel mainPanel;

    // ── Colours & fonts ────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(18, 20, 28);
    private static final Color BG_PANEL     = new Color(26, 30, 42);
    private static final Color BG_CARD      = new Color(34, 40, 56);
    private static final Color ACCENT       = new Color(255, 180, 50);
    private static final Color ACCENT2      = new Color(80, 200, 140);
    private static final Color ACCENT3      = new Color(100, 150, 255);
    private static final Color TEXT_MAIN    = new Color(230, 230, 240);
    private static final Color TEXT_DIM     = new Color(140, 145, 165);
    private static final Color DANGER       = new Color(240, 80, 80);

    private static final Font FONT_TITLE    = new Font("Serif", Font.BOLD, 48);
    private static final Font FONT_HEADING  = new Font("SansSerif", Font.BOLD, 14);
    private static final Font FONT_BODY     = new Font("Monospaced", Font.PLAIN, 12);
    private static final Font FONT_MONO     = new Font("Monospaced", Font.PLAIN, 11);

    // ── Game backend ───────────────────────────────────────────────────────
    private GameManager gameManager;

    // ── Status widgets ─────────────────────────────────────────────────────
    private JLabel lblMoney;
    private JLabel lblKapasitas;
    private JLabel lblRound;
    private JTextArea txtLog;
    private DefaultTableModel modelStok;

    // ── Jimat widgets ──────────────────────────────────────────────────────
    private JLabel lblJimatMenarik, lblJimatKebersihan, lblJimatKeamanan;
    private DefaultListModel<Jimat> listModelInventaris;
    private JList<Jimat> listInventaris;
    private DefaultTableModel modelJimatShop;

    // ── Bahan baku shop ────────────────────────────────────────────────────
    private DefaultTableModel modelBahanShop;

    // ── Menu management ────────────────────────────────────────────────────
    private DefaultTableModel modelMenu;

    // ── NPC animation panel ────────────────────────────────────────────────
    private NpcPanel npcPanel;

    // ══════════════════════════════════════════════════════════════════════
    public Frame() {
        setTitle("🍜 Warung Nusantara — Restaurant Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(BG_DARK);

        mainPanel.add(buildMenuUtama(),   KARTU_MENU);
        mainPanel.add(buildPermainan(),   KARTU_PERMAINAN);

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, KARTU_MENU);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    // ══ MAIN MENU ══════════════════════════════════════════════════════════
    private JPanel buildMenuUtama() {
        JPanel p = new JPanel(new GridBagLayout()) {
            @Override 
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, new Color(18, 20, 28),
                        getWidth(), getHeight(), new Color(30, 15, 50)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative circles
                g2.setColor(new Color(255, 180, 50, 25));
                g2.fillOval(-80, -80, 400, 400);
                g2.setColor(new Color(80, 200, 140, 20));
                g2.fillOval(getWidth() - 250, getHeight() - 200, 400, 350);
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel emoji = new JLabel("🍜", SwingConstants.CENTER);
        emoji.setFont(new Font("Serif", Font.PLAIN, 72));

        JLabel title = new JLabel("WARUNG NUSANTARA", SwingConstants.CENTER);
        title.setFont(FONT_TITLE);
        title.setForeground(ACCENT);

        JLabel sub = new JLabel("Restaurant Management Game", SwingConstants.CENTER);
        sub.setFont(new Font("SansSerif", Font.ITALIC, 18));
        sub.setForeground(TEXT_DIM);

        JButton btnStart = styledButton("▶  MULAI GAME", ACCENT, BG_DARK, 18);
        btnStart.setPreferredSize(new Dimension(280, 56));
        btnStart.addActionListener(e -> startGame());

        JButton btnQuit = styledButton("✕  KELUAR", DANGER, BG_DARK, 14);
        btnQuit.setPreferredSize(new Dimension(180, 42));
        btnQuit.addActionListener(e -> System.exit(0));

        p.add(emoji, gbc);
        p.add(title, gbc);
        p.add(sub, gbc);
        p.add(Box.createVerticalStrut(20), gbc);
        p.add(btnStart, gbc);
        p.add(btnQuit, gbc);

        return p;
    }

    // ══ GAME PANEL ═════════════════════════════════════════════════════════
    private JPanel buildPermainan() {
        JPanel root = new JPanel(new BorderLayout(6, 6));
        root.setBackground(BG_DARK);
        root.setBorder(new EmptyBorder(8, 8, 8, 8));

        // ── Top bar ──────────────────────────────────────────────
        root.add(buildTopBar(), BorderLayout.NORTH);

        // ── Centre: tabbed panels + NPC area ─────────────────────
        JSplitPane centre = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildLeftTabs(), buildNpcArea());
        centre.setDividerLocation(680);
        centre.setDividerSize(6);
        centre.setBackground(BG_DARK);
        root.add(centre, BorderLayout.CENTER);

        // ── Bottom: log ──────────────────────────────────────────
        root.add(buildLogPanel(), BorderLayout.SOUTH);

        return root;
    }

    // ── Top bar ────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        bar.setBackground(BG_PANEL);
        bar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 2, 0, ACCENT),
                new EmptyBorder(4, 12, 4, 12)));

        JLabel logo = new JLabel("🍜 WARUNG NUSANTARA");
        logo.setFont(new Font("Serif", Font.BOLD, 20));
        logo.setForeground(ACCENT);

        lblMoney     = statusLabel("💰 Rp 0");
        lblKapasitas = statusLabel("🏠 Kapasitas: 0");
        lblRound     = statusLabel("📅 Ronde: -/-");


        JButton btnMenu = styledButton("← Menu", TEXT_DIM, BG_CARD, 11);

        // btnMenu.addActionListener(e -> {
        //     if (gameManager != null) gameManager.pauseGame();
        //     cardLayout.show(mainPanel, KARTU_MENU);
        // });

        bar.add(logo);
        bar.add(sep());
        bar.add(lblMoney);
        bar.add(sep());
        bar.add(lblKapasitas);
        bar.add(sep());
        bar.add(lblRound);
        bar.add(Box.createHorizontalGlue());
        bar.add(btnMenu);

        return bar;
    }

    // ── Left tabs ──────────────────────────────────────────────────────────
    private JTabbedPane buildLeftTabs() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(BG_PANEL);
        tabs.setForeground(TEXT_MAIN);
        tabs.setFont(FONT_HEADING);

        tabs.addTab("📊 Status",       buildStatusPanel());
        tabs.addTab("🏪 Restoran",       buildRestoranPanel());
        tabs.addTab("🛒 Supplier",  buildBahanBakuPanel());
        tabs.addTab("📋 Menu",         buildMenuPanel());
        tabs.addTab("🧿 Jimat",        buildJimatPanel());

        return tabs;
    }

    // ── PANEL: Bahan Baku Shop ─────────────────────────────────────────────
    private JPanel buildBahanBakuPanel() {
        JPanel p = new PanelSuplierMenu();
        
        return p;
    }

    // ── PANEL: Jimat ──────────────────────────────────────────────────────
    private JPanel buildJimatPanel() {
        JPanel p = darkPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Equipped slots
        JPanel slots = darkPanel(new GridLayout(1, 3, 8, 0));
        lblJimatMenarik   = jimatSlotLabel("✨ Menarik", "—");
        lblJimatKebersihan = jimatSlotLabel("🧹 Kebersihan", "—");
        lblJimatKeamanan  = jimatSlotLabel("🛡 Keamanan", "—");
        slots.add(wrapJimatSlot("Charming", lblJimatMenarik));
        slots.add(wrapJimatSlot("Cleaner",  lblJimatKebersihan));
        slots.add(wrapJimatSlot("Security", lblJimatKeamanan));
        p.add(slots, BorderLayout.NORTH);

        // Split: shop top, inventory bottom
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBackground(BG_PANEL);
        split.setDividerLocation(190);

        // Jimat shop
        JPanel shopPanel = darkPanel(new BorderLayout(4, 4));
        shopPanel.setBorder(titled("Toko Jimat"));
        String[] shopCols = {"Jimat", "Tipe", "Power", "Harga (Rp)"};
        modelJimatShop = new DefaultTableModel(shopCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable shopTbl = styledTable(modelJimatShop);
        shopPanel.add(new JScrollPane(shopTbl), BorderLayout.CENTER);

        JPanel shopBtns = darkPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBeliJimat = styledButton("Beli Jimat", ACCENT2, BG_CARD, 11);
        // btnBeliJimat.addActionListener(e -> {
        //     int row = shopTbl.getSelectedRow();
        //     if (row < 0) { showMsg("Pilih jimat dari toko."); return; }
        //     String nama = (String) modelJimatShop.getValueAt(row, 0);
        //     Jimat target = gameManager.getSupplier().getDaganganJimat().keySet().stream()
        //             .filter(j -> j.getName().equals(nama)).findFirst().orElse(null);
        //     if (target == null) return;
        //     boolean ok = gameManager.getRestaurant().beli(gameManager.getSupplier(), target, 1);
        //     if (!ok) showMsg("Gagal membeli jimat — uang tidak cukup?");
        //     refreshAll();
        // });
        shopBtns.add(btnBeliJimat);
        shopPanel.add(shopBtns, BorderLayout.SOUTH);
        split.setTopComponent(shopPanel);

        // Inventory
        JPanel invPanel = darkPanel(new BorderLayout(4, 4));
        invPanel.setBorder(titled("Inventaris Jimat"));
        listModelInventaris = new DefaultListModel<>();
        listInventaris = new JList<>(listModelInventaris);
        listInventaris.setBackground(BG_CARD);
        listInventaris.setForeground(TEXT_MAIN);
        listInventaris.setFont(FONT_BODY);
        listInventaris.setCellRenderer(new JimatListRenderer());
        invPanel.add(new JScrollPane(listInventaris), BorderLayout.CENTER);

        JPanel invBtns = darkPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPasang = styledButton("Pasang", ACCENT3, BG_CARD, 11);
        JButton btnJual   = styledButton("Jual", DANGER, BG_CARD, 11);

        // btnPasang.addActionListener(e -> {
        //     Jimat sel = listInventaris.getSelectedValue();
        //     if (sel == null) { showMsg("Pilih jimat dari inventaris."); return; }
        //     boolean ok = gameManager.getRestaurant().pakaiJimatDariInventori(sel);
        //     if (!ok) showMsg("Tidak bisa memasang jimat ini.");
        //     refreshAll();
        // });

        // btnJual.addActionListener(e -> {
        //     Jimat sel = listInventaris.getSelectedValue();
        //     if (sel == null) { showMsg("Pilih jimat dari inventaris."); return; }
        //     gameManager.getRestaurant().jualJimat(sel);
        //     refreshAll();
        // });

        invBtns.add(btnPasang);
        invBtns.add(btnJual);
        invPanel.add(invBtns, BorderLayout.SOUTH);
        split.setBottomComponent(invPanel);

        p.add(split, BorderLayout.CENTER);
        return p;
    }

    // ── PANEL: Menu Management ─────────────────────────────────────────────
    private JPanel buildMenuPanel() {
        JPanel p = darkPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] cols = {"Nama Menu", "Tipe", "Harga (Rp)"};
        modelMenu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 2; }
        };
        JTable tbl = styledTable(modelMenu);
        p.add(new JScrollPane(tbl), BorderLayout.CENTER);

        // Add new menu form
        JPanel form = darkPanel(new GridBagLayout());
        form.setBorder(titled("Tambah / Atur Menu"));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4, 6, 4, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        JTextField fldNama  = darkField();
        JTextField fldHarga = darkField();
        JComboBox<String> cmbTipe = new JComboBox<>(new String[]{"Food", "Snack", "Coffe"});
        cmbTipe.setBackground(BG_CARD); cmbTipe.setForeground(TEXT_MAIN);

        gc.gridx = 0; gc.gridy = 0; form.add(lbl("Nama:"), gc);
        gc.gridx = 1; form.add(fldNama, gc);
        gc.gridx = 0; gc.gridy = 1; form.add(lbl("Tipe:"), gc);
        gc.gridx = 1; form.add(cmbTipe, gc);
        gc.gridx = 0; gc.gridy = 2; form.add(lbl("Harga (Rp):"), gc);
        gc.gridx = 1; form.add(fldHarga, gc);

        JButton btnAdd = styledButton("+ Tambah Menu", ACCENT, BG_CARD, 11);
        // btnAdd.addActionListener(e -> {
        //     String nama = fldNama.getText().trim();
        //     String hargaStr = fldHarga.getText().trim();
        //     if (nama.isEmpty() || hargaStr.isEmpty()) { showMsg("Isi nama dan harga."); return; }
        //     double harga;
        //     try { harga = Double.parseDouble(hargaStr); } catch (Exception ex) { showMsg("Harga tidak valid."); return; }
        //     String tipe = (String) cmbTipe.getSelectedItem();
        //     Menu menu = switch (tipe) {
        //         case "Snack" -> new Snack(nama, (int) harga);
        //         case "Coffe" -> new Coffe(nama, (int) harga);
        //         default      -> new Food(nama, (int) harga);
        //     };
        //     gameManager.getRestaurant().addMenu(menu, harga);
        //     fldNama.setText(""); fldHarga.setText("");
        //     refreshAll();
        // });

        JButton btnSetHarga = styledButton("💲 Set Harga Terpilih", ACCENT2, BG_CARD, 11);
        // btnSetHarga.addActionListener(e -> {
        //     int row = tbl.getSelectedRow();
        //     if (row < 0) { showMsg("Pilih menu dari tabel."); return; }
        //     String hargaStr = (String) modelMenu.getValueAt(row, 2);
        //     try {
        //         double h = Double.parseDouble(hargaStr);
        //         String nama = (String) modelMenu.getValueAt(row, 0);
        //         Menu target = Arrays.stream(gameManager.getRestaurant().lihatDaftarMenu())
        //                 .filter(m -> m.getName().equals(nama)).findFirst().orElse(null);
        //         if (target != null) gameManager.getRestaurant().setHarga(target, h);
        //     } catch (Exception ex) { showMsg("Nilai harga tidak valid."); }
        //     refreshAll();
        // });

        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        JPanel btnRow = darkPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btnRow.add(btnAdd); btnRow.add(btnSetHarga);
        form.add(btnRow, gc);

        p.add(form, BorderLayout.SOUTH);
        return p;
    }

    // ── PANEL: Status ──────────────────────────────────────────────────────
    private JPanel buildStatusPanel() {
        JPanel p = darkPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Stok table
        String[] stokCols = {"Bahan Baku", "Jumlah Stok"};
        modelStok = new DefaultTableModel(stokCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tblStok = styledTable(modelStok);
        JScrollPane stokScroll = new JScrollPane(tblStok);
        stokScroll.setBorder(titled("Stok Bahan Baku"));

        // Upgrade capacity button
        JButton btnUpgrade = styledButton("⬆ Upgrade Kapasitas (+10)", ACCENT3, BG_CARD, 11);
        // btnUpgrade.addActionListener(e -> {
        //     gameManager.getRestaurant().upgradeKapasitas(10);
        //     refreshAll();
        // });

        JPanel north = darkPanel(new BorderLayout());
        north.add(stokScroll, BorderLayout.CENTER);
        north.add(btnUpgrade, BorderLayout.SOUTH);
        p.add(north, BorderLayout.CENTER);

        return p;
    }

    // Build Restoan Menu
    private JPanel buildRestoranPanel() {
        JPanel p = new PanelRestoranMenu();
        
        return p;
    }

    // ── LOG PANEL ──────────────────────────────────────────────────────────
    private JPanel buildLogPanel() {
        JPanel p = darkPanel(new BorderLayout());
        p.setPreferredSize(new Dimension(0, 140));
        p.setBorder(titled("📜 Log Aktivitas"));

        txtLog = new JTextArea();
        txtLog.setFont(FONT_MONO);
        txtLog.setBackground(new Color(14, 16, 24));
        txtLog.setForeground(ACCENT2);
        txtLog.setEditable(false);
        txtLog.setLineWrap(true);
        txtLog.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setBorder(null);

        JButton btnClear = styledButton("Clear", TEXT_DIM, BG_CARD, 10);
        // btnClear.addActionListener(e -> txtLog.setText(""));

        p.add(scroll, BorderLayout.CENTER);
        p.add(btnClear, BorderLayout.EAST);
        return p;
    }

    // ── NPC ANIMATION AREA ─────────────────────────────────────────────────
    private JPanel buildNpcArea() {
        JPanel wrap = darkPanel(new BorderLayout(0, 6));
        wrap.setBorder(new EmptyBorder(4, 4, 4, 4));

        JLabel heading = heading("🏪 Area Restoran");
        wrap.add(heading, BorderLayout.NORTH);

        npcPanel = new NpcPanel();
        npcPanel.setBackground(new Color(22, 28, 40));
        npcPanel.setBorder(new LineBorder(ACCENT, 1));
        wrap.add(npcPanel, BorderLayout.CENTER);

        return wrap;
    }

    // ══ NPC PANEL (animation) ══════════════════════════════════════════════
    private static class Npc {
        float x, y, vx, vy;
        Color color;
        String label;
        int size;

        Npc(float x, float y, Color color, String label) {
            this.x = x; this.y = y;
            this.color = color;
            this.label = label;
            this.size = 22 + (int)(Math.random() * 10);
            this.vx = (float)(Math.random() * 2 - 1) * 1.8f;
            this.vy = (float)(Math.random() * 2 - 1) * 1.8f;
        }

        void update(int W, int H) {
            x += vx; y += vy;
            if (x < 0)     { x = 0;     vx = Math.abs(vx); }
            if (y < 0)     { y = 0;     vy = Math.abs(vy); }
            if (x > W - size) { x = W - size; vx = -Math.abs(vx); }
            if (y > H - size) { y = H - size; vy = -Math.abs(vy); }
            // Slight random drift
            vx += (Math.random() - 0.5) * 0.15;
            vy += (Math.random() - 0.5) * 0.15;
            float maxSpeed = 2.5f;
            vx = Math.max(-maxSpeed, Math.min(maxSpeed, vx));
            vy = Math.max(-maxSpeed, Math.min(maxSpeed, vy));
        }
    }

    class NpcPanel extends JPanel {
        private final List<Npc> npcs = new ArrayList<>();
        private Timer animTimer;

        NpcPanel() {
            animTimer = new Timer(33, e -> { // ~30 FPS
                for (Npc npc : npcs) npc.update(getWidth(), getHeight());
                repaint();
            });
            animTimer.start();
        }

        void setNpcCount(int count) {
            npcs.clear();
            Color[] palette = {
                new Color(255, 180, 50), new Color(80, 200, 140),
                new Color(100, 150, 255), new Color(220, 100, 160),
                new Color(255, 120, 80),  new Color(160, 100, 255)
            };
            Random rng = new Random();
            for (int i = 0; i < count; i++) {
                Color c = palette[i % palette.length];
                float x = 40 + rng.nextFloat() * (getWidth() - 80);
                float y = 40 + rng.nextFloat() * (getHeight() - 80);
                npcs.add(new Npc(x, y, c, "P" + (i + 1)));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Floor grid
            g2.setColor(new Color(40, 50, 65));
            int gridSize = 40;
            for (int gx = 0; gx < getWidth(); gx += gridSize)
                g2.drawLine(gx, 0, gx, getHeight());
            for (int gy = 0; gy < getHeight(); gy += gridSize)
                g2.drawLine(0, gy, getWidth(), gy);

            // Tables (static furniture)
            g2.setColor(new Color(60, 45, 30));
            int[][] tables = {{80, 80}, {200, 80}, {320, 80}, {80, 200}, {200, 200}, {320, 200}};
            for (int[] t : tables) {
                if (t[0] + 50 < getWidth() && t[1] + 35 < getHeight()) {
                    g2.fillRoundRect(t[0], t[1], 50, 35, 8, 8);
                    g2.setColor(new Color(90, 70, 45));
                    g2.drawRoundRect(t[0], t[1], 50, 35, 8, 8);
                    g2.setColor(new Color(60, 45, 30));
                }
            }

            // Counter
            g2.setColor(new Color(100, 70, 30));
            g2.fillRect(0, getHeight() - 50, getWidth(), 50);
            g2.setColor(ACCENT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 13));
            g2.drawString("🍽 KASIR / COUNTER", 20, getHeight() - 22);

            // NPCs
            for (Npc npc : npcs) {
                // Shadow
                g2.setColor(new Color(0, 0, 0, 60));
                g2.fillOval((int)npc.x + 3, (int)npc.y + npc.size - 4, npc.size - 6, 8);

                // Body circle
                g2.setColor(npc.color);
                g2.fill(new Ellipse2D.Float(npc.x, npc.y, npc.size, npc.size));

                // Highlight
                g2.setColor(new Color(255, 255, 255, 80));
                g2.fill(new Ellipse2D.Float(npc.x + 4, npc.y + 3, npc.size / 3f, npc.size / 4f));

                // Label
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 9));
                FontMetrics fm = g2.getFontMetrics();
                int tw = fm.stringWidth(npc.label);
                g2.drawString(npc.label, npc.x + (npc.size - tw) / 2f, npc.y + npc.size / 2f + 4);
            }

            // Customer count badge
            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillRoundRect(8, 8, 160, 28, 10, 10);
            g2.setColor(ACCENT);
            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            g2.drawString("👥 Pelanggan: " + npcs.size(), 16, 27);

            g2.dispose();
        }
    }

    // ══ GAME LIFECYCLE ═════════════════════════════════════════════════════
    private void startGame() {
        gameManager = new GameManager();

        // Connect IO bus → log area
        // IO.addListener(event -> SwingUtilities.invokeLater(() -> {
        //     appendLog(event.getMessage());
        //     refreshAll();
        // }));

        // gameManager.setRoundListener(new GameManager.RoundListener() {
        //     @Override public void onRoundChange(int round, int total) {
        //         SwingUtilities.invokeLater(() -> {
        //             lblRound.setText("📅 Ronde: " + round + "/" + total);
        //             // Simulate customers arriving visually
        //             int count = (int)(Math.random() * 8) + 2;
        //             npcPanel.setNpcCount(count);
        //         });
        //     }
        //     @Override public void onGameOver(boolean won, double finalMoney) {
        //         SwingUtilities.invokeLater(() -> {
        //             String msg = won
        //                 ? "🎉 SELAMAT! Anda menang!\nUang akhir: Rp " + String.format("%.0f", finalMoney)
        //                 : "😢 Game Over!\nUang akhir: Rp " + String.format("%.0f", finalMoney);
        //             JOptionPane.showMessageDialog(Frame.this, msg,
        //                 won ? "Menang!" : "Game Over",
        //                 won ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
        //         });
        //     }
        // });

        cardLayout.show(mainPanel, KARTU_PERMAINAN);
        refreshAll();
        // gameManager.startGame();
    }

    // ══ REFRESH ════════════════════════════════════════════════════════════
    private void refreshAll() {
        if (gameManager == null) return;
        // Restaurant resto = gameManager.getRestaurant();

        // Top bar
        lblMoney.setText("💰 Rp " + String.format("l"));
        lblKapasitas.setText("🏠 Kapasitas: " );

        // Stok table
        modelStok.setRowCount(0);
        // resto.getStokBahanBaku().forEach((rm, qty) ->
        //     modelStok.addRow(new Object[]{rm.getName(), qty}));

        // Bahan shop table
        modelBahanShop.setRowCount(0);
        // gameManager.getSupplier().getDaganganBahanBaku().forEach((rm, harga) -> {
        //     int stok = resto.getStokBahanBaku().getOrDefault(rm, 0);
        //     modelBahanShop.addRow(new Object[]{rm.getName(), (int)(double)harga, stok, 1});
        // });

        // Jimat shop
        modelJimatShop.setRowCount(0);
        // gameManager.getSupplier().getDaganganJimat().forEach((jimat, harga) ->
        //     modelJimatShop.addRow(new Object[]{
        //         jimat.getName(),
        //         jimat.getClass().getSimpleName(),
        //         jimat.getPower(),
        //         (int)(double)harga
        //     }));

        // Jimat inventory
        listModelInventaris.clear();
        // resto.getInventarisJimat().forEach(listModelInventaris::addElement);

        // Equipped jimat labels
        // lblJimatMenarik.setText(resto.getJimatMenarik() != null
        //     ? resto.getJimatMenarik().getName() + " (" + resto.getPoinJimatMenarik() + ")" : "—");
        // lblJimatKebersihan.setText(resto.getJimatKebersihan() != null
        //     ? resto.getJimatKebersihan().getName() + " (" + resto.getPoinJimatKebersihan() + ")" : "—");
        // lblJimatKeamanan.setText(resto.getJimatKeamanan() != null
        //     ? resto.getJimatKeamanan().getName() + " (" + resto.getPoinJimatKeamanan() + ")" : "—");

        // Menu table
        modelMenu.setRowCount(0);
        // resto.getDaftarMenuMap().forEach((menu, harga) ->
        //     modelMenu.addRow(new Object[]{
        //         menu.getName(),
        //         menu.getClass().getSimpleName(),
        //         String.valueOf(harga.intValue())
        //     }));
    }

    private void appendLog(String msg) {
        if (msg == null || msg.isBlank()) return;
        txtLog.append("[" + java.time.LocalTime.now().withNano(0) + "] " + msg + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    // ══ UI HELPERS ═════════════════════════════════════════════════════════
    private JButton styledButton(String text, Color fg, Color bg, int size) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, size));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(6, 16, 6, 16));
        b.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            @Override public void mouseEntered(MouseEvent e) {
                b.setBackground(orig.brighter());
            }
            @Override public void mouseExited(MouseEvent e) {
                b.setBackground(orig);
            }
        });
        return b;
    }

    private JPanel darkPanel(LayoutManager lm) {
        JPanel p = new JPanel(lm);
        p.setBackground(BG_PANEL);
        return p;
    }

    private JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEADING);
        l.setForeground(ACCENT);
        l.setBorder(new EmptyBorder(0, 0, 6, 0));
        return l;
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT_MAIN);
        return l;
    }

    private JLabel statusLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 13));
        l.setForeground(TEXT_MAIN);
        return l;
    }

    private JLabel jimatSlotLabel(String type, String value) {
        JLabel l = new JLabel(value, SwingConstants.CENTER);
        l.setFont(FONT_BODY);
        l.setForeground(ACCENT3);
        return l;
    }

    private JPanel wrapJimatSlot(String type, JLabel valueLabel) {
        JPanel p = darkPanel(new BorderLayout(2, 2));
        p.setBackground(BG_CARD);
        p.setBorder(new CompoundBorder(
            new LineBorder(ACCENT3.darker(), 1, true),
            new EmptyBorder(6, 8, 6, 8)));
        JLabel title = new JLabel(type, SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 11));
        title.setForeground(TEXT_DIM);
        p.add(title, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }

    private JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setBackground(BG_CARD);
        t.setForeground(TEXT_MAIN);
        t.setFont(FONT_BODY);
        t.setGridColor(new Color(50, 60, 80));
        t.setRowHeight(24);
        t.getTableHeader().setBackground(BG_PANEL);
        t.getTableHeader().setForeground(ACCENT);
        t.getTableHeader().setFont(FONT_HEADING);
        t.setSelectionBackground(ACCENT.darker().darker());
        t.setSelectionForeground(Color.WHITE);
        return t;
    }

    private JTextField darkField() {
        JTextField f = new JTextField(14);
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_MAIN);
        f.setCaretColor(TEXT_MAIN);
        f.setFont(FONT_BODY);
        f.setBorder(new CompoundBorder(
            new LineBorder(new Color(60, 70, 100), 1),
            new EmptyBorder(3, 6, 3, 6)));
        return f;
    }

    private Border titled(String title) {
        TitledBorder b = BorderFactory.createTitledBorder(
            new LineBorder(new Color(60, 70, 100), 1), title);
        b.setTitleColor(TEXT_DIM);
        b.setTitleFont(new Font("SansSerif", Font.BOLD, 11));
        return new CompoundBorder(b, new EmptyBorder(4, 4, 4, 4));
    }

    private JSeparator sep() {
        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setForeground(new Color(60, 70, 100));
        s.setPreferredSize(new Dimension(1, 20));
        return s;
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Custom list cell renderer ──────────────────────────────────────────
    private static class JimatListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean sel, boolean focus) {
            super.getListCellRendererComponent(list, value, index, sel, focus);
            setBackground(sel ? new Color(50, 70, 110) : BG_CARD);
            setForeground(TEXT_MAIN);
            setFont(FONT_BODY);
            if (value instanceof Jimat j) {
                String icon = switch (j.getClass().getSimpleName()) {
                    case "Charming" -> "✨ ";
                    case "Cleaner"  -> "🧹 ";
                    case "Security" -> "🛡 ";
                    default         -> "🧿 ";
                };
                setText(icon + j.getName() + "  [power=" + j.getPower() + "]");
            }
            setBorder(new EmptyBorder(3, 8, 3, 8));
            return this;
        }
    }

    // ══ Entry point ════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Frame::new);
    }
}
