
package models;

import core.Restoran;

public class RawMaterial implements IPurchasable {
    private String nama;
    
    public RawMaterial(String nama) {
       this.nama=nama;
    }

    @Override
    public void applyEffect(Restoran resto) {
        System.out.println(nama + " berhasil dibeli dan masuk stok.");
    }
    
}
