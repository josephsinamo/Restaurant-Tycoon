package entities;

import core.*;
import java.util.*;
import models.menu.Menu;

public class Customer {

    private int jumlahPelanggan;
    private HashMap<Menu, Integer> pesanan;
    private boolean kabur = false;

    public Customer() {
        // 70% perorangan, 30% rombongan 2-5 orang
        if (Math.random() < 0.7) {
            jumlahPelanggan = 1;
        } else {
            jumlahPelanggan = 2 + (int)(Math.random() * 4);
        }
        pesanan = new HashMap<>();
    }

    public int getJumlahPelanggan() { return jumlahPelanggan; }
    public HashMap<Menu, Integer> getPesanan() { return pesanan; }
    public boolean isKabur() { return kabur; }

    public void buatPesanan(Menu[] menu) {
        for (int k = 0; k < jumlahPelanggan; k++) {
            int banyakMenuYangTersedia = menu.length;
            int jumlahMenuYangDipesan = (int)((Math.random() * banyakMenuYangTersedia) + 1);

            for (int i = 0; i < jumlahMenuYangDipesan; i++) {
                int menuYangDipesan = (int)(Math.random() * banyakMenuYangTersedia);
                Integer banyaknya = (int)(Math.random() * 10) + 1;
                pesanan.merge(menu[menuYangDipesan], banyaknya, Integer::sum);
            }
        }
    }

    public void tentukanKabur(Restaurant resto) {
        // cek jimat Security dulu
        if (resto.getJimatSecurity() != null && resto.getJimatSecurity().cegahPembeliKabur()) {
            System.out.println("[JIMAT] Security mencegah pembeli kabur!");
            this.kabur = false;
            return;
        }
        // chance kabur = 20% dikurangi poin keamanan
        double chanceKabur = Math.max(0, 20.0 - resto.getPoinJimatKeamanan());
        this.kabur = Math.random() * 100 < chanceKabur;
        if (kabur) {
            System.out.println("[DISASTER] Pembeli kabur! Bahan terpakai tapi tidak bayar.");
        }
    }   
    
    public boolean gantiMenuAtauPulang(Menu[] daftarMenu, Menu menuHabis) {
        pesanan.remove(menuHabis);
        if (Math.random() < 0.5 && daftarMenu.length > 1) {
            Menu[] sisaMenu = Arrays.stream(daftarMenu)
                .filter(m -> !m.getName().equals(menuHabis.getName()))
                .toArray(Menu[]::new);
            if (sisaMenu.length > 0) {
                Menu menuBaru = sisaMenu[(int)(Math.random() * sisaMenu.length)];
                pesanan.merge(menuBaru, 1, Integer::sum);
                System.out.println("[CUSTOMER] Menu habis, ganti ke: " + menuBaru.getName());
                return true;
            }
        }
        System.out.println("[CUSTOMER] Menu habis, pembeli pulang.");
        return false;
    }

    public double bayarPesanan(double money) {
        return money;
    }
}