package tycoon.models.menu;

import tycoon.models.Menu;

public class Food extends Menu {
    private double price;

    public Food(String name,double price){
        super(name);
        this.price=price;
    }
}
