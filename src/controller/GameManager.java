package controller;

import core.Restaurant;
import core.Supplier;
import entities.Customer;
import view.Frame;

public class GameManager {
    
    private static GameManager instance;
    private Restaurant restaurant;
    private Supplier supplier;
    private EventManager eventManager;
    private Frame gameFrame;
    private int currentDay;
    private int day;
    
    private GameManager() {
        this.restaurant = new Restaurant();
        this.supplier = new Supplier();
        this.eventManager = new EventManager();
        this.currentDay = 1;    
    }
    
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    public void startNewGame() {
        this.day = 1;
        this.restaurant = new Restaurant();

        // Menambahkan menu awal (default) agar pelanggan bisa memesan
        models.menu.Food nasiGoreng = new models.menu.Food("Nasi Goreng");
        models.menu.Drink esTeh = new models.menu.Drink("Es Teh Manis");

        this.restaurant.addMenu(nasiGoreng, 15000.0);
        this.restaurant.addMenu(esTeh, 5000.0);

        // Inisialisasi Supplier (Katalog Jimat & Bahan Baku)
        models.jimat.Security jimatAman = new models.jimat.Security("Aman", 50);
        models.jimat.Charming jimatMenarik = new models.jimat.Charming("Menarik", 100);
        models.jimat.Cleaner jimatBersih = new models.jimat.Cleaner("Bersih", 150);

        this.supplier.setHargaJimat(jimatAman, 15000.0);
        this.supplier.setHargaJimat(jimatMenarik, 20000.0);
        this.supplier.setHargaJimat(jimatBersih, 10000.0);

        models.RawMaterial beras = new models.RawMaterial("Beras", 1, 10000.0, "kg");
        this.supplier.setHargaBahanBaku(beras, 10000.0);

        System.out.println("=== Permainan Baru Dimulai! ===");
        System.out.println("Hari ke-" + this.day);
        System.out.println("Modal awal: Rp 50000");
        System.out.println("Menu awal (Nasi Goreng & Es Teh) telah ditambahkan!");
    }

    
    public void runDay() {
        System.out.println("\n=== Memulai Hari ke-" + day + " ===");

        // 1. Jalankan Event Harian (Bencana, Festival, Efek Jimat)
        int[] activeItems = {
                restaurant.getPoinJimatKeamanan() > 0 ? 1 : 0,
                restaurant.getPoinJimatMenarik() > 0 ? 1 : 0,
                restaurant.getPoinJimatKebersihan() > 0 ? 1 : 0
        };
        eventManager.runDailyEvents(activeItems);

        // 2. Simulasi Kedatangan Pelanggan
        Customer pelangganHariIni = new Customer();
        System.out.println("Melayani pelanggan yang datang...");
        restaurant.layaniPelanggan(pelangganHariIni);

        System.out.println("Sisa Uang Restoran saat ini: " + restaurant.getMoney());
    }

     public void endDay() {
        System.out.println("=== Hari ke-" + day + " Selesai ===\n");
        day++;
    }
    
    public void setGameView(Frame view) {
        this.gameFrame = view;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public Supplier getSupplier() {
        return supplier;
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public int getCurrentDay() {
        return currentDay;
    }
    
    public Frame getGameFrame() {
        return gameFrame;
    }
}
