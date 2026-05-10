package tycoon.models.menu;

import tycoon.models.Menu;

public class Snack extends Menu{
    private double price;

    public Snack(String name, double price){
        super(name);
        this.price=price;
    }
}
