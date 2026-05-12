
package core;

import entities.Customer;
import java.util.*;
import models.menu.Menu;


public class Restaurant {
    private int kapasitasRestoran; //banyaknya pelanggan yang bisa di terima dalam satu waktu
    private double money;
    private HashMap <Menu, Double> daftarMenu;
    

    public Menu [] lihatDaftarMenu(){
        Menu[] daftarMenus = daftarMenu.keySet().toArray(new Menu[0]);
        return daftarMenus;
    }

    public void upgradeKapasitas(int t){
        kapasitasRestoran += t;
    }

    public void addMenu(Menu menu, double harga){
        if (!daftarMenu.containsKey(menu)){
            daftarMenu.put(menu, harga);
        }else{
            System.out.println("Menu sudah ada di daftar");
        }
    }

    public void setHarga(Menu menu, double harga){
        if (daftarMenu.containsKey(menu)){
            daftarMenu.put(menu, harga);
        }else{
            System.out.println("Menu tidak tersedia di daftar menu");
        }
    }

    public void racikMenu(Menu menu){
        // masih proges selesai dapur baru bisa
    }

    public void beli(){
        // masih  dipikirkan
        // nunggu menu 
    }

    public void layaniPelanggan(Customer pelanggan){
        double totalBelanja = 0;
        pelanggan.buatPesanan(lihatDaftarMenu());
        // masih harus di integrasikan dengan dapur
        
        getPayment(pelanggan, money);
    }

    private void getPayment(Customer pelanggan, double money){
        money += pelanggan.bayarPesanan(money);
    }
}
