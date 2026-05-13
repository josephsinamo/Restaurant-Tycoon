package exception;
public class StokHabisException extends Exception {
    public StokHabisException() {
        super("Stok bahan baku telah habis!");
    }

    public StokHabisException(String message) {
        super(message); 
    }
}
