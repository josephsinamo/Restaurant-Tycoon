package models.menu;

import models.Receipt;

public abstract class Menu {
    private String name;
    private Receipt receipt;

    public Menu(String name){
        this.name = name;
    }
    public Menu(String name, Receipt receipt){
        this.name = name;
        this.receipt = receipt;
    }

    public void setReceipt(Receipt receipt){
        this.receipt = receipt;
    }
}
