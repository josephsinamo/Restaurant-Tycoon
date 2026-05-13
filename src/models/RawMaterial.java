
package models;

public class RawMaterial {
    private String name;
    private double quantity;
    private double pricePerUnit;
    private String unit;

    public RawMaterial(String name, double quantity, double pricePerUnit, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
    }

    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }
    public String getUnit() { return unit; }

    public void setQuantity(double quantity) { this.quantity = quantity; }

    public boolean isAvailable(double required) {
        return quantity >= required;
    }

    public void consume(double amount) {
        this.quantity = Math.max(0, this.quantity - amount);
    }

    public void reduceByFraction(int fraction) {
        this.quantity -= Math.floor(this.quantity / fraction);
        if (this.quantity < 0) this.quantity = 0;
    }

    public double getTotalValue() {
        return quantity * pricePerUnit;
    }

    @Override
    public String toString() {
        return String.format("%s: %.1f %s (Rp%.0f/%s)", name, quantity, unit, pricePerUnit, unit);
    }
}

