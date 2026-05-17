
package models;

import core.Restaurant;
import java.util.Objects;

public class RawMaterial implements ISupplierItem {

    private final String name;

    public RawMaterial(String name) {
        this.name = name;   
    }

    public String getName() {
        return name;
    }

    @Override
    public void applyEffect(Restaurant resto) {
        applyPurchase(resto, 1);
    }

    @Override
    public void applyPurchase(Restaurant resto, int jumlah) {
        if (jumlah < 1) {
            return;
        }
        resto.tambahBahanBaku(this, jumlah);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawMaterial that = (RawMaterial) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}