package models.menu;

import java.util.*;
import models.RawMaterial;
import models.Receipt;

public abstract class Menu {
    private String name;
    private Set <RawMaterial> daftarBahan;
    private Receipt receipt;

    public Menu(String name, Set <RawMaterial> daftarBahan){
        this.name = name;
        this.daftarBahan = daftarBahan;
    }

    public void setReceipt(Receipt receipt){
        this.receipt = receipt;
    }

    /* Fitur Mendatang
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(nama, menu.nama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama);
     */
}
