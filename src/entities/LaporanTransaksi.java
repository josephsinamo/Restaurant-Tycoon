package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Ringkasan satu kunjungan pelanggan untuk log UI. */
public class LaporanTransaksi {

    private final int jumlahOrang;
    private final List<String> itemDibeli;
    private final double totalBayar;
    private final boolean kabur;

    public LaporanTransaksi(int jumlahOrang, List<String> itemDibeli, double totalBayar, boolean kabur) {
        this.jumlahOrang = jumlahOrang;
        this.itemDibeli = new ArrayList<>(itemDibeli);
        this.totalBayar = totalBayar;
        this.kabur = kabur;
    }

    public int getJumlahOrang() { return jumlahOrang; }
    public boolean isKabur() { return kabur; }
    public double getTotalBayar() { return totalBayar; }
    public List<String> getItemDibeli() { return Collections.unmodifiableList(itemDibeli); }

    public String toLogBaris(int nomorPelanggan) {
        StringBuilder sb = new StringBuilder();
        sb.append("Pelanggan #").append(nomorPelanggan);
        if (jumlahOrang > 1) {
            sb.append(" (").append(jumlahOrang).append(" org)");
        }
        sb.append(" — ");
        if (itemDibeli.isEmpty()) {
            sb.append("tidak ada menu terpenuhi");
        } else {
            sb.append(String.join(", ", itemDibeli));
        }
        if (kabur) {
            sb.append(" [KABUR, tidak bayar]");
        } else {
            sb.append(" | Bayar: Rp ").append(String.format("%.0f", totalBayar));
        }
        return sb.toString();
    }
}
