package models.jimat;

/** Jimat keamanan; satu slot {@link Security} terpasang per restoran. */
public class Security extends Jimat {

    public Security() {
            super(
            "Security " + (100 / 33), 
            (int)(Math.random() * 100) + 5, 
            ((int)(Math.random() * 100) + 5) * 10000 
        );
    }

}
