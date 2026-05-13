package exception;

public class StorageFullException extends Exception {
    public StorageFullException() {
        super("Gudang penyimpanan penuh!");
    }

    public StorageFullException(String message) {
        super(message);
    }

    public StorageFullException(String itemName, double amount) {
        super(String.format("Tidak dapat menyimpan %.0f unit %s, gudang penuh.", amount, itemName));
    }
}