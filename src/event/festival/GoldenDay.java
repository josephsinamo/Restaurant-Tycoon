package event.festival;

import event.GameEvent;

import java.util.Random;

public class GoldenDay implements GameEvent {
    private final Random random = new Random();
    private double tipMultiplier;

    public GoldenDay() {
        this.tipMultiplier = 1.1 + (random.nextDouble() * 0.5);
    }

    public double getTipMultiplier() {
        return tipMultiplier;
    }

    @Override
    public String getEventName() { return "Golden Day"; }

    @Override
    public void execute() {
        this.tipMultiplier = 1.1 + (random.nextDouble() * 0.5);
        System.out.printf("[FESTIVAL] Golden Day! Semua pelanggan memberi tips (%.2fx)%n", tipMultiplier);
    }
}