
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
    private final List<Jimat> katalogJimat = new ArrayList<>();


    public HashMap<RawMaterial, Double> getDaganganBahanBaku() {
        return daganganBahanBaku;
    }

    public List<RawMaterial> getBahanBaku(){
        List<RawMaterial> list = new ArrayList<>(daganganBahanBaku.keySet());
        return list;
    }


    public void setHargaBahanBaku(RawMaterial bahan, double hargaBeli) {
        if (bahan == null) {
            throw new IllegalArgumentException("bahan tidak boleh null");
        }
        if (hargaBeli < 0) {
            throw new IllegalArgumentException("harga tidak boleh negatif");
        }
        daganganBahanBaku.put(bahan, hargaBeli);
    }

    public Double getHargaBahanBaku(RawMaterial bahan) {
        return daganganBahanBaku.get(bahan);
    }

    // init katalog jimat
    public void initKatalogJimat() {
        katalogJimat.clear();
        katalogJimat.add(new models.jimat.Charming());
        katalogJimat.add(new models.jimat.Charming());
        katalogJimat.add(new models.jimat.Cleaner());
        katalogJimat.add(new models.jimat.Cleaner());
        katalogJimat.add(new models.jimat.Security());
        katalogJimat.add(new models.jimat.Security());
    }

    public List<Jimat> getKatalogJimat() { return katalogJimat; }

    public boolean beliJimat(Restaurant resto, Jimat jimat) {
        if (!katalogJimat.contains(jimat)) return false;
        if (!resto.kurangiUang(jimat.getHarga())) {
            System.out.println("Uang tidak cukup!");
            return false;
        }
        resto.tambahKeInventoriJimat(jimat);
        katalogJimat.remove(jimat);
        System.out.println("Jimat " + jimat.getName() + " dibeli!");
        return true;
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
