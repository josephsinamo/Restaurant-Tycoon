package controller;

import event.disaster.*;
import event.festival.*;
import models.items.amulet.*;
import models.items.talisman.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface GameEvent {
    void execute();
}

class EventManager {
    double[] fractions = {0.5, 0.33, 0.25, 0.2};
    Random random = new Random();
    int iAcak = random.nextInt(fractions.length);

    private List<GameEvent> disaster;
    private List<GameEvent> festival;
    private List<GameEvent> items;

    public EventManager(){
        this.disaster = new ArrayList<>();
        this.festival = new ArrayList<>();
        this.items = new ArrayList<>();

        disaster.add(new Rats());//bahan baku berkurang sebanyak 1/4 atau 1/3 atau 1/2 atau 1/5
        disaster.add(new Storm());//pelanggan datang hanya 1/4 atau 1/3 atau 1/2 atau 1/5
        disaster.add(new SuperStorm());//tidak ada pelanggan datang

        festival.add(new FoodParty());//harga menu naik 2x atau 1,5x
        festival.add(new GoldenDay());//semua customer ngasih tips randomize
        festival.add(new MarketDay());//harga bahan baku turun 1/4 atau 1/3 atau 1/2 atau 1/5

        items.add(new Security());//mengurangi change orang kabur randomize
        items.add(new Charming());//menambah change pelanggan memberi tip randomize
        items.add(new Cleaner());//mengurangi change tikus datang
    }

    public void Event(int[] item){
        if (!disaster.isEmpty()) {
            int iDisaster = random.nextInt(disaster.size());
            disaster.get(iDisaster).execute();
        }

        if(random.nextInt(100)<=25){
            int iFestival = random.nextInt(festival.size());
            festival.get(iFestival).execute();
        }

        for(int i=0; i<item.length; i++){
            if(item[i] == 1){
                items.get(i).execute();
            }
        }
    }
}
