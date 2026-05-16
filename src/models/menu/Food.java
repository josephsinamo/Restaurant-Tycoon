package models.menu;

import java.util.Collections;
import java.util.Set;

import models.RawMaterial;

public class Food extends Menu {
    public Food(String name, Set <RawMaterial> daftarBahan) {
        super(name, daftarBahan);
    }
}
