
package models.menu;

public class Drink extends Menu {
    int price;
    public Drink(String name, int price) {
        super(name);
        this.price = price;
    }
}
