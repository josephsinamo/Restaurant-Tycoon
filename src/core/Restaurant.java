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
    private double money = 50000.0;
    

    // ══ Menu & dapur ══════════════════════════════════════════════════════
    private final Map<String, Menu>           daftarMenu    = new HashMap<>();
    private final HashMap<RawMaterial, Integer> stokBahanBaku = new HashMap<>();
    private final Kitchen                      kitchen;

    // ══ Jimat — slot aktif (maks. satu per tipe) ══════════════════════════
    private Charming jimatMenarik;
    private Cleaner  jimatKebersihan;
    private Security jimatKeamanan;

    /** Semua jimat yang dimiliki pemain (termasuk yang belum dipasang). */
    private final List<Jimat> inventarisJimat = new ArrayList<>();

    // ══ Tracking data harian (di-reset tiap nextDay) ══════════════════════
    private double       totalPenjualanHariIni    = 0;
    private double       totalModalHariIni        = 0;   // belum dihitung otomatis; siap pakai
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

    public double getMoney()          { return money; }
    public int    getKapasitas()      { return kapasitasRestoran; }
    public Kitchen getKitchen()       { return kitchen; }

    /** Dipakai GameSave untuk restore uang dari file. */
    public void setMoney(double money) {
        if (money >= 0) this.money = money;
    }

    /** Dipakai GameSave untuk restore kapasitas dari file. */
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
    public boolean kurangiUang(double jumlah) {
        if (jumlah < 0 || money < jumlah) return false;
        money -= jumlah;
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  BAHAN BAKU
    // ══════════════════════════════════════════════════════════════════════

    /** Dipakai Frame & internal — key berupa objek RawMaterial. */
    public Map<RawMaterial, Integer> getStok() {
        return Collections.unmodifiableMap(stokBahanBaku);
    }

    /**
     * Representasi stok dengan key String (nama bahan).
     * Dipakai GameSave agar bisa ditulis tanpa serialisasi objek penuh.
     */
    public Map<String, Integer> getStokBahanBaku() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<RawMaterial, Integer> e : stokBahanBaku.entrySet()) {
            result.put(e.getKey().getName(), e.getValue());
        }
        return result;
    }

    /**
     * Restore stok dari save file.
     * Cocokkan nama String ke RawMaterial yang sudah ada;
     * jika belum ada, buat entri baru.
     */
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

    // upgrade harga jual menu
    public boolean upgradeMenu(Menu menu, double biayaUpgrade) {
        if (!daftarMenu.containsKey(menu.getName())) return false;
        if (!kurangiUang(biayaUpgrade)) {
            System.out.println("Uang tidak cukup untuk upgrade!");
            return false;
        }
        // upgrade = naikkan harga jual 10%
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

    /**
     * Pasang jimat dari inventaris ke slot aktifnya.
     * Jimat lama di slot yang sama langsung digantikan (tidak kembali ke inventaris).
     *
     * @return true jika berhasil dipasang, false jika tipe tidak dikenal
     */
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
        // Hapus dari inventaris setelah terpasang
        inventarisJimat.remove(jimat);
        return true;
    }

    /**
     * Pasang jimat yang sudah ada di inventaris (versi alternatif — cek dulu contains).
     */
    public void equipJimatSlot(Jimat jimat) {
        if (jimat == null || !inventarisJimat.contains(jimat)) return;
        pakaiJimatDariInventori(jimat);
    }

    /**
     * Jual jimat dari inventaris. Jimat yang terpasang di slot aktif
     * juga dilepas jika referensinya sama.
     */
    public void jualJimat(Jimat jimat) {
        if (jimat == null) return;

        // Lepas dari slot aktif jika terpasang
        if (jimat instanceof Charming  && jimatMenarik    == jimat) jimatMenarik    = null;
        if (jimat instanceof Cleaner   && jimatKebersihan == jimat) jimatKebersihan = null;
        if (jimat instanceof Security  && jimatKeamanan   == jimat) jimatKeamanan   = null;

        // Hapus dari inventaris (baik yang belum maupun yang sudah dipasang)
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

  public LaporanTransaksi layaniPelanggan(Customer pelanggan) {
        double totalBelanja = 0;
        List<String> itemLog = new ArrayList<>();
        Menu[] daftarMenuArr = lihatDaftarMenu();
        pelanggan.buatPesanan(daftarMenuArr);
        pelanggan.tentukanKabur(this);

        for (Menu menu : new HashMap<>(pelanggan.getPesanan()).keySet()) {
            int qty = pelanggan.getPesanan().getOrDefault(menu, 0);

            if (menu.getPrice() > 20000 && Math.random() < 0.4) {
                continue;
            }

            int terpenuhi = kitchen.masak(menu, qty);

            if (terpenuhi < qty) {
                pelanggan.gantiMenuAtauPulang(daftarMenuArr, menu);
            }

            if (terpenuhi <= 0) {
                continue;
            }

            double hargaSatuan = daftarMenu.get(menu.getName()).getPrice();
            double harga = hargaSatuan * terpenuhi;

            if (jimatMenarik != null) {
                double tips = jimatMenarik.hitungTips(harga);
                if (tips > 0) {
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

    /** Dipanggil oleh GameManager.nextDay() sebelum melayani pelanggan baru. */
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

    public List<Object[]> getDaftarTransaksiHariIni() {
        return Collections.unmodifiableList(daftarTransaksiHariIni);
    }

    /** Tambah modal (harga bahan baku terpakai) — dipanggil dari Kitchen jika diintegrasikan. */
    public void tambahModal(double modal) {
        if (modal > 0) totalModalHariIni += modal;
    }
}
