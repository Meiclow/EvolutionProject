package main;

import java.util.Comparator;
import java.util.Random;

public class AnimalComparator implements Comparator {

    @Override
    public int compare(Object object1, Object object2) {
        if (!(object1 instanceof Animal && object2 instanceof Animal)) return 0;
        int energy1 = ((Animal) object1).getEnergy();
        int energy2 = ((Animal) object2).getEnergy();
        if (energy1 == energy2) {
            Random rng = new Random();
            if (rng.nextInt(1) == 0) return 1;
            else return -1;
        }
        else if (energy1 > energy2) return -1;
        else return 1;
    }
}
