package models;

import core.Restaurant;

/**
 * Item yang bisa dibeli lewat {@link core.Supplier} (harga ada di katalog supplier, bukan di objek).
 * Bahan baku ({@link RawMaterial}), jimat, dll.
 */
public interface ISupplierItem extends IPurchasable {


    default void applyPurchase(Restaurant resto, int jumlah) {
        if (jumlah < 1) {
            return;
        }
        for (int i = 0; i < jumlah; i++) {
            applyEffect(resto);
        }
    }

    default void applyPurchase(Restaurant resto) {
        applyPurchase(resto, 1);
    }
}
