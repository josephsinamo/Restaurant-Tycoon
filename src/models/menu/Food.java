
package models.menu;

import java.util.Collections;

public class Food extends Menu {
    int price;
    public Food(String name, int price) {
        super(name, Collections.emptySet());
        this.price = price;
    }
    
}
