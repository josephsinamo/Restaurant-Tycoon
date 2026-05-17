package controller;

import core.Restaurant;
import core.Supplier;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

/**
 * GameSave — serialisasi & deserialisasi state GameManager ke/dari file JSON.
 *
 * Tidak memerlukan library eksternal; menggunakan JSON writer/reader minimal
 * yang mencakup tepat field-field yang dibutuhkan game.
 *
 * Lokasi file: ./saves/savegame.json (relatif terhadap working directory)
 */
public class GameSave {

    // ── Konstanta ──────────────────────────────────────────────────────────
    private static final String SAVE_DIR = "saves";
    private static final String SAVE_FILE = "saves/savegame.json";

    // ── Singleton ──────────────────────────────────────────────────────────
    private static GameSave instance;

    private GameSave() {
    }

    public static GameSave getInstance() {
        if (instance == null)
            instance = new GameSave();
        return instance;
    }

    // ══════════════════════════════════════════════════════════════════════
    // PUBLIC API
    // ══════════════════════════════════════════════════════════════════════

    /** Kembalikan true jika file save sudah ada di disk. */
    public boolean hasSaveFile() {
        return Files.exists(Paths.get(SAVE_FILE));
    }

    /**
     * Serialisasi state GameManager saat ini dan tulis ke disk.
     *
     * @param gm instance GameManager yang akan disimpan
     * @throws IOException jika file tidak bisa ditulis
     */
    public void save(GameManager gm) throws IOException {
        Files.createDirectories(Paths.get(SAVE_DIR));

        Restaurant r = gm.getRestaurant();

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // ── State inti ───────────────────────────────────────────────────
        appendField(sb, "currentDay", gm.getCurrentDay(), false);
        sb.append(",\n");

        // ── Restaurant ───────────────────────────────────────────────────
        appendField(sb, "money", r.getMoney(), false);
        sb.append(",\n");
        appendField(sb, "kapasitas", r.getKapasitas(), false);
        sb.append(",\n");
        appendField(sb, "poinJimatKeamanan", r.getPoinJimatKeamanan(), false);
        sb.append(",\n");
        appendField(sb, "poinJimatMenarik", r.getPoinJimatMenarik(), false);
        sb.append(",\n");
        appendField(sb, "poinJimatKebersihan", r.getPoinJimatKebersihan(), false);
        sb.append(",\n");

        // ── Stok bahan baku (nested object) ──────────────────────────────
        Map<String, Integer> stok = r.getStokBahanBaku();
        sb.append("  \"stokBahanBaku\": {\n");
        int idx = 0;
        for (Map.Entry<String, Integer> entry : stok.entrySet()) {
            sb.append("    ").append(jsonString(entry.getKey()))
                    .append(": ").append(entry.getValue());
            if (idx < stok.size() - 1)
                sb.append(",");
            sb.append("\n");
            idx++;
        }
        sb.append("  }\n");

        sb.append("}");

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAVE_FILE))) {
            writer.write(sb.toString());
        }

        System.out.println("[GameSave] Game disimpan ke " + Paths.get(SAVE_FILE).toAbsolutePath());
    }

    /**
     * Baca file save dari disk dan terapkan nilainya ke GameManager yang diberikan.
     *
     * @param gm GameManager yang akan diisi state-nya
     * @throws IOException jika file tidak bisa dibaca
     */
    public void load(GameManager gm) throws IOException {
        if (!hasSaveFile()) {
            throw new FileNotFoundException("Tidak ada file save di " + SAVE_FILE);
        }

        String raw = new String(Files.readAllBytes(Paths.get(SAVE_FILE)));
        Map<String, String> flat = parseJsonFlat(raw);

        Restaurant r = gm.getRestaurant();

        // ── State inti ───────────────────────────────────────────────────
        if (flat.containsKey("currentDay"))
            gm.setCurrentDay(Integer.parseInt(flat.get("currentDay").trim()));

        // ── Restaurant ───────────────────────────────────────────────────
        if (flat.containsKey("money"))
            r.setMoney(Double.parseDouble(flat.get("money").trim()));

        if (flat.containsKey("kapasitas"))
            r.setKapasitas(Integer.parseInt(flat.get("kapasitas").trim()));

        if (flat.containsKey("poinJimatKeamanan"))
            r.setPoinJimatKeamanan(Integer.parseInt(flat.get("poinJimatKeamanan").trim()));

        if (flat.containsKey("poinJimatMenarik"))
            r.setPoinJimatMenarik(Integer.parseInt(flat.get("poinJimatMenarik").trim()));

        if (flat.containsKey("poinJimatKebersihan"))
            r.setPoinJimatKebersihan(Integer.parseInt(flat.get("poinJimatKebersihan").trim()));

        // ── Stok bahan baku ───────────────────────────────────────────────
        Map<String, Integer> stokFromFile = parseStokObject(raw);
        if (!stokFromFile.isEmpty()) {
            r.setStokBahanBaku(stokFromFile);
        }

        System.out.println("[GameSave] Game dimuat dari " + Paths.get(SAVE_FILE).toAbsolutePath());
    }

    /** Hapus file save (dipakai saat mulai game baru). */
    public void deleteSave() {
        try {
            Files.deleteIfExists(Paths.get(SAVE_FILE));
        } catch (IOException e) {
            System.err.println("[GameSave] Tidak bisa menghapus save: " + e.getMessage());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // JSON WRITER HELPERS
    // ══════════════════════════════════════════════════════════════════════

    private void appendField(StringBuilder sb, String key, Object value, boolean isString) {
        sb.append("  ").append(jsonString(key)).append(": ");
        if (isString)
            sb.append(jsonString(value.toString()));
        else
            sb.append(value);
    }

    private String jsonString(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    // ══════════════════════════════════════════════════════════════════════
    // JSON READER HELPERS (minimal flat parser — no external library)
    // ══════════════════════════════════════════════════════════════════════

    /**
     * Parse JSON object datar (satu level) ke Map<key, rawValue>.
     * Blok nested seperti stokBahanBaku dilewati; gunakan parseStokObject.
     */
    private Map<String, String> parseJsonFlat(String json) {
        Map<String, String> map = new HashMap<>();
        int start = json.indexOf('{');
        int end = json.lastIndexOf('}');
        if (start < 0 || end < 0)
            return map;
        String body = json.substring(start + 1, end);

        for (String rawLine : body.split("\n")) {
            String line = rawLine.trim();
            if (line.startsWith("{") || line.startsWith("}") || line.isBlank())
                continue;
            if (!line.contains(":"))
                continue;

            int colon = line.indexOf(':');
            String key = unquote(line.substring(0, colon).trim());
            String value = line.substring(colon + 1).trim().replaceAll(",$", "");

            if (value.equals("{") || value.equals("}") || value.startsWith("{"))
                continue;

            map.put(key, unquote(value));
        }
        return map;
    }

    /** Ekstrak nested object "stokBahanBaku" dari raw JSON. */
    private Map<String, Integer> parseStokObject(String json) {
        Map<String, Integer> stok = new HashMap<>();
        int blockStart = json.indexOf("\"stokBahanBaku\"");
        if (blockStart < 0)
            return stok;

        int objStart = json.indexOf('{', blockStart);
        int objEnd = json.indexOf('}', objStart);
        if (objStart < 0 || objEnd < 0)
            return stok;

        String block = json.substring(objStart + 1, objEnd);
        for (String rawLine : block.split("\n")) {
            String line = rawLine.trim().replaceAll(",$", "");
            if (line.isBlank() || !line.contains(":"))
                continue;

            int colon = line.indexOf(':');
            String key = unquote(line.substring(0, colon).trim());
            String value = line.substring(colon + 1).trim();

            try {
                stok.put(key, Integer.parseInt(value));
            } catch (NumberFormatException ignored) {
            }
        }
        return stok;
    }

    private String unquote(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2)
            return s.substring(1, s.length() - 1)
                    .replace("\\\"", "\"")
                    .replace("\\\\", "\\");
        return s;
    }
}
