package models.jimat;

/** Jimat kebersihan; satu slot {@link Cleaner} terpasang per restoran. */
public class Cleaner extends Jimat {

    public Cleaner() {
            super(
            "Cleaner " + (100 / 33), 
            (int)(Math.random() * 100) + 5, 
            ((int)(Math.random() * 100) + 5) * 10000 
        );
    }
}
