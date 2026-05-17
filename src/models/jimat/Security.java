package models.jimat;

public class Security extends Jimat {

    public Security() {
        super(
            "Security " + (100 / 33),
            (int)(Math.random() * 100) + 5,
            ((int)(Math.random() * 100) + 5) * 10000
        );
    }

    @Override
    public boolean cegahPembeliKabur() {
        return Math.random() * 100 < getPower();
    }
}
