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
    private double power;
    private double harga;

    protected Jimat(String name, double power, double harga) {
        this.name = name;
        this.power = power;
        this.harga = harga;
    }

    public String getName() {
        return name;
    }

    public double  getPower() {
        return power;
    }
    public double getHarga() {
        return harga;
    }

    public double hitungTips(double hargaMenu) {
        return 0;
    }

    public boolean cegahTikus() {
        return false;
    }

    public boolean cegahPembeliKabur() {
        return false;
    }

    public void kurangiKekuatan(int jumlah) {
        if (jumlah < 0) {
            return;
        }
        power = Math.max(0, power - jumlah);
        setelahKurangiKekuatan();
    }

    protected void setelahKurangiKekuatan() {
        // default
    }

    /** Salinan konkret untuk dimasukkan ke inventori (beda referensi per pembelian). */

    // ambigu
    @Override
    public void applyEffect(Restaurant resto) {
        applyPurchase(resto);
    }

    @Override
    public void applyPurchase(Restaurant resto) {
        resto.tambahKeInventoriJimat(this);
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
