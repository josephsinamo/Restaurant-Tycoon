package event.disaster;

import event.GameEvent;
import models.Storage;

import java.util.Random;

public class Rats implements GameEvent {
    private static final int[] FRACTIONS = {2, 3, 4, 5};
    private final Random random = new Random();

    public Rats() {}

    @Override
    public String getEventName() { return "Tikus Lapar"; }

    @Override
    public void execute() {
        int fraction = FRACTIONS[random.nextInt(FRACTIONS.length)];
        Storage.getInstance().reduceAllByFraction(fraction);
        System.out.println("[DISASTER] " + getEventName() + "! Bahan baku berkurang 1/" + fraction);
    }
}