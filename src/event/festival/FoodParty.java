package event.festival;

import event.GameEvent;

import java.util.Random;

public class FoodParty implements GameEvent {
    private final Random random = new Random();
    private double priceMultiplier;

    public FoodParty() {
        this.priceMultiplier = random.nextBoolean() ? 2.0 : 1.5;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    @Override
    public String getEventName() { return "Food Party"; }

    @Override
    public void execute() {
        this.priceMultiplier = random.nextBoolean() ? 2.0 : 1.5;
        System.out.println("[FESTIVAL] Food Party! Harga menu naik " + priceMultiplier + "x hari ini.");
    }
}