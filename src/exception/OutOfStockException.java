package exception;

public class OutOfStockException extends Exception {
    public OutOfStockException() {
        super("Stok bahan baku habis!");
    }

    public OutOfStockException(String itemName) {
        super("Stok habis: " + itemName);
    }

    public OutOfStockException(String itemName, double required, double available) {
        super(String.format("Stok %s tidak cukup. Dibutuhkan: %.0f, Tersedia: %.0f", itemName, required, available));
    }
}