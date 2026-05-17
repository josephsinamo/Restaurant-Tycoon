package models.jimat;

public class Cleaner extends Jimat {

    public Cleaner() {
        super(
            "Cleaner " + (100 / 33),
            (int)(Math.random() * 100) + 5,
            ((int)(Math.random() * 100) + 5) * 10000
        );
    }

    @Override
    public boolean cegahTikus() {
        return Math.random() * 100 < getPower();
    }
}
