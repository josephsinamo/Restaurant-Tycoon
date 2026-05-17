package view;

import controller.GameManager;
import core.Restaurant;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 * Panel Rekapan Harian — menampilkan data REAL dari GameManager.
 *
 * Cara pakai dari Frame:
 *   panelRekapan.refreshFromGame(gameManager);
 *
 * Data yang ditampilkan:
 *   - Total penjualan hari ini
 *   - Keuntungan (penjualan - modal bahan baku)
 *   - Jumlah pengunjung
 *   - Daftar transaksi menu yang terjual
 */
public class PanelRekapanHari extends JPanel {

    // ── Palet warna ────────────────────────────────────────────────────────
    private static final Color BG_DARK      = new Color(26, 30, 42);
    private static final Color BG_CARD      = new Color(41, 33, 45);
    private static final Color BG_ROW_EVEN  = new Color(35, 28, 40);
    private static final Color BG_ROW_ODD   = new Color(45, 36, 52);
    private static final Color ACCENT_GOLD  = new Color(204, 170, 80);
    private static final Color ACCENT_GREEN = new Color(80, 200, 120);
    private static final Color ACCENT_BLUE  = new Color(100, 160, 255);
    private static final Color ACCENT_RED   = new Color(240, 100, 100);
    private static final Color TEXT_WHITE   = Color.WHITE;
    private static final Color TEXT_MUTED   = new Color(180, 175, 190);
    private static final Color BORDER_BROWN = new Color(139, 69, 19);

    // ── Widget fields ──────────────────────────────────────────────────────
    private JLabel lblJudul;
    private JLabel lblTanggal;
    private JLabel lblHariKe;

    // Kartu statistik
    private JLabel lblPendapatanValue;
    private JLabel lblKeuntunganValue;
    private JLabel lblPengunjungValue;
    private JLabel lblItemTerjualValue;

    // Tabel transaksi
    private JTable tabelTransaksi;
    private DefaultTableModel tableModel;

    private JScrollPane jScrollPane1;

    // ── Constructor ────────────────────────────────────────────────────────
    public PanelRekapanHari() {
        buildUI();
        // Tampilkan state kosong saat pertama kali (sebelum hari pertama selesai)
        showEmptyState();
    }

    // ══ Build UI ═══════════════════════════════════════════════════════════
    private void buildUI() {
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        setLayout(new BorderLayout(0, 8));

        add(buildHeader(),   BorderLayout.PAGE_START);
        add(buildStatCards(), BorderLayout.NORTH);
        add(buildTableSection(), BorderLayout.CENTER);
    }

    // ── Header: judul + tanggal + hari ke- ────────────────────────────────
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.setBackground(BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(2, 0, 4, 0));

        lblJudul = new JLabel("REKAPAN HARIAN", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Showcard Gothic", Font.BOLD, 18));
        lblJudul.setForeground(TEXT_WHITE);

        JPanel sub = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        sub.setBackground(BG_DARK);

        lblTanggal = new JLabel(getTanggalHariIni());
        lblTanggal.setFont(new Font("Arial", Font.PLAIN, 12));
        lblTanggal.setForeground(TEXT_MUTED);

        lblHariKe = new JLabel("Hari ke-1");
        lblHariKe.setFont(new Font("Arial", Font.BOLD, 12));
        lblHariKe.setForeground(ACCENT_GOLD);

        sub.add(lblTanggal);
        sub.add(new JLabel("|") {{ setForeground(new Color(80, 70, 95)); }});
        sub.add(lblHariKe);

        p.add(lblJudul, BorderLayout.CENTER);
        p.add(sub, BorderLayout.SOUTH);
        return p;
    }

    // ── Stat cards: 4 kartu ringkasan ─────────────────────────────────────
    private JPanel buildStatCards() {
        JPanel p = new JPanel(new GridLayout(1, 4, 8, 0));
        p.setBackground(BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        // Kartu 1: Total Penjualan
        lblPendapatanValue = new JLabel("Rp 0", SwingConstants.CENTER);
        p.add(buildStatCard("💰 Total Penjualan", lblPendapatanValue, ACCENT_GOLD));

        // Kartu 2: Keuntungan
        lblKeuntunganValue = new JLabel("Rp 0", SwingConstants.CENTER);
        p.add(buildStatCard("📈 Keuntungan", lblKeuntunganValue, ACCENT_GREEN));

        // Kartu 3: Pengunjung
        lblPengunjungValue = new JLabel("0 orang", SwingConstants.CENTER);
        p.add(buildStatCard("👥 Pengunjung", lblPengunjungValue, ACCENT_BLUE));

        // Kartu 4: Item Terjual
        lblItemTerjualValue = new JLabel("0 item", SwingConstants.CENTER);
        p.add(buildStatCard("🍽️ Item Terjual", lblItemTerjualValue, new Color(200, 140, 255)));

        return p;
    }

    private JPanel buildStatCard(String title, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, accentColor),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Arial", Font.PLAIN, 11));
        titleLbl.setForeground(TEXT_MUTED);

        valueLabel.setFont(new Font("Arial", Font.BOLD, 18));
        valueLabel.setForeground(accentColor);

        card.add(titleLbl,   BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ── Tabel transaksi ───────────────────────────────────────────────────
    private JPanel buildTableSection() {
        JPanel p = new JPanel(new BorderLayout(0, 6));
        p.setBackground(BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        JLabel subJudul = new JLabel("Daftar Transaksi");
        subJudul.setFont(new Font("Arial", Font.BOLD, 13));
        subJudul.setForeground(ACCENT_GOLD);
        subJudul.setBorder(BorderFactory.createEmptyBorder(0, 2, 4, 0));
        p.add(subJudul, BorderLayout.PAGE_START);

        // Model tabel
        String[] columns = {"No", "Nama Menu", "Qty", "Harga Satuan", "Total"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tabelTransaksi = new JTable(tableModel);
        styleTable();

        jScrollPane1 = new JScrollPane(tabelTransaksi);
        jScrollPane1.setBorder(BorderFactory.createLineBorder(BORDER_BROWN, 3));
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getViewport().setBackground(BG_DARK);
        jScrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        p.add(jScrollPane1, BorderLayout.CENTER);
        return p;
    }

    private void styleTable() {
        tabelTransaksi.setBackground(BG_ROW_EVEN);
        tabelTransaksi.setForeground(TEXT_WHITE);
        tabelTransaksi.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelTransaksi.setRowHeight(32);
        tabelTransaksi.setGridColor(new Color(60, 50, 70));
        tabelTransaksi.setSelectionBackground(new Color(80, 60, 100));
        tabelTransaksi.setSelectionForeground(TEXT_WHITE);
        tabelTransaksi.setShowVerticalLines(true);
        tabelTransaksi.setFillsViewportHeight(true);
        tabelTransaksi.setIntercellSpacing(new Dimension(1, 1));
        tabelTransaksi.setAutoCreateRowSorter(true);

        JTableHeader header = tabelTransaksi.getTableHeader();
        header.setBackground(new Color(50, 35, 60));
        header.setForeground(ACCENT_GOLD);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 36));

        // Lebar kolom
        tabelTransaksi.getColumnModel().getColumn(0).setMaxWidth(50);
        tabelTransaksi.getColumnModel().getColumn(0).setPreferredWidth(40);
        tabelTransaksi.getColumnModel().getColumn(2).setMaxWidth(70);
        tabelTransaksi.getColumnModel().getColumn(2).setPreferredWidth(55);
        tabelTransaksi.getColumnModel().getColumn(3).setPreferredWidth(130);
        tabelTransaksi.getColumnModel().getColumn(4).setPreferredWidth(130);

        tabelTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? BG_ROW_EVEN : BG_ROW_ODD);
                    setForeground(TEXT_WHITE);
                }
                setHorizontalAlignment(
                    (col == 0 || col == 2) ? SwingConstants.CENTER :
                    (col == 3 || col == 4) ? SwingConstants.RIGHT  :
                    SwingConstants.LEFT);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    // ══ PUBLIC API ═════════════════════════════════════════════════════════

    /**
     * Dipanggil dari Frame.refreshAll() setiap kali state game berubah.
     * Mengambil data langsung dari GameManager tanpa dummy data.
     *
     * @param gm GameManager yang sedang aktif
     */
    public void refreshFromGame(GameManager gm) {
        if (gm == null) { showEmptyState(); return; }

        Restaurant resto = gm.getRestaurant();

        // ── Update label hari ke- ─────────────────────────────────────
        lblHariKe.setText("Hari ke-" + gm.getCurrentDay());

        // ── Ambil data statistik dari Restaurant ──────────────────────
        double totalPenjualan = resto.getTotalPenjualanHariIni();
        double keuntungan     = resto.getKeuntunganHariIni();
        int    pengunjung     = resto.getJumlahPengunjungHariIni();
        int    itemTerjual    = resto.getJumlahItemTerjualHariIni();

        // ── Update kartu statistik ────────────────────────────────────
        lblPendapatanValue.setText(formatRupiah(totalPenjualan));
        lblKeuntunganValue.setText(formatRupiah(keuntungan));
        lblKeuntunganValue.setForeground(keuntungan >= 0 ? ACCENT_GREEN : ACCENT_RED);
        lblPengunjungValue.setText(pengunjung + " orang");
        lblItemTerjualValue.setText(itemTerjual + " item");

        // ── Ambil daftar transaksi dari Restaurant ────────────────────
        // Restaurant.getDaftarTransaksiHariIni() mengembalikan List<Object[]>
        // di mana tiap Object[] berisi: {namaMenu, qty, hargaSatuan, totalHarga}
        List<Object[]> transaksi = resto.getDaftarTransaksiHariIni();

        tableModel.setRowCount(0); // bersihkan tabel lama
        if (transaksi == null || transaksi.isEmpty()) {
            // Tampilkan baris placeholder jika belum ada transaksi
            tableModel.addRow(new Object[]{
                "—", "Belum ada transaksi hari ini", "—", "—", "—"
            });
        } else {
            int no = 1;
            for (Object[] row : transaksi) {
                // row[0]=namaMenu, row[1]=qty, row[2]=hargaSatuan, row[3]=totalHarga
                String namaMenu    = (String) row[0];
                int    qty         = (int)    row[1];
                double hargaSatuan = (double) row[2];
                double totalHarga  = (double) row[3];

                tableModel.addRow(new Object[]{
                    no++,
                    namaMenu,
                    qty,
                    formatRupiah(hargaSatuan),
                    formatRupiah(totalHarga)
                });
            }
        }
    }

    /**
     * Tampilkan state kosong (sebelum hari pertama selesai).
     */
    private void showEmptyState() {
        lblHariKe.setText("Belum mulai");
        lblPendapatanValue.setText("Rp 0");
        lblKeuntunganValue.setText("Rp 0");
        lblPengunjungValue.setText("0 orang");
        lblItemTerjualValue.setText("0 item");
        tableModel.setRowCount(0);
        tableModel.addRow(new Object[]{"—", "-", "—", "—", "—"});
    }

    // ══ HELPERS ════════════════════════════════════════════════════════════

    private String formatRupiah(double amount) {
        return "Rp " + String.format("%,.0f", amount);
    }

    private String getTanggalHariIni() {
        java.time.LocalDate today = java.time.LocalDate.now();
        java.time.format.DateTimeFormatter fmt =
            java.time.format.DateTimeFormatter.ofPattern(
                "EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"));
        return today.format(fmt);
    }
}
