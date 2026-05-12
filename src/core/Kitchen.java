
package core;

import java.util.HashMap;
import models.RawMaterial;
import models.menu.Menu;

public class Kitchen {
    private final HashMap<RawMaterial, Integer> stokBahanBaku;

    public Kitchen(HashMap<RawMaterial, Integer> stokBahanBaku) {
        this.stokBahanBaku = stokBahanBaku;
    }

    public HashMap<RawMaterial, Integer> getStokBahanBaku() {
        return stokBahanBaku;
    }

    public void addBahanBaku(RawMaterial bahan, Integer qty) {
        stokBahanBaku.put(bahan, stokBahanBaku.getOrDefault(bahan, 0) + qty);
    }

    public void setResep(Menu menu) {
        // tunggu menu selesai
    }

    public int masak(Menu menu, int qty) {
        // tunggu resep
        return 0; 
    }

    public void buangBahanSisa() {
        stokBahanBaku.clear();
    }

    public void getDisasterTikusAttack(Double rate) {  
        // tunggu GameEvent
    }
}
