package models.jimat;

import core.Restaurant;
import java.util.Objects;
import models.ISupplierItem;

/**
 * Jimat dasar; harga beli di katalog {@link core.Supplier}.
 * Pembelian menambah salinan baru ke inventori restoran, bukan menambah kekuatan pada objek katalog.
 * Satu slot terpasang per kategori ({@link Restaurant#equipJimatSlot(Jimat)} / {@link Restaurant#pakaiJimatDariInventori(Jimat)}).
 */
public abstract class Jimat implements ISupplierItem {

    private final String name;
    private final int kekuatanPerUnitPembelian;
    private int kekuatanAktif;

    protected Jimat(String name, int kekuatanPerUnitPembelian) {
        this.name = name;
        this.kekuatanPerUnitPembelian = kekuatanPerUnitPembelian;
        this.kekuatanAktif = 0;
    }

    public String getName() {
        return name;
    }

    public int getKekuatanPerUnitPembelian() {
        return kekuatanPerUnitPembelian;
    }

    public int kekuatanEfekPerSatuan() {
        return kekuatanAktif;
    }

    protected void tambahKekuatanDariPembelian(int jumlah) {
        if (jumlah < 1) {
            return;
        }
        kekuatanAktif += kekuatanPerUnitPembelian * jumlah;
    }

    /**
     * Satu unit jimat baru di inventori: kekuatan = satu kali nilai per unit (bukan menumpuk di template supplier).
     */
    protected void initSebagaiItemInventori() {
        kekuatanAktif = kekuatanPerUnitPembelian;
    }

    public void kurangiKekuatan(int jumlah) {
        if (jumlah < 1) {
            return;
        }
        kekuatanAktif = Math.max(0, kekuatanAktif - jumlah);
        setelahKurangiKekuatan();
    }

    protected void setelahKurangiKekuatan() {
        // default
    }

    /** Salinan konkret untuk dimasukkan ke inventori (beda referensi per pembelian). */
    protected abstract Jimat buatInstanceSalinan();

    protected Jimat buatSalinanUntukInventori() {
        Jimat salinan = buatInstanceSalinan();
        salinan.initSebagaiItemInventori();
        return salinan;
    }

    @Override
    public void applyEffect(Restaurant resto) {
        applyPurchase(resto, 1);
    }

    @Override
    public void applyPurchase(Restaurant resto, int jumlah) {
        if (jumlah < 1) {
            return;
        }
        for (int i = 0; i < jumlah; i++) {
            resto.tambahKeInventoriJimat(buatSalinanUntukInventori());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Jimat jimat = (Jimat) o;
        return Objects.equals(name, jimat.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), name);
    }
}
