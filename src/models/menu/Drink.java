
package models.menu;

import java.util.Collections;

public class Drink extends Menu {
    int price;
    public Drink(String name, int price) {
        super(name, Collections.emptySet());
        this.price = price;
    }

    @Override public int getPrice() { 
        return price; 
    }   
}
