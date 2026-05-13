package models;

import exception.OutOfStockException;
import exception.StorageFullException;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static Storage instance;

    private Map<String, RawMaterial> stock;
    private double maxCapacity;
    private double currentCapacity;

    private Storage() {
        this.stock = new HashMap<>();
        this.maxCapacity = 1000.0;
        this.currentCapacity = 0.0;
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public void addMaterial(RawMaterial material, double amount) throws StorageFullException {
        if (currentCapacity + amount > maxCapacity) {
            throw new StorageFullException(material.getName(), amount);
        }
        stock.merge(material.getName(), material, (existing, newMat) -> {
            existing.setQuantity(existing.getQuantity() + amount);
            return existing;
        });
        if (!stock.containsKey(material.getName())) {
            material.setQuantity(amount);
            stock.put(material.getName(), material);
        }
        currentCapacity += amount;
    }

    public RawMaterial getMaterial(String name) throws OutOfStockException {
        RawMaterial material = stock.get(name);
        if (material == null || material.getQuantity() <= 0) {
            throw new OutOfStockException(name);
        }
        return material;
    }

    public void reduceAllByFraction(int fraction) {
        stock.values().forEach(m -> {
            double reduced = Math.floor(m.getQuantity() / fraction);
            m.consume(reduced);
            currentCapacity -= reduced;
            if (currentCapacity < 0) currentCapacity = 0;
        });
    }

    public Map<String, RawMaterial> getStock() { return stock; }
    public double getCurrentCapacity() { return currentCapacity; }
    public double getMaxCapacity() { return maxCapacity; }

    public void upgradeCapacity(double additionalCapacity) {
        this.maxCapacity += additionalCapacity;
    }

    public void clearLeftovers() {
        stock.values().forEach(m -> m.setQuantity(0));
        currentCapacity = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== GUDANG ===\n");
        stock.values().forEach(m -> sb.append(m).append("\n"));
        sb.append(String.format("Kapasitas: %.0f/%.0f%n", currentCapacity, maxCapacity));
        return sb.toString();
    }
}