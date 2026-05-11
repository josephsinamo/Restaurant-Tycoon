
package models;

import core.Restaurant;

public class RawMaterial implements IPurchasable {
    String name;

    public RawMaterial(String name) {
        this.name = name;
    }

    @Override
    public void applyEffect(Restaurant resto) {
        // Implementation for applying effect of raw material
    }
}

