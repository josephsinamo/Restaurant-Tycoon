package models.items.talisman;

import models.items.Charm;

public class Cleaner extends Charm {
    public Cleaner() {
        super("Jimat Cleaner", 6000);
    }

    @Override
    public String getEffectDescription() {
        return String.format("Mengurangi peluang tikus datang sebesar %.1f%%", effectPercentage);
    }

    @Override
    public String getEventName() { return "Jimat Cleaner"; }

    @Override
    public void execute() {
        System.out.printf("[JIMAT] %s aktif: %s%n", name, getEffectDescription());
    }
}