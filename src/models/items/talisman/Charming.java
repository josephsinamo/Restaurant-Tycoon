package models.items.talisman;

import models.items.Charm;

public class Charming extends Charm {
    public Charming() {
        super("Jimat Charming", 7000);
    }

    @Override
    public String getEffectDescription() {
        return String.format("Menambah peluang pelanggan memberi tip sebesar %.1f%%", effectPercentage);
    }

    @Override
    public String getEventName() { return "Jimat Charming"; }

    @Override
    public void execute() {
        System.out.printf("[JIMAT] %s aktif: %s%n", name, getEffectDescription());
    }
}