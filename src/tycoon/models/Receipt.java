
package models;

import java.util.HashMap;

import tycoon.models.RawMaterial;

public class Receipt {

    private HashMap <RawMaterial, Integer> resep = new HashMap <> ();

    public Receipt(HashMap<RawMaterial, Integer> resep) {
        this.resep = resep;
    }

    public void setReceipt(HashMap<RawMaterial, Integer> resep) {
        this.resep = resep;
    }

    public HashMap<RawMaterial, Integer> getReceipt() {
        return resep;
    }
}
