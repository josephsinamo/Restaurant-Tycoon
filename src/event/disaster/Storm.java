package event.disaster;

import event.GameEvent;

import java.util.Random;

public class Storm implements GameEvent {
    private static final int[] FRACTIONS = {2, 3, 4, 5};
    private final Random random = new Random();
    private double customerMultiplier;

    public Storm() {
        this.customerMultiplier = 1.0;
    }

    public double getCustomerMultiplier() { return customerMultiplier; }

    @Override
    public String getEventName() { return "Badai"; }

    @Override
    public void execute() {
        int fraction = FRACTIONS[random.nextInt(FRACTIONS.length)];
        this.customerMultiplier = 1.0 / fraction;
        System.out.println("[DISASTER] " + getEventName() + "! Pelanggan yang datang hanya 1/" + fraction + " dari kapasitas.");
    }
}