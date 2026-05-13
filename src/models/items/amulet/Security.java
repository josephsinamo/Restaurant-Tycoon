package models.items.amulet;

import models.items.Charm;

public class Security extends Charm {
    public Security() {
        super("Jimat Security", 5000);
    }

    @Override
    public String getEffectDescription() {
        return String.format("Mengurangi peluang pembeli kabur sebesar %.1f%%", effectPercentage);
    }

    @Override
    public String getEventName() { return "Jimat Security"; }

    @Override
    public void execute() {
        System.out.printf("[JIMAT] %s aktif: %s%n", name, getEffectDescription());
    }
}