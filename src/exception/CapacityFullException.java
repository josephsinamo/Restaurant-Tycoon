package exception;

public class CapacityFullException extends Exception {
    public CapacityFullException() {
        super("Restoran sudah penuh! Tidak ada kapasitas tersisa.");
    }

    public CapacityFullException(String message) {
        super(message);
    }
}