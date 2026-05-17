package core;

import entities.Customer;
import entities.LaporanTransaksi;
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
    private int          jumlahPembeliKaburHariIni = 0;
    private int          jumlahGrupPelangganHariIni = 0;
    private boolean      tikusMenyerangHariIni = false;
    private boolean      tikusDicegahJimatHariIni = false;
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

    /** Upgrade kapasitas tempat makan dengan biaya. */
    public boolean upgradeKapasitasRestoran(int tambahan, double biaya) {
        if (tambahan <= 0 || !kurangiUang(biaya)) {
            return false;
        }
        kapasitasRestoran += tambahan;
        return true;
    }

    /** Kurangi uang; return false jika saldo tidak cukup. */
<<<<<<< HEAD
=======
    /**
     * PERBAIKAN BUG #5 — ubah dari package-private ke public.
     * Sebelumnya hanya bisa diakses dalam package 'core',
     * sehingga Supplier tidak bisa memanggil method ini untuk mengurangi uang.
     */
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13
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
            for (RawMaterial rm : menu.getDaftarBahan()) {
                if (menu.getReceipt().getOrDefault(rm, 0) < 1) {
                    menu.setReceipt(rm, 1);
                }
            }
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

<<<<<<< HEAD
  public LaporanTransaksi layaniPelanggan(Customer pelanggan) {
=======
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
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13
        double totalBelanja = 0;
        List<String> itemLog = new ArrayList<>();
        Menu[] daftarMenuArr = lihatDaftarMenu();

        pelanggan.buatPesanan(daftarMenuArr);
        pelanggan.tentukanKabur(this);

        for (Menu menu : new HashMap<>(pelanggan.getPesanan()).keySet()) {
            int qty = pelanggan.getPesanan().getOrDefault(menu, 0);
            if (qty <= 0) continue;

<<<<<<< HEAD
=======
            // Harga mahal → chance dilewati pelanggan
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13
            if (menu.getPrice() > 20000 && Math.random() < 0.4) {
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

<<<<<<< HEAD
            if (terpenuhi <= 0) {
                continue;
            }

            double hargaSatuan = daftarMenu.get(menu.getName()).getPrice();
            double harga = hargaSatuan * terpenuhi;
=======
            double hargaSatuan = daftarMenu.get(menu.getName()).getPrice();
            double subtotal    = hargaSatuan * terpenuhi;
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13

            if (jimatMenarik != null) {
                double tips = jimatMenarik.hitungTips(subtotal);
                if (tips > 0) {
<<<<<<< HEAD
                    harga += tips;
                }
            }

            totalBelanja += harga;
            jumlahItemTerjualHariIni += terpenuhi;
            itemLog.add(menu.getName() + " x" + terpenuhi
                    + " (Rp " + String.format("%.0f", harga) + ")");
            daftarTransaksiHariIni.add(new Object[]{
                    menu.getName(), terpenuhi, hargaSatuan, harga
            });
        }

        if (pelanggan.isKabur()) {
            jumlahPembeliKaburHariIni++;
        } else if (totalBelanja > 0) {
            money += totalBelanja;
            totalPenjualanHariIni += totalBelanja;
=======
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
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13
        }
        jumlahPengunjungHariIni += pelanggan.getJumlahPelanggan();
        jumlahGrupPelangganHariIni++;

        return new LaporanTransaksi(
                pelanggan.getJumlahPelanggan(), itemLog, totalBelanja, pelanggan.isKabur());
    }

    public void catatTikusMenyerang() {
        tikusMenyerangHariIni = true;
    }

    public void catatTikusDicegahJimat() {
        tikusDicegahJimatHariIni = true;
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
        jumlahPembeliKaburHariIni = 0;
        jumlahGrupPelangganHariIni = 0;
        tikusMenyerangHariIni = false;
        tikusDicegahJimatHariIni = false;
        daftarTransaksiHariIni.clear();
    }

<<<<<<< HEAD
    public double       getTotalPenjualanHariIni()    { return totalPenjualanHariIni; }
    public double       getKeuntunganHariIni()        { return totalPenjualanHariIni - totalModalHariIni; }
    public int          getJumlahPengunjungHariIni()  { return jumlahPengunjungHariIni; }
    public int          getJumlahItemTerjualHariIni() { return jumlahItemTerjualHariIni; }
    public int          getJumlahPembeliKaburHariIni() { return jumlahPembeliKaburHariIni; }
    public boolean      isTikusMenyerangHariIni()     { return tikusMenyerangHariIni; }
    public boolean      isTikusDicegahJimatHariIni()  { return tikusDicegahJimatHariIni; }

    /** Jumlah kunjungan/grup pelanggan yang dilayani hari ini. */
    public int getJumlahGrupPelangganHariIni() {
        return jumlahGrupPelangganHariIni;
    }
=======
    public double getTotalPenjualanHariIni()    { return totalPenjualanHariIni; }
    public double getKeuntunganHariIni()        { return totalPenjualanHariIni - totalModalHariIni; }
    public int    getJumlahPengunjungHariIni()  { return jumlahPengunjungHariIni; }
    public int    getJumlahItemTerjualHariIni() { return jumlahItemTerjualHariIni; }
>>>>>>> 5c4b727a1081151ae82c99c08722716c56cfaf13

    public List<Object[]> getDaftarTransaksiHariIni() {
        return Collections.unmodifiableList(daftarTransaksiHariIni);
    }

    public void tambahModal(double modal) {
        if (modal > 0) totalModalHariIni += modal;
    }
}