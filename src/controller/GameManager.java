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
        eventManager.runDailyEvents(activeItems);
        
    
        Customer pelangganHariIni = new Customer();
        System.out.println("Melayani pelanggan...");
        restaurant.layaniPelanggan(pelangganHariIni);
        
        System.out.println("Total Uang: " + restaurant.getMoney());
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
