package models.jimat;


public class Charming extends Jimat {

    public Charming() {
        super(
            "Charming " + (100 / 33),
            (int)(Math.random() * 100) + 5,
            ((int)(Math.random() * 100) + 5) * 10000
        );
    }

    @Override
    public double hitungTips(double hargaMenu) {
        if (Math.random() * 100 < getPower()) {
            double persen = (5 + Math.random() * 15) / 100.0;
            return hargaMenu * persen;
        }
        return 0;
    }
}
