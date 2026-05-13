package models.items;

import event.GameEvent;

import java.util.Random;

public abstract class Charm implements GameEvent {
    protected String name;
    protected double effectPercentage;
    protected double price;
    protected static final Random random = new Random();

    public Charm(String name, double price) {
        this.name = name;
        this.price = price;
        this.effectPercentage = 10 + random.nextDouble() * 40;
    }

    public String getName() { return name; }
    public double getEffectPercentage() { return effectPercentage; }
    public double getPrice() { return price; }

    public abstract String getEffectDescription();

    @Override
    public String toString() {
        return String.format("%s (Efek: %.1f%%) - Rp%.0f", name, effectPercentage, price);
    }
}