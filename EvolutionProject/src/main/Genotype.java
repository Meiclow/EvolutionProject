package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genotype {

    private int[] genome = new int[32];

    public int getRandomGene() {
        Random rng = new Random();
        return genome[rng.nextInt(32)];
    }

    /**
     * Creates random genome.
     */
    public Genotype() {
        Random rng = new Random();
        for (int i = 0; i < 32; i++) {
            this.genome[i] = rng.nextInt(8);
        }
        this.mutate();
    }

    /**
     * Creates genome inherited from parents.
     *
     * @param parent1 First parent to inherit from.
     * @param parent2 Second parent to inherit from.
     */
    public Genotype(Genotype parent1, Genotype parent2) {
        Random rng = new Random();
        boolean doesParent1GiveOneSegment = rng.nextBoolean();
        int breaker1, breaker2;
        breaker1 = rng.nextInt(32);
        do {
            breaker2 = rng.nextInt(32);
        }
        while (breaker1 != breaker2);
        if (breaker1 > breaker2) {
            breaker1 = breaker2;
        }
        int[] child = new int[32];
        if (doesParent1GiveOneSegment) {
            for (int i = 0; i < breaker1; i++) {
                child[i] = parent1.genome[i];
            }
            for (int i = breaker1 + 1; i < 32; i++) {
                child[i] = parent2.genome[i];
            }
        } else {
            for (int i = 0; i < breaker1; i++) {
                child[i] = parent2.genome[i];
            }
            for (int i = breaker1 + 1; i < 32; i++) {
                child[i] = parent1.genome[i];
            }
        }
        this.genome = child;
        this.mutate();
    }

    /**
     * Checks if genome contains all directions and if not, repairs it until correct.
     */
    private void mutate() {
        int[] check = new int[8];
        for (int i = 0; i < 8; i++) {
            check[i] = 0;
        }
        for (int i = 0; i < 32; i++) {
            check[genome[i]]++;
        }
        List<Integer> zeroes = new ArrayList<>();
        List<Integer> moreThanTwos = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            if (check[i] == 0) {
                zeroes.add(i);
            } else if (check[i] > 1) {
                moreThanTwos.add(i);
            }
        }
        if (zeroes.isEmpty()) return;
        Random rng = new Random();
        int randomFromMoreThanTwos = moreThanTwos.get(rng.nextInt(moreThanTwos.size()));
        int index = 0;
        int valueToChange = zeroes.get(0);
        while (genome[index] != randomFromMoreThanTwos) index++;
        genome[index] = valueToChange;
        mutate();
    }

}