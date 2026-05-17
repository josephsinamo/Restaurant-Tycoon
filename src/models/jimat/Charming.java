package models.jimat;


public class Charming extends Jimat {

    public Charming() {
        super(
            "Charming " + (100 / 33), 
            (int)(Math.random() * 100) + 5, 
            ((int)(Math.random() * 100) + 5) * 10000 
        );
    }
}
