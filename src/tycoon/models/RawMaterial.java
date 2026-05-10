
package tycoon.models;

import core.Restoran;

public class RawMaterial implements IPurchasable {
    
    private String name;
    
    public RawMaterial(String name) {
       this.name=name;
    }

    @Override
    public void applyEffect(Restoran resto) {
        System.out.println(name + " berhasil dibeli dan masuk stok.");
    }
    
}
