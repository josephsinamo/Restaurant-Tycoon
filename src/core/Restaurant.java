
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
    private int kapasitasRestoran; // banyaknya pelanggan yang bisa di terima dalam satu waktu
    private double money = 50000.0;
    private Map <String,Menu> daftarMenu;

    private final HashMap<RawMaterial, Integer> stokBahanBaku;
    private int maxStorage;
    private final Kitchen kitchen;

    /**
     * Maksimal satu referensi per kategori; poin efek =
     * {@link Jimat#kekuatanEfekPerSatuan()} yang terpasang.
     */
    private Charming jimatMenarik;
    private Cleaner jimatKebersihan;
    private Security jimatKeamanan;

    /**
     * Jimat yang dibeli; hapus/pakai memakai referensi objek yang sama
     * ({@code ==}).
     */
    private final List<Jimat> inventarisJimat = new ArrayList<>();

    public Restaurant() {
        this.daftarMenu = new HashMap<>();
        this.stokBahanBaku = new HashMap<>();
        this.kitchen = new Kitchen(stokBahanBaku);
    }

    public void tambahKeInventoriJimat(Jimat jimat) {
        if (jimat != null) {
            inventarisJimat.add(jimat);
        }
    }

    // method kurang jelas
    public boolean pakaiJimatDariInventori(Jimat jimat) {
        if (jimat == null || !hapusDariInventoriJimatIdentik(jimat)) {
            return false;
        }
        if (jimat instanceof Charming c) {
            if (jimatMenarik != null) {
                inventarisJimat.add(jimatMenarik);
            }
            jimatMenarik = c;
        } else if (jimat instanceof Cleaner cl) {
            if (jimatKebersihan != null) {
                inventarisJimat.add(jimatKebersihan);
            }
            jimatKebersihan = cl;
        } else if (jimat instanceof Security s) {
            if (jimatKeamanan != null) {
                inventarisJimat.add(jimatKeamanan);
            }
            jimatKeamanan = s;
        } else {
            inventarisJimat.add(jimat);
            return false;
        }
        return true;
    }

    private boolean hapusDariInventoriJimatIdentik(Jimat target) {
        for (int i = 0; i < inventarisJimat.size(); i++) {
            if (inventarisJimat.get(i) == target) {
                inventarisJimat.remove(i);
                return true;
            }
        }
        return false;
    }

    public int getPoinJimatMenarik() {
        return jimatMenarik == null ? 0 : jimatMenarik.getPower();
    }

    public int getPoinJimatKebersihan() {
        return jimatKebersihan == null ? 0 : jimatKebersihan.getPower();
    }

    public int getPoinJimatKeamanan() {
        return jimatKeamanan == null ? 0 : jimatKeamanan.getPower();
    }

    /**
     * Pasang jimat pada slot tipenya. Hanya satu referensi per kategori (ganti =
     * referensi baru menggantikan yang lama).
     */
    public void equipJimatSlot(Jimat jimat) {
        if (jimat == null) {
            return;
        }
        if (jimat instanceof Charming c) {
            jimatMenarik = c;
        } else if (jimat instanceof Cleaner cl) {
            jimatKebersihan = cl;
        } else if (jimat instanceof Security s) {
            jimatKeamanan = s;
        }
    }

    // masih perlu di perbaiki
    
    public void jualJimat(Jimat jimat) {
        if (jimat == null) {
            return;
        }
        hapusDariInventoriJimatIdentik(jimat);
        if (jimat instanceof Charming && jimatMenarik == jimat) {
            jimatMenarik = null;
        } else if (jimat instanceof Cleaner && jimatKebersihan == jimat) {
            jimatKebersihan = null;
        } else if (jimat instanceof Security && jimatKeamanan == jimat) {
            jimatKeamanan = null;
        }
    }

    public Menu[] lihatDaftarMenu() {
        Menu[] daftarMenus = daftarMenu.keySet().toArray(new Menu[0]);
        return daftarMenus;
    }

    public void upgradeKapasitas(int t) {
        kapasitasRestoran += t;
    }

    public int getKapasitas(){
        return kapasitasRestoran;
    }


    // bisa di satukan dengan setHarga
    public void addMenu(Menu menu, double harga) {
        if (!daftarMenu.containsKey(menu.getName())) {
            menu.setHarga(harga);
            daftarMenu.put(menu.getName(),menu);
        } else {
            //System.out.println("Menu sudah ada di daftar");
        }
    }
    
    // konsep sama dengan addMenu
    public void setHarga(Menu menu, double harga) {
        if (daftarMenu.containsKey(menu.getName())) {
            menu.setHarga(harga);
            daftarMenu.put(menu.getName(),menu);
        } else {
            //System.out.println("Menu tidak tersedia di daftar menu");
        }
    }

    public void racikMenu(Menu menu,RawMaterial rw, Integer qty) {
        if (!daftarMenu.containsKey(menu.getName())){
            return;
        } else{
            daftarMenu.get(menu.getName()).setReceipt(rw, qty);
        }
    }


    // coba ke void
    public boolean beli(Supplier supplier, ISupplierItem item) {
        return beli(supplier, item, 1);
    }

    public boolean beli(Supplier supplier, ISupplierItem item, int jumlah) {
        if (supplier == null || item == null) {
            return false;
        }
        return supplier.jual(this, item, jumlah);
    }

    public void tambahBahanBaku(RawMaterial bahan, int jumlah) {
        if (jumlah < 1) {
            return;
        }
        stokBahanBaku.merge(bahan, jumlah, Integer::sum);
    }

    boolean kurangiUang(double jumlah) {
        if (jumlah < 0 || money < jumlah) {
            return false;
        }
        money -= jumlah;
        return true;
    }

    public double getMoney() {
        return money;
    }

    public void layaniPelanggan(Customer pelanggan) {
        double totalBelanja = 0;
        pelanggan.buatPesanan(lihatDaftarMenu());
        // masih harus di integrasikan dengan dapur

        for (Menu menu : pelanggan.getPesanan().keySet()){
            int totPesan = kitchen.masak(menu, pelanggan.getPesanan().get(menu));
            totalBelanja += (double)(daftarMenu.get(menu.getName()).getPrice()*totPesan);
        }
        getPayment(pelanggan, money);
    }

    private void getPayment(Customer pelanggan, double money) {
        money += pelanggan.bayarPesanan(money);
    }
}
