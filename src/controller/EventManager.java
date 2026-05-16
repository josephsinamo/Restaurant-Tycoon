package controller;

import event.GameEvent;
import event.disaster.*;
import event.festival.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import models.items.amulet.Security;
import models.items.talisman.Charming;
import models.items.talisman.Cleaner;

public class EventManager {
    private static final int FESTIVAL_CHANCE = 25;
    private final Random random = new Random();

    private final List<GameEvent> disasters = new ArrayList<>();
    private final List<GameEvent> festivals = new ArrayList<>();
    private final List<GameEvent> itemEvents = new ArrayList<>();

    public EventManager() {
        disasters.add(new Rats());
        disasters.add(new Storm());
        disasters.add(new SuperStorm());

        festivals.add(new FoodParty());
        festivals.add(new GoldenDay());
        festivals.add(new MarketDay());

        itemEvents.add(new Security());
        itemEvents.add(new Charming());
        itemEvents.add(new Cleaner());
    }

    public GameEvent triggerRandomDisaster() {
        GameEvent disaster = disasters.get(random.nextInt(disasters.size()));
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
        for (int i = 0; i < activeItems.length && i < itemEvents.size(); i++) {
            if (activeItems[i] == 1) {
                itemEvents.get(i).execute();
            }
        }
    }

    public void runDailyEvents(int[] activeItems) {
        System.out.println("=== EVENT HARIAN ===");
        triggerRandomDisaster();
        GameEvent festival = tryTriggerFestival();
        if (festival == null) System.out.println("[FESTIVAL] Tidak ada festival hari ini.");
        activateItems(activeItems);
        System.out.println("====================");
    }
}