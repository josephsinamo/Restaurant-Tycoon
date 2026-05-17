package event.disaster;

import event.GameEvent;
import core.Kitchen;
import java.util.Random;

public class Rats implements GameEvent {
    private static final int[] FRACTIONS = {2, 3, 4, 5};
    private final Random random = new Random();
    private final Kitchen kitchen;

    public Rats(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    @Override
    public String getEventName() { return "Tikus Lapar"; }

    @Override
    public void execute() {
        int fraction = FRACTIONS[random.nextInt(FRACTIONS.length)];
        double rate = 1.0 / fraction;
        kitchen.getDisasterTikusAttack(rate);
        System.out.println("[DISASTER] " + getEventName() +
            "! Bahan baku tersisa 1/" + fraction);
    }
}