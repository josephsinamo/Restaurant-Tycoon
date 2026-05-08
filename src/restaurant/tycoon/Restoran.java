import java.util.ArrayList;
import java.util.HashMap;

public class Restoran {
    // Attributes (Private sesuai tanda - di diagram)
    private int kapasitasRestoran;
    private double money;
    private HashMap <Menu, Double> daftarMenu; // Menggunakan helper class untuk menyimpan Menu dan Price

    // Constructor (Opsional, untuk inisialisasi)
    public Restoran(int kapasitasRestoran, double money) {
        this.kapasitasRestoran = kapasitasRestoran;
        this.money = money;
        this.daftarMenu = new HashMap<>();
    }

    // Methods (Public sesuai tanda + di diagram)
    public void lihatDaftarMenu() {
        // Implementasi untuk menampilkan daftar menu
    }

    public void upgradeKapasitas() {
        // Implementasi untuk menambah kapasitasRestoran
    }

    public void addMenu(Menu menu, double price) {
        // Implementasi untuk menambah menu baru ke ArrayList
    }

    public void setHarga(Menu menu, double price) {
        // Implementasi untuk mengubah harga menu yang sudah ada
    }

    public void racikMenu(Menu menu) {
        // Implementasi logika meracik menu
    }


    public void beli(IPurchasable item, int qty) {
        // Implementasi membeli barang dengan jumlah tertentu
    }

    public void layaniPelanggan(Pelanggan pelanggan) {
        // Implementasi logika pelayanan pelanggan
    }

    private void getPayment(double money) {
        this.money += money;
    }
}
