
package models.menu;

import java.util.Set;
import models.RawMaterial;

public class Drink extends Menu {
    private Jenis jenis;

    public Drink(String name,Set <RawMaterial> daftarBahan, Jenis jenis) {
        super(name, daftarBahan);
        this.jenis = jenis;
    }
}

enum Jenis{
    kopi, susu, buah
}
