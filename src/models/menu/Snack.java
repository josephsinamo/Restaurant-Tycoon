package models.menu;

import java.util.Set;
import models.RawMaterial;


public class Snack extends Menu {
    private JenisSnack jenis;

    public Snack(String name,Set <RawMaterial> daftarBahan, JenisSnack jenis ) {
        super(name, daftarBahan);
        this.jenis = jenis;
    }
    
    
}

