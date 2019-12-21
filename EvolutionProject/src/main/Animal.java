package main;

public class Animal {

    private Position position;
    private Genotype genotype;
    private int energy;
    private MapDirection direction;
    private IWorldMap map;

    public Animal(Position position, int energy, IWorldMap map) {
        this.energy = energy;
        this.genotype = new Genotype();
        this.map = map;
        this.direction = MapDirection.randomDirection();
        this.position = position;
    }

    public Animal(Animal parent1, Animal parent2, Position newPosition) {
        this.energy = parent1.energy / 4 + parent2.energy / 4;
        this.position = newPosition;
        this.map = parent1.map;
        this.direction = MapDirection.randomDirection();
        this.genotype = new Genotype(parent1.genotype, parent2.genotype);
    }

    /**
     * Gets position of animal.
     *
     * @return Position of animal.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Rotates animal in random direction according to genotype.
     */
    public void randomRotate() {
        int rotationPower = genotype.getRandomGene();
        direction = direction.rotate(rotationPower);
    }

    /**
     * Increases energy of animal.
     *
     * @param energyGain Amount of energy addition.
     */
    public void eat(int energyGain) {
        this.energy += energyGain;
    }

    /**
     * Sets position of animal.
     *
     * @param position Position to set to.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets energy of animal.
     *
     * @return Energy of animal.
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Gets direction of animal.
     *
     * @return direction of animal.
     */
    public MapDirection getDirection() {
        return direction;
    }

    /**
     * Checks if animal can copulate.
     *
     * @param energyRequired Energy required for animal to copulate.
     * @return true if animal can copulate.
     */
    public boolean canCopulate(int energyRequired) {
        return (energy >= energyRequired);
    }

    /**
     * Decreases energy of animal by fourth after copulating.
     */
    public void exhaustFromCopulating() {
        this.energy = this.energy * 3 / 4;
    }

    /**
     * Decreases energy of animal by set value after moving.
     *
     * @param moveEnergy Value to decrease by.
     */
    public void exhaustFromMoving(int moveEnergy) {
        this.energy -= moveEnergy;
    }

    /**
     * Shows animal as String.
     *
     * @return "a"
     */
    public String toString() {
        return "a";
    }
}
