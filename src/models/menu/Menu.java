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

    public String getName() { 
        return name; 
    }
    public Set<RawMaterial> getDaftarBahan() { 
        return daftarBahan; 
    }
    public Receipt getReceipt() { 
        return receipt; 
    }
    public void setReceipt(Receipt receipt) { 
        this.receipt = receipt; 
    }
}

