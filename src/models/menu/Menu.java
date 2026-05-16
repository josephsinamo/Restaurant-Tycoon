package models.menu;

import java.util.*;
import models.RawMaterial;
import models.Receipt;

public abstract class Menu {
    private String name;
    private Set <RawMaterial> daftarBahan;
    private Receipt receipt;
    private double price;

    public Menu(String name, Set <RawMaterial> daftarBahan){
        this.name = name;
        this.daftarBahan = daftarBahan;
        receipt = new Receipt(daftarBahan);
        price = 0;
    }

    public String getName() { 
        return name; 
    }

    public double getPrice(){
        return price;
    }

    public void setHarga(double price){
        if (price < 0){

        }else{
            this.price = price;
        }
    }
    public Set<RawMaterial> getDaftarBahan() { 
        return daftarBahan; 
    }
    public HashMap <RawMaterial, Integer> getReceipt() { 
        return receipt.getReceipt(); 
    }
    public void setReceipt(RawMaterial rw, Integer qty) { 
        receipt.setReceipt(rw, qty);
    }


    // simpan dlu
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Jika alamat memori sama, pasti true
        if (o == null || getClass() != o.getClass()) return false; // Jika tipe data beda, false
        
        Menu menu = (Menu) o;
        // Tentukan aturan: Objek dianggap SAMA jika nama dan power-nya sama
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        // Menghasilkan kode unik berbasis nilai atribut nama dan power
        return Objects.hash(name);
    }
}

