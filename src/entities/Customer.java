/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entities;

import java.util.*;
import models.menu.Menu;


/**
 *
 * @author WINDOWS
 */
public class Customer {
    
    private int jumlahPelanggan; 
    // ini nanti nampilkan berapa orang yang datang dalam satu waktu
    // pake MATH random minimal 1 orang
    
    private HashMap <Menu, Integer> pesanan ;

    public Customer (){
        jumlahPelanggan = (int)(Math.random()*10) + 1;
        pesanan = new HashMap<>();

    }

    public HashMap <Menu,Integer> getPesanan(){
        return pesanan;
    }

    public void buatPesanan(Menu [] menu){
        
        for (int k = 0; k < jumlahPelanggan; k++) {
            //tiap pelanggan dapat memesan sebatas dari banyaknya menu 
            int banyakMenuYangTersedia = menu.length;
            int jumlahMenuYangDipesan = (int)((Math.random()*banyakMenuYangTersedia) + 1);
    
            for (int i = 0; i < jumlahMenuYangDipesan; i++) {
    
                int menuYangDipesan = (int)(Math.random()*banyakMenuYangTersedia); //hanya bisa memesan menu yang tersedia
                Integer banyaknya = (int)(Math.random()*10)+1; // satu orang hanya bisa memesan maks 10 item tiap menu
    
                pesanan.merge(menu[menuYangDipesan], banyaknya, Integer::sum);
            }
        }
    
    }

    public double  bayarPesanan(double money){
        
        return money;
    }
    
}
