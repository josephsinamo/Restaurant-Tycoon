
package core;

import java.util.HashMap;
import models.menu.Menu;

import models.RawMaterial;

public class Kitchen {
    private HashMap<RawMaterial, Integer> stokBahanBaku = new HashMap<>();

    public Kitchen(HashMap<RawMaterial, Integer> stokBahanBaku) {
        this.stokBahanBaku = stokBahanBaku;
    }

    public void addBahanBaku(RawMaterial bahan, Integer qty) {
        stokBahanBaku.put(bahan, stokBahanBaku.getOrDefault(bahan, 0) + qty);
    }

    public void setResep(Menu menu) {
    }

    public int masak(Menu menu, int qty) {
        return 0; 
    }

    public void buangBahanSisa() {
    }

    public void getDisasterTikusAttack(Double rate) {
    }
}

