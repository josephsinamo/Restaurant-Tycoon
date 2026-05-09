
package models;

import core.Restoran;

public class BahanBaku implements IPurchasable {
    private String nama;
    
    public BahanBaku(String nama) {
       this.nama=nama;
    }

    @Override
    public void applyEffect(Restoran resto) {
        System.out.println(nama + " berhasil dibeli dan masuk stok.");
    }
    
}
