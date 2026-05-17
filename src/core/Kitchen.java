
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
    

    public int masak(Menu menu, int qty) {
        int hasil = 0;
        for (int i = 0; i < qty ; i++){
            for (RawMaterial rw : menu.getReceipt().keySet() ){
                if (stokBahanBaku.get(rw) < menu.getReceipt().get(rw)){
                    return hasil;
                }
            } 
            hasil++;
        }
        return hasil; 
    }

    public void buangBahanSisa() {
        stokBahanBaku.clear();
    }

    public void getDisasterTikusAttack(Double rate) {  
        // tunggu GameEvent
    }
}
