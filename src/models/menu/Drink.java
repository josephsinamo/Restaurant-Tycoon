
package models.menu;

import java.util.Set;
import models.RawMaterial;

public class Drink extends Menu {
    private JenisDrink jenis;

    public Drink(String name,Set <RawMaterial> daftarBahan, JenisDrink jenis) {
        super(name, daftarBahan);
        this.jenis = jenis;
    }
}
