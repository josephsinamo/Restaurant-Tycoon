
package models;

import java.util.HashMap;

public class Resep {

    private HashMap<BahanBaku, Integer> resep;

    public Resep(HashMap<BahanBaku, Integer> resep) {
        this.resep = resep;
    }

    public void setResep(HashMap<BahanBaku, Integer> resep) {
        this.resep = resep;
    }

    public HashMap<BahanBaku, Integer> getResep() {
        return resep;
    }
}
