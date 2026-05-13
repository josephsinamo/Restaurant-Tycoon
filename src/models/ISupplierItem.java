package models;

import core.Restaurant;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

interface IPurchasable {
    void applyEffect(Restaurant resto);
}

public interface ISupplierItem extends IPurchasable {
    default void applyPurchase(Restaurant resto, int jumlah) {
        if (jumlah < 1) return;
        for (int i = 0; i < jumlah; i++) {
            applyEffect(resto);
        }
    }
    default void applyPurchase(Restaurant resto) { 
        applyPurchase(resto, 1); }
}
