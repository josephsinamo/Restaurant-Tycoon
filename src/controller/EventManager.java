package controller;

import core.Kitchen;
import core.Restaurant;
import event.GameEvent;
import event.disaster.Rats;
import event.disaster.Storm;
import event.disaster.SuperStorm;
import event.festival.FoodParty;
import event.festival.GoldenDay;
import event.festival.MarketDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventManager {
    private static final int FESTIVAL_CHANCE = 25;
    private final Random random = new Random();
    private final Kitchen kitchen;

    private final List<GameEvent> disasters = new ArrayList<>();
    private final List<GameEvent> festivals = new ArrayList<>();

    public EventManager(Kitchen kitchen, Restaurant restaurant) {
        this.kitchen = kitchen;

        disasters.add(new Rats(kitchen));
        disasters.add(new Storm());
        disasters.add(new SuperStorm());

        festivals.add(new FoodParty());
        festivals.add(new GoldenDay());
        festivals.add(new MarketDay());
    }

    public List<String> runDailyEvents(int[] activeItems, Restaurant restaurant) {
        List<String> log = new ArrayList<>();
        log.add("=== Event Harian ===");

        int jumlahBencana = 1 + random.nextInt(2);
        for (int i = 0; i < jumlahBencana; i++) {
            String baris = triggerRandomDisaster(restaurant);
            if (baris != null) {
                log.add(baris);
            }
        }

        GameEvent festival = tryTriggerFestival();
        if (festival != null) {
            log.add("[Festival] " + festival.getEventName());
        } else {
            log.add("[Festival] Tidak ada festival hari ini.");
        }

        activateItems(activeItems);
        log.add("====================");
        return log;
    }

    private String triggerRandomDisaster(Restaurant restaurant) {
        GameEvent disaster = disasters.get(random.nextInt(disasters.size()));

        if (disaster instanceof Rats) {
            if (restaurant.getJimatCleaner() != null
                    && restaurant.getJimatCleaner().cegahTikus()) {
                restaurant.catatTikusDicegahJimat();
                return "[Bencana] Tikus lapar — dicegah jimat Cleaner!";
            }
            disaster.execute();
            restaurant.catatTikusMenyerang();
            return "[Bencana] Tikus lapar menyerang! Sebagian bahan baku dimakan.";
        }

        disaster.execute();
        return "[Bencana] " + disaster.getEventName();
    }

    private GameEvent tryTriggerFestival() {
        if (random.nextInt(100) < FESTIVAL_CHANCE) {
            GameEvent festival = festivals.get(random.nextInt(festivals.size()));
            festival.execute();
            return festival;
        }
        return null;
    }

    public void activateItems(int[] activeItems) {
        // reserved
    }
}
