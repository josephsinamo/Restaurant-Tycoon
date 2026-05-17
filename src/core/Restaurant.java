package core;

import entities.Customer;
import java.util.*;
import models.ISupplierItem;
import models.RawMaterial;
import models.jimat.Charming;
import models.jimat.Cleaner;
import models.jimat.Jimat;
import models.jimat.Security;
import models.menu.Menu;

public class Restaurant {

    // ══ State utama ═══════════════════════════════════════════════════════
    private int    kapasitasRestoran = 0;
    private double money = 500000.0;

    // ══ Menu & dapur ══════════════════════════════════════════════════════
    private final Map<String, Menu>             daftarMenu    = new HashMap<>();
    private final HashMap<RawMaterial, Integer> stokBahanBaku = new HashMap<>();
    private final Kitchen                       kitchen;

    // ══ Jimat — slot aktif (maks. satu per tipe) ══════════════════════════
    private Charming jimatMenarik;
    private Cleaner  jimatKebersihan;
    private Security jimatKeamanan;

    /** Semua jimat yang dimiliki pemain (termasuk yang belum dipasang). */
    private final List<Jimat> inventarisJimat = new ArrayList<>();

    // ══ Tracking data harian (di-reset tiap nextDay) ══════════════════════
    private double       totalPenjualanHariIni    = 0;
    private double       totalModalHariIni        = 0;
    private int          jumlahPengunjungHariIni  = 0;
    private int          jumlahItemTerjualHariIni = 0;

    /** Setiap elemen: { namaMenu(String), qty(int), hargaSatuan(double), subtotal(double) } */
    private final List<Object[]> daftarTransaksiHariIni = new ArrayList<>();

    // ══ Constructor ═══════════════════════════════════════════════════════
    public Restaurant() {
        this.kitchen = new Kitchen(stokBahanBaku);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  GETTER / SETTER — state utama
    // ══════════════════════════════════════════════════════════════════════

    public double getMoney()     { return money; }
    public int    getKapasitas() { return kapasitasRestoran; }
    public Kitchen getKitchen()  { return kitchen; }

    public void setMoney(double money) {
        if (money >= 0) this.money = money;
    }

    public void setKapasitas(int kapasitas) {
        if (kapasitas >= 0) this.kapasitasRestoran = kapasitas;
    }

    public void upgradeKapasitas(int tambahan) {
        if (tambahan > 0) kapasitasRestoran += tambahan;
    }

    /** Kurangi uang; return false jika saldo tidak cukup. */
    /**
     * PERBAIKAN BUG #5 — ubah dari package-private ke public.
     * Sebelumnya hanya bisa diakses dalam package 'core',
     * sehingga Supplier tidak bisa memanggil method ini untuk mengurangi uang.
     */
    public boolean kurangiUang(double jumlah) {
        if (jumlah < 0 || money < jumlah) return false;
        money -= jumlah;
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BAHAN BAKU
    // ══════════════════════════════════════════════════════════════════════

    public Map<RawMaterial, Integer> getStok() {
        return Collections.unmodifiableMap(stokBahanBaku);
    }

    public Map<String, Integer> getStokBahanBaku() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<RawMaterial, Integer> e : stokBahanBaku.entrySet()) {
            result.put(e.getKey().getName(), e.getValue());
        }
        return result;
    }

    public void setStokBahanBaku(Map<String, Integer> stokBaru) {
        Map<String, RawMaterial> namaKeObjek = new HashMap<>();
        for (RawMaterial rm : stokBahanBaku.keySet()) {
            namaKeObjek.put(rm.getName(), rm);
        }
        stokBahanBaku.clear();
        for (Map.Entry<String, Integer> entry : stokBaru.entrySet()) {
            RawMaterial rm = namaKeObjek.getOrDefault(
                    entry.getKey(), new RawMaterial(entry.getKey()));
            stokBahanBaku.put(rm, entry.getValue());
        }
    }

    public void tambahBahanBaku(RawMaterial bahan, int jumlah) {
        if (jumlah < 1) return;
        stokBahanBaku.merge(bahan, jumlah, Integer::sum);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  MENU
    // ══════════════════════════════════════════════════════════════════════

    public Menu[] lihatDaftarMenu() {
        return daftarMenu.values().toArray(new Menu[0]);
    }

    public Map<String, Menu> getDaftarMenuMap() {
        return Collections.unmodifiableMap(daftarMenu);
    }

    public void addMenu(Menu menu, double harga) {
        if (menu == null) return;
        if (!daftarMenu.containsKey(menu.getName())) {
            menu.setHarga(harga);
            daftarMenu.put(menu.getName(), menu);
        }
    }

    public void setHarga(Menu menu, double harga) {
        if (menu != null && daftarMenu.containsKey(menu.getName())) {
            menu.setHarga(harga);
            daftarMenu.put(menu.getName(), menu);
        }
    }

    public void racikMenu(Menu menu, RawMaterial rw, Integer qty) {
        if (menu == null || !daftarMenu.containsKey(menu.getName())) return;
        daftarMenu.get(menu.getName()).setReceipt(rw, qty);
    }

    public Set<RawMaterial> daftarResep(Menu menu) {
        return menu.getDaftarBahan();
    }

    public boolean upgradeMenu(Menu menu, double biayaUpgrade) {
        if (!daftarMenu.containsKey(menu.getName())) return false;
        if (!kurangiUang(biayaUpgrade)) {
            System.out.println("Uang tidak cukup untuk upgrade!");
            return false;
        }
        double hargaBaru = menu.getPrice() * 1.1;
        menu.setHarga(hargaBaru);
        System.out.println("Menu " + menu.getName() +
            " di-upgrade! Harga baru: Rp " + String.format("%.0f", hargaBaru));
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  JIMAT — inventaris
    // ══════════════════════════════════════════════════════════════════════

    public List<Jimat> getDaftarJimat() {
        return Collections.unmodifiableList(inventarisJimat);
    }

    public void tambahKeInventoriJimat(Jimat jimat) {
        if (jimat != null) inventarisJimat.add(jimat);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  JIMAT — slot aktif
    // ══════════════════════════════════════════════════════════════════════

    public Charming getJimatCharming()  { return jimatMenarik; }
    public Cleaner  getJimatCleaner()   { return jimatKebersihan; }
    public Security getJimatSecurity()  { return jimatKeamanan; }

    public double getPoinJimatMenarik()    { return jimatMenarik    == null ? 0 : jimatMenarik.getPower(); }
    public double getPoinJimatKebersihan() { return jimatKebersihan == null ? 0 : jimatKebersihan.getPower(); }
    public double getPoinJimatKeamanan()   { return jimatKeamanan   == null ? 0 : jimatKeamanan.getPower(); }

    public boolean pakaiJimatDariInventori(Jimat jimat) {
        if (jimat == null) return false;

        if (jimat instanceof Charming c) {
            jimatMenarik = c;
        } else if (jimat instanceof Cleaner cl) {
            jimatKebersihan = cl;
        } else if (jimat instanceof Security s) {
            jimatKeamanan = s;
        } else {
            return false;
        }
        inventarisJimat.remove(jimat);
        return true;
    }

    public void equipJimatSlot(Jimat jimat) {
        if (jimat == null || !inventarisJimat.contains(jimat)) return;
        pakaiJimatDariInventori(jimat);
    }

    public void jualJimat(Jimat jimat) {
        if (jimat == null) return;

        if (jimat instanceof Charming  && jimatMenarik    == jimat) jimatMenarik    = null;
        if (jimat instanceof Cleaner   && jimatKebersihan == jimat) jimatKebersihan = null;
        if (jimat instanceof Security  && jimatKeamanan   == jimat) jimatKeamanan   = null;

        inventarisJimat.remove(jimat);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  PEMBELIAN DARI SUPPLIER
    // ══════════════════════════════════════════════════════════════════════

    public boolean beli(Supplier supplier, ISupplierItem item) {
        return beli(supplier, item, 1);
    }

    public boolean beli(Supplier supplier, ISupplierItem item, int jumlah) {
        if (supplier == null || item == null) return false;
        return supplier.jual(this, item, jumlah);
    }

    // ══════════════════════════════════════════════════════════════════════
    //  PELAYANAN PELANGGAN
    // ══════════════════════════════════════════════════════════════════════

    /**
     * PERBAIKAN layaniPelanggan():
     *
     * Bug lama:
     * 1. totalBelanja bisa 0 karena semua menu di-skip (harga > 20000 + random)
     * 2. Tidak ada logging yang jelas kapan uang bertambah / tidak
     * 3. jumlahItemTerjualHariIni tidak pernah di-increment
     * 4. daftarTransaksiHariIni tidak pernah di-isi
     *
     * Perbaikan:
     * - Semua item yang berhasil dimasak dicatat ke daftarTransaksiHariIni
     * - jumlahItemTerjualHariIni di-increment dengan benar
     * - Log lebih informatif: cetak uang masuk / kabur
     */
    public void layaniPelanggan(Customer pelanggan) {
        double totalBelanja = 0;
        Menu[] daftarMenuArr = lihatDaftarMenu();

        pelanggan.buatPesanan(daftarMenuArr);
        pelanggan.tentukanKabur(this);

        for (Menu menu : new HashMap<>(pelanggan.getPesanan()).keySet()) {
            int qty = pelanggan.getPesanan().getOrDefault(menu, 0);
            if (qty <= 0) continue;

            // Harga mahal → chance dilewati pelanggan
            if (menu.getPrice() > 20000 && Math.random() < 0.4) {
                System.out.println("[INFO] " + menu.getName() + " terlalu mahal, dilewati.");
                continue;
            }

            int terpenuhi = kitchen.masak(menu, qty);

            if (terpenuhi <= 0) {
                pelanggan.gantiMenuAtauPulang(daftarMenuArr, menu);
                continue;
            }
            if (terpenuhi < qty) {
                pelanggan.gantiMenuAtauPulang(daftarMenuArr, menu);
            }

            double hargaSatuan = daftarMenu.get(menu.getName()).getPrice();
            double subtotal    = hargaSatuan * terpenuhi;

            if (jimatMenarik != null) {
                double tips = jimatMenarik.hitungTips(subtotal);
                if (tips > 0) {
                    subtotal += tips;
                    System.out.println("[JIMAT] Tips: Rp " + String.format("%.0f", tips));
                }
            }

            totalBelanja += subtotal;

            // Catat transaksi & item terjual — sebelumnya tidak pernah diisi!
            daftarTransaksiHariIni.add(new Object[]{
                menu.getName(), terpenuhi, hargaSatuan, subtotal
            });
            jumlahItemTerjualHariIni += terpenuhi;
        }

        // ── Pembayaran ─────────────────────────────────────────────────
        if (pelanggan.isKabur()) {
            System.out.println("[KABUR] Pelanggan kabur! Rugi Rp "
                + String.format("%.0f", totalBelanja));
        } else {
            if (totalBelanja > 0) {
                money += totalBelanja;
                totalPenjualanHariIni += totalBelanja;
                System.out.println("[BAYAR] +Rp " + String.format("%.0f", totalBelanja)
                    + " | Saldo: Rp " + String.format("%.0f", money));
            }
        }
        jumlahPengunjungHariIni += pelanggan.getJumlahPelanggan();
    }

    public void akhirHari() {
        kitchen.buangBahanSisa();
        System.out.println("[INFO] Bahan sisa hari ini dibuang.");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  TRACKING DATA HARIAN
    // ══════════════════════════════════════════════════════════════════════

    public void resetDataHarian() {
        totalPenjualanHariIni    = 0;
        totalModalHariIni        = 0;
        jumlahPengunjungHariIni  = 0;
        jumlahItemTerjualHariIni = 0;
        daftarTransaksiHariIni.clear();
    }

    public double getTotalPenjualanHariIni()    { return totalPenjualanHariIni; }
    public double getKeuntunganHariIni()        { return totalPenjualanHariIni - totalModalHariIni; }
    public int    getJumlahPengunjungHariIni()  { return jumlahPengunjungHariIni; }
    public int    getJumlahItemTerjualHariIni() { return jumlahItemTerjualHariIni; }

    public List<Object[]> getDaftarTransaksiHariIni() {
        return Collections.unmodifiableList(daftarTransaksiHariIni);
    }

    public void tambahModal(double modal) {
        if (modal > 0) totalModalHariIni += modal;
    }
}