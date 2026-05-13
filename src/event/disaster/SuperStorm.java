package event.disaster;

import event.GameEvent;

public class SuperStorm implements GameEvent {
    public SuperStorm() {}

    @Override
    public void execute() {
        System.out.println("[DISASTER] Badai Super! Tidak ada pelanggan yang datang hari ini.");
    }

    @Override
    public String getEventName() { return "Badai Super"; }

    public double getCustomerMultiplier() {
        return 0.0;
    }
}