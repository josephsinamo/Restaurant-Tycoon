package tycoon.models.menu;

import tycoon.models.Menu;

public class Drink extends Menu {
    private double price;

    public Drink(String name,double price){
        super(name);
        this.price=price;
    }
}
