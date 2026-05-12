
package models.menu;

import java.util.Collections;

public class Snack extends Menu {
    int price;
    public Snack(String name, int price) {
        super(name, Collections.emptySet());
        this.price = price;
    }
    
}
