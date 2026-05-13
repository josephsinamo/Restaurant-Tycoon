package event.festival;

import event.GameEvent;

import java.util.Random;

public class MarketDay implements GameEvent {
    private static final int[] FRACTIONS = {2, 3, 4, 5};
    private final Random random = new Random();
    private double discountMultiplier;

    public MarketDay() {
        int fraction = FRACTIONS[random.nextInt(FRACTIONS.length)];
        this.discountMultiplier = 1.0 - (1.0 / fraction);
    }

    public double getDiscountMultiplier() {
        return discountMultiplier;
    }

    @Override
    public String getEventName() { return "Market Day"; }

    @Override
    public void execute() {
        int fraction = FRACTIONS[random.nextInt(FRACTIONS.length)];
        this.discountMultiplier = 1.0 - (1.0 / fraction);
        System.out.printf("[FESTIVAL] Market Day! Harga bahan baku diskon %.0f%%%n", (1.0 / fraction) * 100);
    }
}