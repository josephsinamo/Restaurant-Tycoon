package models.menu;
import models.Resep;

public abstract class Menu {
    private String nama;
    private Resep resep;

    public Menu(String nama){
        this.nama = nama;
    }
    public Menu(String nama, Resep resep){
        this.nama = nama;
        this.resep = resep;
    }

    public void setResep(Resep resep){
        this.resep = resep;
    }
}
