package controller;

import core.Restaurant;
import core.Supplier;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import models.RawMaterial;
import models.jimat.*;
import models.menu.Menu;


public class GameSave {

    private static final String SAVE_FILE = "save.json";

    private GameSave() {}



    public static void simpan(GameManager gm) {
        if (gm == null) return;
        Restaurant resto    = gm.getRestaurant();
        Supplier   supplier = gm.getSupplier();

        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        sb.append("  \"day\": ").append(gm.getCurrentDay()).append(",\n");
        sb.append("  \"fase\": \"").append(gm.getFaseSekarang().name()).append("\",\n");
        sb.append("  \"paused\": ").append(gm.isPaused()).append(",\n");
        sb.append("  \"money\": ").append(resto.getMoney()).append(",\n");
        sb.append("  \"kapasitas\": ").append(resto.getKapasitas()).append(",\n");

  
        sb.append("  \"stokBahanBaku\": ");
        sb.append(mapIntToJson(resto.getStokBahanBaku()));
        sb.append(",\n");

        sb.append("  \"hargaBahanBaku\": ");
        // Konversi HashMap<RawMaterial, Double> → Map<String, Double>
        Map<String, Double> hargaBahanMap = new LinkedHashMap<>();
        for (Map.Entry<RawMaterial, Double> e : supplier.getDaganganBahanBaku().entrySet()) {
            hargaBahanMap.put(e.getKey().getName(), e.getValue());
        }
sb.append(mapDoubleToJson(hargaBahanMap));
        sb.append(",\n");


        Map<String, Double> hargaMenu = new LinkedHashMap<>();
        for (Map.Entry<String, Menu> e : resto.getDaftarMenuMap().entrySet()) {
            hargaMenu.put(e.getKey(), e.getValue().getPrice());
        }
        sb.append("  \"hargaMenu\": ");
        sb.append(mapDoubleToJson(hargaMenu));
        sb.append(",\n");


        // Tentukan jimat mana yang sedang aktif di slot
        Jimat slotCharming  = resto.getJimatCharming();
        Jimat slotCleaner   = resto.getJimatCleaner();
        Jimat slotSecurity  = resto.getJimatSecurity();

        // Kumpulkan semua jimat: inventaris + yang sedang terpasang di slot
        List<Jimat> semuaJimat = new ArrayList<>(resto.getDaftarJimat());
        if (slotCharming != null && !semuaJimat.contains(slotCharming)) semuaJimat.add(slotCharming);
        if (slotCleaner  != null && !semuaJimat.contains(slotCleaner))  semuaJimat.add(slotCleaner);
        if (slotSecurity != null && !semuaJimat.contains(slotSecurity)) semuaJimat.add(slotSecurity);

        sb.append("  \"inventarisJimat\": [\n");
        for (int i = 0; i < semuaJimat.size(); i++) {
            Jimat j     = semuaJimat.get(i);
            boolean aktif = (j == slotCharming || j == slotCleaner || j == slotSecurity);
            sb.append("    ");
            sb.append(jimatToJson(j, aktif));
            if (i < semuaJimat.size() - 1) sb.append(",");
            sb.append("\n");
        }
        sb.append("  ]\n");

        sb.append("}");

    
        try (Writer w = Files.newBufferedWriter(
                Paths.get(SAVE_FILE), StandardCharsets.UTF_8)) {
            w.write(sb.toString());
            System.out.println("[GameSave] Tersimpan ke " + getSavePath());
        } catch (IOException ex) {
            System.err.println("[GameSave] Gagal menyimpan: " + ex.getMessage());
        }
    }



    public static boolean muat(GameManager gm) {
        if (gm == null) return false;

        Path path = Paths.get(SAVE_FILE);
        if (!Files.exists(path)) {
            System.out.println("[GameSave] Tidak ada file save.");
            return false;
        }

        String json;
        try {
            json = Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            System.err.println("[GameSave] Gagal membaca: " + ex.getMessage());
            return false;
        }

        Restaurant resto    = gm.getRestaurant();
        Supplier   supplier = gm.getSupplier();

        try {
        
            gm.setCurrentDay((int) extractLong(json, "day"));
            gm.setFaseDariSave(extractString(json, "fase"));
            resto.setMoney(extractDouble(json, "money"));
            resto.setKapasitas((int) extractLong(json, "kapasitas"));

            
            Map<String, Integer> stok = parseMapInt(extractBlock(json, "stokBahanBaku"));
            if (!stok.isEmpty()) resto.setStokBahanBaku(stok);

            
            Map<String, Double> hargaBahan = parseMapDouble(extractBlock(json, "hargaBahanBaku"));
            for (Map.Entry<String, Double> e : hargaBahan.entrySet()) {
                supplier.setHargaBahanBaku(new RawMaterial(e.getKey()), e.getValue());
            }

            
            Map<String, Double> hargaMenu = parseMapDouble(extractBlock(json, "hargaMenu"));
            for (Map.Entry<String, Menu> e : resto.getDaftarMenuMap().entrySet()) {
                if (hargaMenu.containsKey(e.getKey())) {
                    e.getValue().setHarga(hargaMenu.get(e.getKey()));
                }
            }

            
            List<Map<String, String>> jimatList = parseJimatArray(
                    extractArrayBlock(json, "inventarisJimat"));

            for (Map<String, String> jimatData : jimatList) {
                String  nama  = jimatData.getOrDefault("nama",  "");
                String  tipe  = jimatData.getOrDefault("tipe",  "");
                double  power = Double.parseDouble(jimatData.getOrDefault("power", "0"));
                boolean aktif = Boolean.parseBoolean(jimatData.getOrDefault("aktifDiSlot", "false"));

                Jimat jimat = buatJimat(tipe, nama, power);
                if (jimat == null) continue;

                if (aktif) {
                    // Pasang langsung ke slot aktif (tidak masuk inventaris dulu)
                    pasangKeSlot(resto, jimat);
                } else {
                    resto.tambahKeInventoriJimat(jimat);
                }
            }

        } catch (Exception ex) {
            System.err.println("[GameSave] Format save rusak: " + ex.getMessage());
            return false;
        }

        System.out.println("[GameSave] Dimuat dari " + getSavePath()
                + " (Hari ke-" + gm.getCurrentDay() + ")");
        return true;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  UTILITAS PUBLIK
    // ══════════════════════════════════════════════════════════════════════

    /** @return true jika file save.json ada */
    public static boolean adaSaveFile() {
        return Files.exists(Paths.get(SAVE_FILE));
    }

    /** Hapus file save — untuk New Game bersih */
    public static void hapusSave() {
        try {
            boolean ok = Files.deleteIfExists(Paths.get(SAVE_FILE));
            System.out.println(ok ? "[GameSave] File save dihapus."
                                  : "[GameSave] Tidak ada file save untuk dihapus.");
        } catch (IOException ex) {
            System.err.println("[GameSave] Gagal menghapus: " + ex.getMessage());
        }
    }

    /** @return path absolut save.json */
    public static String getSavePath() {
        return Paths.get(SAVE_FILE).toAbsolutePath().toString();
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPER — SERIALISASI
    // ══════════════════════════════════════════════════════════════════════

    /** Map<String, Integer> → JSON object string */
    private static String mapIntToJson(Map<String, Integer> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{\n");
        Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> e = it.next();
            sb.append("    \"").append(escJson(e.getKey())).append("\": ").append(e.getValue());
            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("  }");
        return sb.toString();
    }

    /** Map<String, Double> → JSON object string */
    private static String mapDoubleToJson(Map<String, Double> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder("{\n");
        Iterator<Map.Entry<String, Double>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Double> e = it.next();
            sb.append("    \"").append(escJson(e.getKey())).append("\": ").append(e.getValue());
            if (it.hasNext()) sb.append(",");
            sb.append("\n");
        }
        sb.append("  }");
        return sb.toString();
    }

    /** Jimat → JSON object string satu baris */
    private static String jimatToJson(Jimat j, boolean aktif) {
        return String.format(
            "{\"nama\": \"%s\", \"tipe\": \"%s\", \"power\": %s, \"aktifDiSlot\": %s}",
            escJson(j.getName()),
            j.getClass().getSimpleName(),
            j.getPower(),
            aktif
        );
    }

    private static String escJson(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPER — PARSING JSON MANUAL
    // ══════════════════════════════════════════════════════════════════════

    /** Ekstrak nilai angka bulat dari field top-level: "key": 123 */
    private static long extractLong(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?\\d+)");
        Matcher m = p.matcher(json);
        if (m.find()) return Long.parseLong(m.group(1));
        throw new NoSuchElementException("Key tidak ditemukan: " + key);
    }

    /** Ekstrak nilai desimal dari field top-level: "key": 123.45 */
    private static double extractDouble(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(-?[\\d.]+)");
        Matcher m = p.matcher(json);
        if (m.find()) return Double.parseDouble(m.group(1));
        throw new NoSuchElementException("Key tidak ditemukan: " + key);
    }

    /** Ekstrak string dari field top-level: "key": "nilai" */
    private static String extractString(String json, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) return m.group(1);
        return "PERSIAPAN";
    }

    /**
     * Ekstrak isi block objek JSON: "key": { ... }
     * Mengembalikan string isi di antara { dan } terluar.
     */
    private static String extractBlock(String json, String key) {
        int keyIdx = json.indexOf("\"" + key + "\"");
        if (keyIdx < 0) return "";
        int start = json.indexOf('{', keyIdx);
        if (start < 0) return "";
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '{') depth++;
            else if (c == '}') {
                depth--;
                if (depth == 0) return json.substring(start + 1, i);
            }
        }
        return "";
    }

    /**
     * Ekstrak isi array JSON: "key": [ ... ]
     * Mengembalikan string isi di antara [ dan ] terluar.
     */
    private static String extractArrayBlock(String json, String key) {
        int keyIdx = json.indexOf("\"" + key + "\"");
        if (keyIdx < 0) return "";
        int start = json.indexOf('[', keyIdx);
        if (start < 0) return "";
        int depth = 0;
        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '[') depth++;
            else if (c == ']') {
                depth--;
                if (depth == 0) return json.substring(start + 1, i);
            }
        }
        return "";
    }

    /**
     * Parse JSON object dengan nilai integer: { "Beras": 20, "Ayam": 15 }
     * → Map<String, Integer>
     */
    private static Map<String, Integer> parseMapInt(String block) {
        Map<String, Integer> result = new LinkedHashMap<>();
        if (block == null || block.isBlank()) return result;
        Pattern p = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(-?\\d+)");
        Matcher m = p.matcher(block);
        while (m.find()) result.put(m.group(1), Integer.parseInt(m.group(2)));
        return result;
    }

    /**
     * Parse JSON object dengan nilai double: { "Beras": 2000.0, "Ayam": 5000.0 }
     * → Map<String, Double>
     */
    private static Map<String, Double> parseMapDouble(String block) {
        Map<String, Double> result = new LinkedHashMap<>();
        if (block == null || block.isBlank()) return result;
        Pattern p = Pattern.compile("\"([^\"]+)\"\\s*:\\s*(-?[\\d.]+)");
        Matcher m = p.matcher(block);
        while (m.find()) result.put(m.group(1), Double.parseDouble(m.group(2)));
        return result;
    }

    /**
     * Parse array of jimat objects:
     * [ {"nama": "...", "tipe": "...", "power": 5.0, "aktifDiSlot": true}, ... ]
     * → List<Map<String, String>> (semua nilai sebagai String untuk fleksibilitas)
     */
    private static List<Map<String, String>> parseJimatArray(String arrayBlock) {
        List<Map<String, String>> result = new ArrayList<>();
        if (arrayBlock == null || arrayBlock.isBlank()) return result;

        // Pecah per objek { ... }
        int i = 0;
        while (i < arrayBlock.length()) {
            int start = arrayBlock.indexOf('{', i);
            if (start < 0) break;
            int depth = 0;
            int end   = start;
            for (int j = start; j < arrayBlock.length(); j++) {
                char c = arrayBlock.charAt(j);
                if (c == '{') depth++;
                else if (c == '}') {
                    depth--;
                    if (depth == 0) { end = j; break; }
                }
            }
            String objStr = arrayBlock.substring(start + 1, end);
            result.add(parseSimpleObj(objStr));
            i = end + 1;
        }
        return result;
    }

    /**
     * Parse satu JSON object flat (tanpa nested): semua nilai dijadikan String.
     * {"nama": "Jimat Pesona", "tipe": "Charming", "power": 5.0, "aktifDiSlot": true}
     */
    private static Map<String, String> parseSimpleObj(String objContent) {
        Map<String, String> map = new LinkedHashMap<>();
        // Match string value: "key": "value"
        Matcher ms = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"").matcher(objContent);
        while (ms.find()) map.put(ms.group(1), ms.group(2));
        // Match primitive value (number/boolean): "key": value
        Matcher mp = Pattern.compile("\"([^\"]+)\"\\s*:\\s*([^,}\"\\s]+)").matcher(objContent);
        while (mp.find()) map.putIfAbsent(mp.group(1), mp.group(2).trim());
        return map;
    }

    // ══════════════════════════════════════════════════════════════════════
    //  HELPER — REKONSTRUKSI OBJEK
    // ══════════════════════════════════════════════════════════════════════

    /** Buat objek Jimat berdasarkan tipe string */
    private static Jimat buatJimat(String tipe, String nama, double power) {
        return switch (tipe) {
            case "Charming"  -> new Charming();
            case "Cleaner"   -> new Cleaner();
            case "Security"  -> new Security();
            default          -> null;
        };
    }

    /** Pasang jimat langsung ke slot aktif di Restaurant */
    private static void pasangKeSlot(Restaurant resto, Jimat jimat) {
        if (jimat instanceof Charming || jimat instanceof Cleaner || jimat instanceof Security) {
            // Tambah dulu ke inventaris agar pakaiJimatDariInventori dapat menemukannya
            resto.tambahKeInventoriJimat(jimat);
            resto.pakaiJimatDariInventori(jimat);
        }
    }
}
