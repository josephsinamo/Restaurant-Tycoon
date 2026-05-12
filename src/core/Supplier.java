
package core;

import java.util.HashMap;
import models.ISupplierItem;

/**
 * Toko / katalog: harga per objek barang di {@link #getDaftarHarga()}, bukan di dalam {@link models.RawMaterial} atau {@link models.jimat.Jimat}.
 */
public class Supplier {

    /** Kunci biasanya {@link models.RawMaterial} atau {@link models.jimat.Jimat} yang sama dengan yang dipakai saat beli. */
    private final HashMap<Object, Double> daftarHarga = new HashMap<>();

    public HashMap<Object, Double> getDaftarHarga() {
        return daftarHarga;
    }

    public void setHargaBarang(Object barang, double hargaBeli) {
        if (barang == null) {
            throw new IllegalArgumentException("barang tidak boleh null");
        }
        if (hargaBeli < 0) {
            throw new IllegalArgumentException("harga tidak boleh negatif");
        }
        daftarHarga.put(barang, hargaBeli);
    }

    public Double getHargaBarang(Object barang) {
        return daftarHarga.get(barang);
    }

    public boolean jual(Restaurant pembeli, ISupplierItem barang) {
        return jual(pembeli, barang, 1);
    }

    public boolean jual(Restaurant pembeli, ISupplierItem barang, int jumlah) {
        if (pembeli == null || barang == null || jumlah < 1) {
            return false;
        }
        Double hargaSatuan = daftarHarga.get(barang);
        if (hargaSatuan == null) {
            return false;
        }
        double total = hargaSatuan * jumlah;
        if (!pembeli.kurangiUang(total)) {
            return false;
        }
        barang.applyPurchase(pembeli, jumlah);
        return true;
    }
}
