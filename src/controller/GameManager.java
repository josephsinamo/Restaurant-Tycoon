package controller;

import core.Restaurant;
import core.Supplier;
import entities.Customer;
import java.util.Set;
import models.RawMaterial;
import models.menu.*;
import view.Frame;

public class GameManager {

    private static GameManager instance;

    private Restaurant   restaurant;
    private Supplier     supplier;
    private EventManager eventManager;
    private Frame        gameFrame;
    private int          currentDay;
    public enum Fase { PERSIAPAN, BERJUALAN }
    private Fase faseSekarang = Fase.PERSIAPAN;

    /** Apakah game sedang di-pause (misal: balik ke main menu). */
    private boolean paused = false;

    // ══ Constructor ═══════════════════════════════════════════════════════
    public GameManager() {
        this.restaurant   = new Restaurant();
        this.supplier     = new Supplier();
        this.eventManager = new EventManager(restaurant.getKitchen(), restaurant);
        this.currentDay   = 1;
        setupGame();   // <<< inisialisasi konten default saat game baru dibuat
    }

    // ══ Setup awal konten game ═════════════════════════════════════════════
    private void setupGame() {
        // ── Bahan baku default ─────────────────────────────────────────
        RawMaterial beras  = new RawMaterial("Beras");
        RawMaterial telor  = new RawMaterial("Telor");
        RawMaterial ayam   = new RawMaterial("Ayam");
        RawMaterial kopi   = new RawMaterial("Kopi");
        RawMaterial gula   = new RawMaterial("Gula");
        RawMaterial tepung = new RawMaterial("Tepung");
        RawMaterial bumbu  = new RawMaterial("Bumbu");

        // ── Harga katalog supplier — bahan baku ────────────────────────
        supplier.setHargaBahanBaku(beras,  2000);
        supplier.setHargaBahanBaku(telor,  1500);
        supplier.setHargaBahanBaku(ayam,   5000);
        supplier.setHargaBahanBaku(kopi,   3000);
        supplier.setHargaBahanBaku(gula,   1000);
        supplier.setHargaBahanBaku(tepung, 1500);
        supplier.setHargaBahanBaku(bumbu,  2500);


        // ── Menu default ───────────────────────────────────────────────
        Menu nasiGoreng   = new Food ("NasiGoreng",   Set.of(beras, telor, bumbu));
        Menu kentangGoreng = new Snack("KentangGoreng", Set.of(tepung, telor, bumbu),
                                       JenisSnack.kentang_goreng);
        Menu milkDrink    = new Drink("MilkDrink",    Set.of(gula, tepung),
                                       JenisDrink.Milk);

                restaurant.addMenu(nasiGoreng,    15000);
        restaurant.addMenu(kentangGoreng,  8000);
        restaurant.addMenu(milkDrink,     10000);

        // ── Stok awal bahan baku ───────────────────────────────────────
        restaurant.tambahBahanBaku(beras,  20);
        restaurant.tambahBahanBaku(telor,  15);
        restaurant.tambahBahanBaku(ayam,   15);
        restaurant.tambahBahanBaku(kopi,   10);
        restaurant.tambahBahanBaku(gula,   10);
        restaurant.tambahBahanBaku(tepung, 10);
        restaurant.tambahBahanBaku(bumbu,  10);

        // ── Kapasitas awal ─────────────────────────────────────────────
        restaurant.setKapasitas(10);
        supplier.initKatalogJimat(); 
    }

    // ══ Singleton ═════════════════════════════════════════════════════════
    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    // ══ Lifecycle ═════════════════════════════════════════════════════════

    public void startGame() {
        paused = false;
        if (this.gameFrame == null) {
            this.gameFrame = new Frame();
        }
        System.out.println("Permainan Dimulai!");
        System.out.println("Hari ke-" + currentDay);
    }

    public void mulaiBerjualan() {
        if (faseSekarang != Fase.PERSIAPAN) return;
        faseSekarang = Fase.BERJUALAN;

        int[] activeItems = getActiveItems();
        //eventManager.runDailyEvents(activeItems, restaurant);

        int kapasitas     = restaurant.getKapasitas();
        int jumlahPembeli = (int)(Math.random() * kapasitas) + 1;

        for (int i = 0; i < jumlahPembeli; i++) {
            Customer pelanggan = new Customer();
            if (restaurant.getJumlahPengunjungHariIni() + pelanggan.getJumlahPelanggan()
                    > kapasitas) break;
            restaurant.layaniPelanggan(pelanggan);
            notifyUI(); // update UI per pelanggan
        }

        restaurant.akhirHari();
        System.out.println("Total penjualan: Rp " +
        String.format("%.0f", restaurant.getTotalPenjualanHariIni()));
        System.out.println("Pengunjung: " + restaurant.getJumlahPengunjungHariIni());
        notifyUI(); // update UI final
    }

    public void nextDay() {
        if (faseSekarang != Fase.BERJUALAN) return;
        this.currentDay++;
        restaurant.resetDataHarian();
        faseSekarang = Fase.PERSIAPAN;
        notifyUI(); // ← tambahkan ini
    }

    public Fase getFaseSekarang() { 
    return faseSekarang; 
}

    /** Tandai game sebagai di-pause (misal saat kembali ke main menu). */
    public void pauseGame() {
        this.paused = true;
        System.out.println("Game di-pause pada hari ke-" + currentDay);
    }
    // Tambah di dalam class GameManager, setelah field paused:
    public interface GameStateListener {
        void onStateChanged();
    }
    private GameStateListener stateListener;

    public void setGameStateListener(GameStateListener listener) {
        this.stateListener = listener;
    }

    private void notifyUI() {
        if (stateListener != null) {
            javax.swing.SwingUtilities.invokeLater(() -> stateListener.onStateChanged());
        }
    }

    public boolean isPaused() { return paused; }

    /** Stub save — implementasi penuh ada di GameSave. */
    public void save() {
        GameSave.simpan(this);
    }

    // ══ Helper internal ════════════════════════════════════════════════════

    /**
     * Ambil status jimat aktif sebagai array int[3]:
     * [0] = keamanan, [1] = menarik, [2] = kebersihan.
     * Nilai 1 = aktif, 0 = tidak aktif.
     */
    private int[] getActiveItems() {
        return new int[]{
            restaurant.getPoinJimatKeamanan()   > 0 ? 1 : 0,
            restaurant.getPoinJimatMenarik()    > 0 ? 1 : 0,
            restaurant.getPoinJimatKebersihan() > 0 ? 1 : 0
        };
    }

    // ══ Getter ═════════════════════════════════════════════════════════════

    public Restaurant   getRestaurant()   { return restaurant; }
    public Supplier     getSupplier()     { return supplier; }
    public EventManager getEventManager() { return eventManager; }
    public int          getCurrentDay()   { return currentDay; }
    public Frame        getGameFrame()    { return gameFrame; }

    // ══ Setter — dipakai GameSave saat restore ════════════════════════════

    public void setCurrentDay(int day)            { if (day > 0) this.currentDay = day; }
    public void setRestaurant(Restaurant resto)   { if (resto != null) this.restaurant = resto; }
    public void setSupplier(Supplier sup)         { if (sup   != null) this.supplier   = sup; }
}
