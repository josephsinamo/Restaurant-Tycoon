package models.menu;
import models.Menu;

public class Snack extends Menu{
    private double price;

    public Snack(String name, double price){
        super(name);
        this.price=price;
    }
}
