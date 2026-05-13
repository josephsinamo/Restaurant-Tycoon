package exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super("Uang tidak cukup untuk melakukan transaksi ini.");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(double required, double available) {
        super(String.format("Uang tidak cukup. Dibutuhkan: Rp%.0f, Tersedia: Rp%.0f", required, available));
    }
}