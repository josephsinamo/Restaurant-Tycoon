package controller;

import core.Restaurant;
import core.Supplier;
import entities.Customer;
import java.util.Set;
import models.RawMaterial;
import models.jimat.*;
import models.menu.*;
import view.Frame;

public class GameManager {

    private static GameManager instance;

    private Restaurant   restaurant;
    private Supplier     supplier;
    private EventManager eventManager;
    private Frame        gameFrame;
    private int          currentDay;

    /** Apakah game sedang di-pause (misal: balik ke main menu). */
    private boolean paused = false;

    // ══ Constructor ═══════════════════════════════════════════════════════
    public GameManager() {
        this.restaurant   = new Restaurant();
        this.supplier     = new Supplier();
        this.eventManager = new EventManager(restaurant.getKitchen());
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

        restaurant.addMenu(nasiGoreng,    15_000);
        restaurant.addMenu(kentangGoreng,  8_000);
        restaurant.addMenu(milkDrink,     10_000);

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

    /**
     * Jalankan satu hari penuh:
     * 1. Tambah nomor hari
     * 2. Reset tracking harian di restaurant
     * 3. Jalankan event harian (cuaca, insiden, dll.)
     * 4. Buat & layani pelanggan
     */
    public void nextDay() {
        paused = false;
        this.currentDay++;

        // Reset data harian sebelum aktivitas dimulai
        restaurant.resetDataHarian();

        System.out.println("\n=== Hari ke-" + currentDay + " ===");

        // Jalankan event berdasarkan jimat yang aktif
        int[] activeItems = getActiveItems();
        eventManager.runDailyEvents(activeItems);

        // Buat pelanggan dan layani
        Customer pelangganHariIni = new Customer();
        System.out.println("Melayani pelanggan...");
        restaurant.layaniPelanggan(pelangganHariIni);

        System.out.println("Total penjualan hari ini: Rp "
                + String.format("%.0f", restaurant.getTotalPenjualanHariIni()));
        System.out.println("Pengunjung: " + restaurant.getJumlahPengunjungHariIni());
    }

    /** Tandai game sebagai di-pause (misal saat kembali ke main menu). */
    public void pauseGame() {
        this.paused = true;
        System.out.println("Game di-pause pada hari ke-" + currentDay);
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
