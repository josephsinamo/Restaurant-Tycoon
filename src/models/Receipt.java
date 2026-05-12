
package models;
import java.util.*;

public class Receipt {

    private HashMap <RawMaterial, Integer> resep = new HashMap <> ();

    public Receipt(Set <RawMaterial> daftarBahan) {
        for (RawMaterial rm : daftarBahan){
            resep.put(rm, 0);
        }
    }

    public void setReceipt(HashMap<RawMaterial, Integer> resep) {
        this.resep = resep;
    }

    public HashMap<RawMaterial, Integer> getReceipt() {
        return resep;
    }
}
