package controller;

import core.Restaurant;
import core.Supplier;
import entities.Customer;
import event.disaster.Rats;
import models.RawMaterial;
import models.menu.Food;
import models.menu.Snack;
import view.Frame;
import models.jimat.*;

public class GameManager {
    
    private static GameManager instance;
    private Restaurant restaurant;
    private Supplier supplier;
    private EventManager eventManager;
    private Frame gameFrame;
    private int currentDay;
    
    public GameManager() {
        this.restaurant = new Restaurant();
        this.supplier = new Supplier();
        this.eventManager = new EventManager();
        this.currentDay = 1;    
    }

    private void setupGame() {
        // Default raw materials
        RawMaterial beras = new RawMaterial("Beras");
        RawMaterial ayam = new RawMaterial("Ayam");
        RawMaterial kopi = new RawMaterial("Kopi");
        RawMaterial gula = new RawMaterial("Gula");
        RawMaterial tepung = new RawMaterial("Tepung");

        // Stock supplier catalogue
        supplier.setHargaBahanBaku(beras, 2000);
        supplier.setHargaBahanBaku(ayam, 5000);
        supplier.setHargaBahanBaku(kopi, 3000);
        supplier.setHargaBahanBaku(gula, 1000);
        supplier.setHargaBahanBaku(tepung, 1500);

        // Jimat catalogue
        Charming jimatPesona = new Charming("Jimat Pesona", 5);
        Cleaner jimatBersih = new Cleaner("Jimat Kebersihan", 5);
        Security jimatPagar = new Security("Jimat Pagar", 5);
        supplier.setHargaJimat(jimatPesona, 8000);
        supplier.setHargaJimat(jimatBersih, 7000);
        supplier.setHargaJimat(jimatPagar, 9000);

        // Default menu
        restaurant.addMenu(new Food("Nasi Ayam", 15000), 15000);
        restaurant.addMenu(new Snack("Kentang Goreng", 8000), 8000);
        restaurant.addMenu(new Coffe("Kopi Hitam", 10000), 10000);

        // Give starting stock
        restaurant.tambahBahanBaku(beras, 20);
        restaurant.tambahBahanBaku(ayam, 15);
        restaurant.tambahBahanBaku(kopi, 10);
        restaurant.tambahBahanBaku(gula, 10);
        restaurant.tambahBahanBaku(tepung, 10);
    }

    public void save(){

    }

    public void pauseGame(){
        
    }
    
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }
    
    public void startGame() {
        if (this.gameFrame == null) {
            this.gameFrame = new Frame();
        }
        System.out.println("Permainan Dimulai!");
        System.out.println("Hari ke-" + currentDay);
    }
    
    public void nextDay() {
        this.currentDay++;
        
        System.out.println("\n=== Hari ke-" + currentDay + " ===");
        
        int[] activeItems = getActiveItems();
        //eventManager.runDailyEvents(activeItems);
        
    
        Customer pelangganHariIni = new Customer();
        System.out.println("Melayani pelanggan...");
        restaurant.layaniPelanggan(pelangganHariIni);
        
        //System.out.println("Total Uang: " + restaurant.getMoney());
    }
    
    private int[] getActiveItems() {
        int[] activeItems = new int[3];
        activeItems[0] = restaurant.getPoinJimatKeamanan() > 0 ? 1 : 0;
        activeItems[1] = restaurant.getPoinJimatMenarik() > 0 ? 1 : 0;
        activeItems[2] = restaurant.getPoinJimatKebersihan() > 0 ? 1 : 0;
        return activeItems;
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
