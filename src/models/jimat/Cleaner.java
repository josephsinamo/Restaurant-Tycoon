package models.jimat;

/** Jimat kebersihan; satu slot {@link Cleaner} terpasang per restoran. */
public class Cleaner extends Jimat {

    public Cleaner(String name, int kekuatanPerUnitPembelian) {
        super(name, kekuatanPerUnitPembelian);
    }

    @Override
    protected Jimat buatInstanceSalinan() {
        return new Cleaner(getName(), getKekuatanPerUnitPembelian());
    }
}
