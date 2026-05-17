package controller;

import event.GameEvent;
import event.disaster.*;
import event.festival.*;
import models.jimat.Charming;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.Kitchen;
import core.Restaurant;

public class EventManager {
    private static final int FESTIVAL_CHANCE = 25;
    private final Random random = new Random();
    private final Restaurant restaurant;

    private final List<GameEvent> disasters = new ArrayList<>();
    private final List<GameEvent> festivals = new ArrayList<>();
    private final List<GameEvent> itemEvents = new ArrayList<>();

        public EventManager(Kitchen kitchen, Restaurant restaurant) {
        this.restaurant = restaurant;
        
        disasters.add(new Rats(kitchen));
        disasters.add(new Storm());
        disasters.add(new SuperStorm());

        festivals.add(new FoodParty());
        festivals.add(new GoldenDay());
        festivals.add(new MarketDay());

    }
    

    public GameEvent triggerRandomDisaster(Restaurant restaurant) {
        GameEvent disaster = disasters.get(random.nextInt(disasters.size()));

        // cek jimat Cleaner untuk tikus
        if (disaster instanceof Rats) {
            if (restaurant.getJimatCleaner() != null &&
                restaurant.getJimatCleaner().cegahTikus()) {
                System.out.println("[JIMAT] Cleaner mencegah tikus!");
                return null;
            }
        }

        disaster.execute();
        return disaster;
    }

    public GameEvent tryTriggerFestival() {
        if (random.nextInt(100) < FESTIVAL_CHANCE) {
            GameEvent festival = festivals.get(random.nextInt(festivals.size()));
            festival.execute();
            return festival;
        }
        return null;
    }


    // masih perlu di cek
    public void activateItems(int[] activeItems) {
    }

    public void runDailyEvents(int[] activeItems, Restaurant restaurant) {
        System.out.println("=== EVENT HARIAN ===");

        // minimal 1 bencana, random bisa lebih
        int jumlahBencana = 1 + random.nextInt(2);
        for (int i = 0; i < jumlahBencana; i++) {
            triggerRandomDisaster(restaurant);
        }

        GameEvent festival = tryTriggerFestival();
        if (festival == null) System.out.println("[FESTIVAL] Tidak ada festival hari ini.");
        activateItems(activeItems);
        System.out.println("====================");
    }
}