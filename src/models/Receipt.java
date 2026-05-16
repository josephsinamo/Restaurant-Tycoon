
package models;
import java.util.*;

public class Receipt {

    private HashMap <RawMaterial, Integer> resep = new HashMap <> ();

    public Receipt(Set <RawMaterial> daftarBahan) {
        for (RawMaterial rm : daftarBahan){
            resep.put(rm, 0);
        }
    }

    public void setReceipt(RawMaterial rw , Integer qty) {
        resep.put(rw, qty);
    }

    public HashMap <RawMaterial, Integer> getResep(){
        return resep;
    } 
    public HashMap<RawMaterial, Integer> getReceipt() {
        return resep;
    }
}
