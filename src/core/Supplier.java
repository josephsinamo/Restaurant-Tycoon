
package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import models.ISupplierItem;
import models.RawMaterial;
import models.jimat.Jimat;


public class Supplier {

    // private final HashMap<Jimat, Double> daganganJimat = new HashMap<>();
    private final HashMap<RawMaterial, Double> daganganBahanBaku = new HashMap<>();

    // public HashMap<Jimat, Double> getDaganganJimat() {
    //     return daganganJimat;
    // }

    public HashMap<RawMaterial, Double> getDaganganBahanBaku() {
        return daganganBahanBaku;
    }

    public List<RawMaterial> getBahanBaku(){
        List<RawMaterial> list = new ArrayList<>(daganganBahanBaku.keySet());
        return list;
    }

    // public List<Jimat> getJimat(){
    //     List<Jimat> list = new ArrayList<>(daganganJimat.keySet());
    //     return list;
    // }




    // biar agak seru nanti jimat di buat seperti gacha
    // public void setHargaJimat(Jimat jimat, double hargaBeli) {
    //     if (jimat == null) {
    //         throw new IllegalArgumentException("jimat tidak boleh null");
    //     }
    //     if (hargaBeli < 0) {
    //         throw new IllegalArgumentException("harga tidak boleh negatif");
    //     }
    //     daganganJimat.put(jimat, hargaBeli);
    // }

    public void setHargaBahanBaku(RawMaterial bahan, double hargaBeli) {
        if (bahan == null) {
            throw new IllegalArgumentException("bahan tidak boleh null");
        }
        if (hargaBeli < 0) {
            throw new IllegalArgumentException("harga tidak boleh negatif");
        }
        daganganBahanBaku.put(bahan, hargaBeli);
    }

    // public Double getHargaJimat(Jimat jimat) {
    //     return daganganJimat.get(jimat);
    // }

    public Double getHargaBahanBaku(RawMaterial bahan) {
        return daganganBahanBaku.get(bahan);
    }

    // coba ke void
    public boolean jual(Restaurant pembeli, ISupplierItem barang) {
        return jual(pembeli, barang, 1);
    }

    public boolean jual(Restaurant pembeli, ISupplierItem barang, int jumlah) {
        if (pembeli == null || barang == null || jumlah < 1) {
            return false;
        }
        Double hargaSatuan = null;
        if (barang instanceof Jimat j) {
            hargaSatuan = (double) 50000;
        } else if (barang instanceof RawMaterial r) {
            hargaSatuan = daganganBahanBaku.get(r);
        }
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
