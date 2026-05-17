package view;

import controller.GameManager;
import core.Restaurant;
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
import javax.swing.SwingUtilities;

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
    private GameManager gameManager ;

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
        gameManager = new GameManager();
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

    public void setLabelMoney(String x){
        lblMoney.setText(x);
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
            refreshAll();
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
    // tombol fase
        JButton btnFase = styledButton("▶ Mulai Berjualan", ACCENT2, BG_CARD, 12);
        btnFase.addActionListener(e -> {
            if (gameManager == null) return;
            if (gameManager.getFaseSekarang() == GameManager.Fase.PERSIAPAN) {
                gameManager.mulaiberjualan();
                btnFase.setText("➡ Hari Berikutnya");
                refreshAll();
                appendLog("=== Fase Berjualan Dimulai ===");
            } else {
                gameManager.nextDay();
                btnFase.setText("▶ Mulai Berjualan");
                refreshAll();
                appendLog("=== Hari ke-" + gameManager.getCurrentDay() + " — Persiapan ===");
            }
        });
        bar.add(sep());
        bar.add(btnFase);
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
   
        JPanel tokoPanel = darkPanel(new BorderLayout(4, 4));
        tokoPanel.setBorder(titled("🏪 Toko Jimat"));

        DefaultListModel<Jimat> modelToko = new DefaultListModel<>();
        JList<Jimat> listToko = new JList<>(modelToko);
        listToko.setCellRenderer(new PanelInventarisJimat());

        // load katalog jimat
        if (gameManager != null) {
            for (Jimat j : gameManager.getSupplier().getKatalogJimat()) {
                modelToko.addElement(j);
            }
        }

        JButton btnBeli = styledButton("Beli", ACCENT2, BG_CARD, 11);
        btnBeli.addActionListener(e -> {
            Jimat sel = listToko.getSelectedValue();
            if (sel == null) { showMsg("Pilih jimat dulu."); return; }
            boolean ok = gameManager.getSupplier().beliJimat(
                gameManager.getRestaurant(), sel);
            if (ok) {
                modelToko.removeElement(sel);
                refreshAll();
                appendLog("Jimat " + sel.getName() + " dibeli!");
            } else {
                showMsg("Uang tidak cukup!");
            }
        });

        JPanel tokoBtns = darkPanel(new FlowLayout(FlowLayout.RIGHT));
        tokoBtns.add(btnBeli);
        tokoPanel.add(new JScrollPane(listToko), BorderLayout.CENTER);
        tokoPanel.add(tokoBtns, BorderLayout.SOUTH);

        p.add(tokoPanel, BorderLayout.SOUTH);

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
        return new PanelMakananMenu(gameManager);
    }

    // ── PANEL: Status ──────────────────────────────────────────────────────
    private JPanel buildStatusPanel() {
        JPanel p = new PanelRekapanHari();


        return p;
    }

    // Build Restoan Menu
    private JPanel buildRestoranPanel() {
        JPanel p = new PanelRestoranMenu(gameManager);
        
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
        enum State { MASUK, KE_KASIR, KE_MEJA, DUDUK, KELUAR }
        enum Hadap { KIRI, KANAN, ATAS, BAWAH }

        float x, y;
        int size = 32;
        int index;
        State state;
        Hadap hadap = Hadap.KIRI; // default hadap kiri (masuk dari kanan)

        float targetX, targetY;
        float speed = 1.5f;
        int dudukTimer = 0;
        int mejaTujuan = -1;

        Npc(float startX, float startY, int index) {
            this.x = startX;
            this.y = startY;
            this.index = index;
            this.state = State.MASUK;
            this.hadap = Hadap.KIRI; // masuk dari kanan, hadap kiri
        }

        boolean gerakKe(float tx, float ty) {
            float dx = tx - x;
            float dy = ty - y;
            float dist = (float) Math.sqrt(dx * dx + dy * dy);

            // update arah hadap berdasarkan gerakan
            if (Math.abs(dx) > Math.abs(dy)) {
                hadap = dx > 0 ? Hadap.KANAN : Hadap.KIRI;
            } else {
                hadap = dy > 0 ? Hadap.BAWAH : Hadap.ATAS;
            }

            if (dist < speed + 1) {
                x = tx; y = ty;
                return true;
            }
            if (Math.abs(dx) > Math.abs(dy)) {
                x += dx > 0 ? speed : -speed;
            } else {
                y += dy > 0 ? speed : -speed;
            }
            return false;
        }
    }

    class NpcPanel extends JPanel {
    private final List<Npc> npcs = new ArrayList<>();
    private final boolean[] mejaTerisi; // track meja yang sudah ditempati
    private Timer animTimer;

    // posisi kasir (tengah bawah)
    private int kasirX() { return getWidth() / 2 - 60; }
    private int kasirY() { return getHeight() - 70; }

    // posisi meja — harus sama dengan yang digambar
    private int[][] getMejaPosisi() {
        return new int[][]{{80,80},{200,80},{320,80},{80,200},{200,200},{320,200}};
    }

    NpcPanel() {
        mejaTerisi = new boolean[6]; // 6 meja
        animTimer = new Timer(33, e -> {
            updateNpcs();
            repaint();
        });
        animTimer.start();
    }

    void spawnNpc(int index) {
        // spawn dari kanan frame
        float startX = getWidth() + 10;
        float startY = getHeight() / 2f;
        npcs.add(new Npc(startX, startY, index));
    }

    void setNpcCount(int count) {
        npcs.clear();
        // spawn terus menerus dengan jeda random
        Timer spawnTimer = new Timer(0, null);
        spawnTimer.addActionListener(e -> {
            spawnNpc(new Random().nextInt(2)); // NPC1 atau NPC2 random
            int jedaBerikutnya = 2000 + new Random().nextInt(4000); // jeda 2-6 detik
            spawnTimer.setDelay(jedaBerikutnya);
        });
        spawnTimer.setRepeats(true);
        spawnTimer.start();
    }

    private void updateNpcs() {
        List<Npc> selesai = new ArrayList<>();
        int[][] mejaPosisi = getMejaPosisi();

        for (Npc npc : npcs) {
            switch (npc.state) {

                case MASUK -> {
                    // jalan masuk dari kanan ke tengah layar
                    float targetMasukX = getWidth() - 150;
                    float targetMasukY = getHeight() / 2f;
                    if (npc.gerakKe(targetMasukX, targetMasukY)) {
                        npc.state = Npc.State.KE_KASIR;
                    }
                }

                case KE_KASIR -> {
                    // jalan ke kasir
                    float kx = kasirX() + 60;
                    float ky = kasirY() - 40;
                    if (npc.gerakKe(kx, ky)) {
                        // sampai kasir, cari meja kosong
                        npc.mejaTujuan = cariMejaKosong();
                        if (npc.mejaTujuan >= 0) {
                            mejaTerisi[npc.mejaTujuan] = true;
                            npc.state = Npc.State.KE_MEJA;
                        } else {
                            // tidak ada meja kosong, langsung keluar
                            npc.state = Npc.State.KELUAR;
                        }
                    }
                }

                case KE_MEJA -> {
                    int[] meja = mejaPosisi[npc.mejaTujuan];
                    float mx = meja[0] + 10;
                    float my = meja[1] - 30; 
                    if (npc.gerakKe(mx, my)) {
                        npc.state = Npc.State.DUDUK;
                        npc.dudukTimer = 150;
                    }
                }

                case DUDUK -> {
                    npc.dudukTimer--;
                    if (npc.dudukTimer <= 0) {
                        // selesai makan, bebaskan meja
                        if (npc.mejaTujuan >= 0) {
                            mejaTerisi[npc.mejaTujuan] = false;
                        }
                        npc.state = Npc.State.KELUAR;
                    }
                }

                case KELUAR -> {
                    // jalan keluar ke kanan
                    if (npc.gerakKe(getWidth() + 50, npc.y)) {
                        selesai.add(npc); // hapus dari list
                    }
                }
            }
        }
        npcs.removeAll(selesai);
    }

    private int cariMejaKosong() {
        for (int i = 0; i < mejaTerisi.length; i++) {
            if (!mejaTerisi[i]) return i;
        }
        return -1; // semua penuh
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Lantai
        java.net.URL lantaiUrl = getClass().getResource("/view/Aset/Lantai.png");
        if (lantaiUrl != null) {
            Image lantaiImg = new ImageIcon(lantaiUrl).getImage();
            int tileSize = 40;
            for (int gx = 0; gx < getWidth(); gx += tileSize)
                for (int gy = 0; gy < getHeight(); gy += tileSize)
                    g2.drawImage(lantaiImg, gx, gy, tileSize, tileSize, null);
        }

        // Load semua gambar
        Image mejaImg   = loadImg("/view/Aset/Meja.png");
        Image kursiImg  = loadImg("/view/Aset/Kursi.png");
        Image kasirImg  = loadImg("/view/Aset/Kasir.png");
        Image npc1      = loadImg("/view/Aset/NPC1.png");
        Image npc2      = loadImg("/view/Aset/NPC2.png");
        Image npc1Kiri   = loadImg("/view/Aset/NPC1.png");
        Image npc2Kiri   = loadImg("/view/Aset/NPC2.png");
        Image npc1Duduk = loadImg("/view/Aset/NPC1_duduk.png");
        Image npc2Duduk = loadImg("/view/Aset/NPC2_duduk.png");

        // Meja + Kursi
        int[][] mejaPosisi = getMejaPosisi();
        for (int[] t : mejaPosisi) {
            if (mejaImg != null)
                g2.drawImage(mejaImg, t[0], t[1], 60, 40, null);
           if (kursiImg != null)
                g2.drawImage(kursiImg, t[0] + 10, t[1] - 30, 30, 30, null); // kursi di atas meja
        }

        // Kasir
        if (kasirImg != null)
            g2.drawImage(kasirImg, kasirX(), kasirY(), 120, 70, null);

        // NPC

    for (Npc npc : npcs) {
        Image img;
        if (npc.state == Npc.State.DUDUK) {
            img = npc.index == 0 ? npc1Duduk : npc2Duduk;
            g2.drawImage(img, (int)npc.x, (int)npc.y, npc.size, npc.size, null);
        }
        else {
            img = npc.index == 0 ? npc1Kiri : npc2Kiri;
            if (npc.hadap == Npc.Hadap.KANAN) {
                g2.drawImage(img,
                    (int)npc.x + npc.size, (int)npc.y,
                    -npc.size, npc.size,
                    null);
            } else {
                g2.drawImage(img, (int)npc.x, (int)npc.y, npc.size, npc.size, null);
            }
        }
    }

        // Badge
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(8, 8, 160, 28, 10, 10);
        g2.setColor(ACCENT);
        g2.setFont(new Font("SansSerif", Font.BOLD, 12));
        g2.drawString("👥 Pelanggan: " + npcs.size(), 16, 27);

        g2.dispose();
    }

    private Image loadImg(String path) {
        java.net.URL url = getClass().getResource(path);
        return url != null ? new ImageIcon(url).getImage() : null;
    }
}
    // ══ GAME LIFECYCLE ═════════════════════════════════════════════════════
    private void startGame() {
        gameManager = new GameManager();
        cardLayout.show(mainPanel, KARTU_PERMAINAN);
        npcPanel.setNpcCount(5);
        refreshAll();
    }
    // ══ REFRESH ════════════════════════════════════════════════════════════
    public void refreshAll() {
        if (gameManager == null) return;
        Restaurant resto = gameManager.getRestaurant();

        // Top bar
        lblMoney.setText("💰 Rp " + String.format("%.0f",resto.getMoney()));
        lblKapasitas.setText("🏠 Kapasitas: "+ String.format("%d", resto.getKapasitas()) );
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
