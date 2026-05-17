package view;

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
import models.jimat.*;
import view.modelsDaftarPanel.PanelInventarisJimat;

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
    private GameManager gameManager= new GameManager();

    // ── Status widgets ─────────────────────────────────────────────────────
    private JLabel lblMoney;
    private JLabel lblKapasitas;
    private JLabel lblDay;
    private JTextArea txtLog;


    // ── Jimat widgets ──────────────────────────────────────────────────────
    private JLabel lblJimatMenarik, lblJimatKebersihan, lblJimatKeamanan;
    private DefaultListModel<Jimat> model = new DefaultListModel<>();;
    private JList<Jimat> listInventaris;

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
        centre.setDividerLocation(1050);
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
        lblDay     = statusLabel("📅 Day: 1");


        JButton btnMenu = styledButton("← Menu", TEXT_DIM, BG_CARD, 11);

        btnMenu.addActionListener(e -> {
            if (gameManager != null) gameManager.pauseGame();
            cardLayout.show(mainPanel, KARTU_MENU);
        });

        bar.add(logo);
        bar.add(sep());
        bar.add(lblMoney);
        bar.add(sep());
        bar.add(lblKapasitas);
        bar.add(sep());
        bar.add(lblDay);
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
        PanelSuplierMenu p = new PanelSuplierMenu(gameManager);

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

        // Inventory
        JPanel invPanel = darkPanel(new BorderLayout(4, 4));
        invPanel.setBorder(titled("Inventaris Jimat"));

        listInventaris = new JList<>(model); 
        listInventaris.setCellRenderer(new PanelInventarisJimat());
        
        invPanel.add(new JScrollPane(listInventaris), BorderLayout.CENTER);

        JPanel invBtns = darkPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPasang = styledButton("Pasang", ACCENT3, BG_CARD, 11);
        JButton btnJual   = styledButton("Jual", DANGER, BG_CARD, 11);

        btnPasang.addActionListener(e -> {
            Jimat sel = listInventaris.getSelectedValue();
            if (sel == null) { 
                showMsg("Pilih jimat dari inventaris."); 
                return; 
            }

            // Memastikan gameManager dan Restaurant tidak null sebelum eksekusi
            if (gameManager != null && gameManager.getRestaurant() != null) {
                boolean ok = gameManager.getRestaurant().pakaiJimatDariInventori(sel);
                if (!ok) {
                    showMsg("Tidak bisa memasang jimat ini.");
                } else {
                    refreshAll(); // Muat ulang UI jika berhasil terpasang
                }
            }
        });

        btnJual.addActionListener(e -> {
            Jimat sel = listInventaris.getSelectedValue();
            if (sel == null) { 
                showMsg("Pilih jimat dari inventaris."); 
                return; 
            }

            if (gameManager != null && gameManager.getRestaurant() != null) {
                gameManager.getRestaurant().jualJimat(sel);
                refreshAll(); // Muat ulang UI setelah jimat terjual (hilang dari list)
            }
        });


        invBtns.add(btnPasang);
        invBtns.add(btnJual);
        invPanel.add(invBtns, BorderLayout.SOUTH);

        p.add(invPanel, BorderLayout.CENTER);
        return p;
    }

    // ── PANEL: Menu Management ─────────────────────────────────────────────
    private JPanel buildMenuPanel() {
        JPanel p = new PanelMakananMenu();
        return p;
    }

    // ── PANEL: Status ──────────────────────────────────────────────────────
    private JPanel buildStatusPanel() {
        JPanel p = darkPanel(new BorderLayout(6, 6));


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
        wrap.setPreferredSize(new Dimension(100, 0)); // tambah baris ini, atur angka 450

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
    float x, y;
    Color color;
    String label;
    int size;
    
    // arah: 0=kanan, 1=kiri, 2=bawah, 3=atas
    int arah;
    int langkahSisa;
    float speed = 1.5f;
    private final Random rng = new Random();

    Npc(float x, float y, Color color, String label) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.label = label;
        this.size = 32;
        this.arah = rng.nextInt(4);
        this.langkahSisa = 30 + rng.nextInt(50);
    }

    void update(int W, int H, List<Rectangle> obstacles) {
        // ganti arah kalau langkah habis
        if (langkahSisa <= 0) {
            arah = rng.nextInt(4);
            langkahSisa = 30 + rng.nextInt(50);
        }

        float nx = x, ny = y;
        switch (arah) {
            case 0 -> nx += speed; // kanan
            case 1 -> nx -= speed; // kiri
            case 2 -> ny += speed; // bawah
            case 3 -> ny -= speed; // atas
        }

        // batas layar
        if (nx < 0 || nx > W - size || ny < 0 || ny > H - size) {
            arah = rng.nextInt(4);
            langkahSisa = 20;
            return;
        }

        // cek tabrakan dengan obstacle (meja & kasir)
        Rectangle npcRect = new Rectangle((int)nx, (int)ny, size, size);
        for (Rectangle obs : obstacles) {
            if (npcRect.intersects(obs)) {
                arah = rng.nextInt(4); // ganti arah kalau nabrak
                langkahSisa = 20;
                return;
            }
        }

        x = nx;
        y = ny;
        langkahSisa--;
    }
}

    class NpcPanel extends JPanel {
        private final List<Npc> npcs = new ArrayList<>();
        private final List<Rectangle> obstacles = new ArrayList<>(); // tambah ini
        private Timer animTimer;

        NpcPanel() {
            animTimer = new Timer(33, e -> {
                buildObstacles(); // update obstacle tiap frame (kalau resize)
                for (Npc npc : npcs) npc.update(getWidth(), getHeight(), obstacles);
                repaint();
            });
            animTimer.start();
        }

        // posisi meja & kasir harus sama dengan yang di paintComponent
        private void buildObstacles() {
            obstacles.clear();
            
            // meja — sesuaikan koordinat dengan yang di paintComponent
            int[][] tables = {{80,80},{200,80},{320,80},{80,200},{200,200},{320,200}};
            for (int[] t : tables) {
                obstacles.add(new Rectangle(t[0], t[1], 50, 35));
            }
            
            // kasir
            obstacles.add(new Rectangle(
                getWidth()/2 - 60,  // x kasir
                getHeight() - 70,   // y kasir
                120, 70             // ukuran kasir
            ));
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
                // spawn di area aman (tengah layar, jauh dari meja)
                float x = 100 + rng.nextFloat() * 200;
                float y = 300 + rng.nextFloat() * 100;
                npcs.add(new Npc(x, y, c, "P" + (i + 1)));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Floor grid
            java.net.URL lantaiUrl = getClass().getResource("/view/Aset/Lantai.png");
            if (lantaiUrl != null) {
                Image lantaiImg = new ImageIcon(lantaiUrl).getImage();
                int tileSize = 40; // ukuran tile lantai
                for (int gx = 0; gx < getWidth(); gx += tileSize) {
                    for (int gy = 0; gy < getHeight(); gy += tileSize) {
                        g2.drawImage(lantaiImg, gx, gy, tileSize, tileSize, null);
                    }
                }
            } else {
                // fallback grid kalau gambar tidak ada
                g2.setColor(new Color(40, 50, 65));
                int gridSize = 40;
                for (int gx = 0; gx < getWidth(); gx += gridSize)
                    g2.drawLine(gx, 0, gx, getHeight());
                for (int gy = 0; gy < getHeight(); gy += gridSize)
                    g2.drawLine(0, gy, getWidth(), gy);
            }

            // Tables
            ImageIcon mejaImg = new ImageIcon(getClass().getResource("/view/Aset/Meja.png"));
            int[][] tables = {{80,80},{200,80},{320,80},{80,200},{200,200},{320,200}};
            for (int[] t : tables) {
                g2.drawImage(mejaImg.getImage(), t[0], t[1], 50, 35, null);
            }

            // Kasir
            ImageIcon kasirImg = new ImageIcon(getClass().getResource("/view/Aset/Kasir.png"));
            g2.drawImage(kasirImg.getImage(),
                getWidth()/2 - 60, getHeight() - 70, 120, 70, null);

            // NPCs
            ImageIcon npc1Img = new ImageIcon(getClass().getResource("/view/Aset/NPC1.png"));
            ImageIcon npc2Img = new ImageIcon(getClass().getResource("/view/Aset/NPC2.png"));
            for (int i = 0; i < npcs.size(); i++) {
                Npc npc = npcs.get(i);
                ImageIcon npcImg = (i % 2 == 0) ? npc1Img : npc2Img;
                g2.drawImage(npcImg.getImage(), (int)npc.x, (int)npc.y, npc.size, npc.size, null);
            }

            // Badge pelanggan
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
        cardLayout.show(mainPanel, KARTU_PERMAINAN);
        npcPanel.setNpcCount(5); // tambah baris ini
        refreshAll();
    }

    // ══ REFRESH ════════════════════════════════════════════════════════════
    public void refreshAll() {
        if (gameManager == null) return;
        // Restaurant resto = gameManager.getRestaurant();

        // Top bar
        lblMoney.setText("💰 Rp " + String.format("%.0f",gameManager.getRestaurant().getMoney()));
        lblKapasitas.setText("🏠 Kapasitas: "+ String.format("%d", gameManager.getRestaurant().getKapasitas()) );
        lblDay.setText("📅 Day: "+String.format("%d", gameManager.getCurrentDay()));


        // jimat manager
        model.clear();

        for (Jimat j : gameManager.getRestaurant().getDaftarJimat()){
            model.addElement(j);
        }
        
        Jimat m = gameManager.getRestaurant().getJimatCharming();
        lblJimatMenarik.setText(m != null ? m.getName() + " (" + m.getPower() + ")" : "—");
    
        Jimat c = gameManager.getRestaurant().getJimatCleaner();
        lblJimatKebersihan.setText(c != null ? c.getName() + " (" + c.getPower() + ")" : "—");
    
        Jimat s = gameManager.getRestaurant().getJimatSecurity();
        lblJimatKeamanan.setText(s != null ? s.getName() + " (" + s.getPower() + ")" : "—");
        
        this.revalidate();
        this.repaint();
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
