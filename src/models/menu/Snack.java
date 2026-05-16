package models.menu;

import java.util.Set;
import models.RawMaterial;

enum Jenis{
    kentang_goreng, pisang_goreng, kelepon, sawit_rebus 
}

public class Snack extends Menu {
    private Jenis jenis;

    public Snack(String name,Set <RawMaterial> daftarBahan, Jenis jenis ) {
        super(name, daftarBahan);
        this.jenis = jenis;
    }
    
}

