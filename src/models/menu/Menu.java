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

    /* Fitur Mendatang
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(nama, menu.nama);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nama);
     */
}
