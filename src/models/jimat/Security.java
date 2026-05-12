package models.jimat;

/** Jimat keamanan; satu slot {@link Security} terpasang per restoran. */
public class Security extends Jimat {

    public Security(String name, int kekuatanPerUnitPembelian) {
        super(name, kekuatanPerUnitPembelian);
    }

    @Override
    protected Jimat buatInstanceSalinan() {
        return new Security(getName(), getKekuatanPerUnitPembelian());
    }
}
