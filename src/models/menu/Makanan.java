
package models.menu;

public class Makanan extends Menu {
    private double harga;

    public Makanan(String nama,double harga){
        super(nama);
        this.harga=harga;
    }
}
