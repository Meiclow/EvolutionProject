package main;

import java.util.*;

public class WorldMap implements IWorldMap {

    private int width;
    private int height;
    private int startEnergy;
    private int energyToMultiply;
    private int moveEnergy;
    private int plantEnergy;
    private double jungleRatio;
    private int jungleHeight;
    private int jungleWidth;

    private ArrayList<Animal> animalList;
    private ArrayList<Position> positionsWithElementInsideJungle;
    private ArrayList<Position> positionsWithoutElementInsideJungle;
    private ArrayList<Position> positionsWithElementOutsideJungle;
    private ArrayList<Position> positionsWithoutElementOutsideJungle;
    private Map<Position, Grass> grasses = new HashMap<>();
    private Map<Position, LinkedList<Animal>> animalPacks = new HashMap<>();

    /**
     * Changes position to be looped within map.
     * @param position Position to change.
     * @return Looped position.
     */
    private Position loopChange(Position position) {
        return (new Position(((position.getX()%width)+width)%width,((position.getY()%height)+height)%height));
    }

    public WorldMap(int width, int height, int startEnergy, int moveEnergy, int plantEnergy, double jungleRatio) {
        if(jungleRatio < 0 || jungleRatio > 1 || startEnergy < 0 || moveEnergy < 0 || plantEnergy < 0 || height < 0 || width < 0) {
            throw new IllegalArgumentException("World map argument(s) not in scope");
        }
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.energyToMultiply = startEnergy / 2;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.jungleRatio = jungleRatio;
        this.jungleHeight = (int) (jungleRatio * height);
        this.jungleWidth = (int) (jungleRatio * width);
        this.animalList = new ArrayList<>();
        this.grasses = new HashMap<>();
        this.animalPacks = new HashMap<>();
        this.positionsWithElementInsideJungle = new ArrayList<>();
        this.positionsWithElementOutsideJungle = new ArrayList<>();
        this.positionsWithoutElementInsideJungle = new ArrayList<>();
        for (int i = 0; i < jungleWidth; i++) {
            for (int j = 0; j < jungleHeight; j++) {
                positionsWithoutElementInsideJungle.add(new Position( i, j));
            }
        }
        this.positionsWithoutElementOutsideJungle = new ArrayList<>();
        for (int i = jungleWidth; i < width; i++) {
            for (int j = 0; j < jungleHeight; j++) {
                positionsWithoutElementOutsideJungle.add(new Position( i, j));
            }
        }
        for (int i = 0; i < jungleWidth; i++) {
            for (int j = jungleHeight; j < height; j++) {
                positionsWithoutElementOutsideJungle.add(new Position( i, j));
            }
        }
        for (int i = jungleWidth; i < width; i++) {
            for (int j = jungleHeight; j < height; j++) {
                positionsWithoutElementOutsideJungle.add(new Position( i, j));
            }
        }
    }

    @Override
    public boolean place(Animal animal) {
        Position animalPosition = animal.getPosition();
        boolean wasOccupied = isOccupied(animalPosition);
        if (!wasOccupied) {
            if(inJungle(animalPosition)) {
                positionsWithElementInsideJungle.add(animalPosition);
                positionsWithoutElementInsideJungle.remove(animalPosition);
            }
            else {
                positionsWithElementOutsideJungle.add(animalPosition);
                positionsWithoutElementOutsideJungle.remove(animalPosition);
            }
        }
        if (!animalList.contains(animal)) {
            animalList.add(animal);
        }
        addToPack(animal);
        return true;
    }

    @Override
    public boolean isOccupied(Position position) {
        return (objectAt(position) != null);
    }

    @Override
    public Object objectAt(Position position) {
        LinkedList<Animal> packAtPosition = animalPacks.get(position);
        if (packAtPosition != null) {
            return packAtPosition.getFirst();
        }
        else {
            return grasses.get(position);
        }
    }

    /**
     * Checks if position is inside the jungle.
     * @param position Position to check.
     * @return true if position is inside jungle.
     */
    private boolean inJungle(Position position) {
        return position.isContainedByUpperRight(new Position(jungleWidth-1, jungleHeight-1));
    }

    /**
     * Places grass on map.
     * @param grass Grass to place.
     */
    private void placeGrass(Grass grass) {
        Position grassPosition = grass.getPosition();
        if (isOccupied(grassPosition)) {
            throw new IllegalArgumentException("Can't place grass on occupied position");
        }
        grasses.put(grass.getPosition(), grass);
        if(inJungle(grassPosition)) {
            positionsWithElementInsideJungle.add(grassPosition);
            positionsWithoutElementInsideJungle.remove(grassPosition);
        }
        else {
            positionsWithElementOutsideJungle.add(grassPosition);
            positionsWithoutElementOutsideJungle.remove(grassPosition);
        }
    }

    /**
     * Adds animal to pack at its position and creates it if there isn't one already.
     * @param animal Animal to add to pack.
     */
    private void addToPack(Animal animal) {
        if (animal == null) {
            throw new IllegalArgumentException("Can't add null to pack");
        }
        Position animalPosition = animal.getPosition();
        LinkedList<Animal> packToAddTo = animalPacks.get(animalPosition);
        if (packToAddTo == null) {
            LinkedList<Animal> newPack = new LinkedList<>();
            animalPacks.put(animalPosition, newPack);
            newPack.add(animal);
        }
        else {
            packToAddTo.add(animal);
            packToAddTo.sort(new AnimalComparator());
        }
    }

    /**
     * Removes animal from map.
     * @param animal Animal to remove.
     * @param kill Tells if animal should be permanently removed from map.
     */
    private void removeAnimal(Animal animal, boolean kill) {
        if (animal == null) {
            throw new IllegalArgumentException("Can't remove animal being null");
        }
        Position animalPosition = animal.getPosition();
        LinkedList<Animal> packToRemoveFrom =animalPacks.get(animalPosition);
        if (packToRemoveFrom == null) {
            throw new IllegalArgumentException("Can't remove animal from non existing pack");
        }
        packToRemoveFrom.remove(animal);
        if (packToRemoveFrom.size() == 0) {
            animalPacks.remove(animalPosition);
            if (!isOccupied(animalPosition)) {
                if(inJungle(animalPosition)) {
                    positionsWithElementInsideJungle.remove(animalPosition);
                    positionsWithoutElementInsideJungle.add(animalPosition);
                }
                else {
                    positionsWithElementOutsideJungle.remove(animalPosition);
                    positionsWithoutElementOutsideJungle.add(animalPosition);
                }
            }
        }
        if (kill) {
            if (!animalList.contains(animal)) {
                throw new IllegalArgumentException("Can't kill animal which is not on the map");
            }
            animalList.remove(animal);
        }
    }

    /**
     * Removes grass from map.
     * @param grass Grass to remove.
     */
    private void removeGrass(Grass grass) {
        Position grassPosition = grass.getPosition();
        grasses.remove(grassPosition);
        if (!isOccupied(grassPosition)) {
            if (inJungle(grassPosition)) {
                positionsWithoutElementInsideJungle.add(grassPosition);
                positionsWithElementInsideJungle.remove(grassPosition);
            } else {
                positionsWithoutElementOutsideJungle.add(grassPosition);
                positionsWithElementOutsideJungle.remove(grassPosition);
            }
        }
    }

    /**
     * Kills animals with no energy.
     */
    private void clearStarved() {
        ArrayList<Animal> animalsToRemove = new ArrayList<>();
        for (int i = 0; i < animalList.size(); i++) {
            Animal animalToCheck =animalList.get(i);
            if (animalToCheck.getEnergy() <= 0) animalsToRemove.add(animalToCheck);
        }
        while (animalsToRemove.size() > 0) {
            removeAnimal(animalsToRemove.get(0), true);
            animalsToRemove.remove(animalsToRemove.get(0));
        }
    }

    /**
     * Moves animals on the map according to their genotypes.
     */
    private void moveAnimals() {
        ArrayList<Animal> animalsToMove = new ArrayList<>();
        animalsToMove.addAll(animalList);
        for (Animal animal : animalsToMove) {
            removeAnimal(animal, false);
            animal.randomRotate();
            animal.exhaustFromMoving(moveEnergy);
            animal.setPosition(loopChange(animal.getPosition().add(animal.getDirection().toUnitVector())));
            place(animal);
        }
    }

    /**
     * Gives food divided by number of strongest animals in pack.
     * @param eatingPack Pack to feed.
     */
    private void Feed(LinkedList<Animal> eatingPack) {
        if (eatingPack != null) {
            int maxEnergy = eatingPack.getFirst().getEnergy();
            int strongestEndIndex = 0;
            for (int i = 1; i < eatingPack.size() && eatingPack.get(i).getEnergy() == maxEnergy; i++) {
                strongestEndIndex++;
            }
            int energyGain = plantEnergy/(1+strongestEndIndex);
            for (int i = 0; i <= strongestEndIndex; i++) {
                eatingPack.get(i).eat(energyGain);
            }
        }
    }

    /**
     * Give food to all packs and remove grass from every position with both pack and grass.
     */
    private void eatGrass() {
        ArrayList<Grass> eatenGrass = new ArrayList<>();
        for (Map.Entry<Position, Grass> grassEntry : grasses.entrySet()) {
            LinkedList<Animal> eatingPack = animalPacks.get(grassEntry.getKey());
            if (eatingPack != null) {
                eatenGrass.add(grassEntry.getValue());
                Feed(eatingPack);
            }
        }
        for (Grass grass : eatenGrass) {
            removeGrass(grass);
        }
    }

    /**
     * Gives position of random free neighbouring position if there's any. If not give random neighbouring position.
     * @param position Position of which searches for neighbours.
     * @return Position of free random neighbour if there's any. If not, random neighbour.
     */
    private Position randomNeighbour(Position position) {
        ArrayList<Position> freeNeighbours = new ArrayList<>();
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                Position neighbour = new Position(i + position.getX(),j + position.getY());
                if ((!neighbour.equals(position))&&!isOccupied(neighbour)) {
                    freeNeighbours.add(neighbour);
                }
            }
        }
        if (freeNeighbours.size() == 0) {
            MapDirection neighbourDirection = MapDirection.randomDirection();
            return loopChange(position.add(neighbourDirection.toUnitVector()));
        }
        else {
            Random rng = new Random();
            return loopChange(freeNeighbours.get(rng.nextInt(freeNeighbours.size())));
        }
    }

    /**
     * Makes all allowed animals copulate.
     */
    private void copulate() {
        ArrayList<Animal> newborns = new ArrayList<>();
        for (Map.Entry<Position, LinkedList<Animal>> packEntry : animalPacks.entrySet()) {
            LinkedList<Animal> copulatingPack = packEntry.getValue();
            if (copulatingPack.size() >= 2) {
                Animal parent1 = copulatingPack.get(0);
                Animal parent2 = copulatingPack.get(1);
                if (parent1.canCopulate(energyToMultiply) && parent2.canCopulate(energyToMultiply)) {
                    Animal child = new Animal(parent1, parent2, randomNeighbour(parent1.getPosition()));
                    parent1.exhaustFromCopulating();
                    parent2.exhaustFromCopulating();
                    newborns.add(child);
                }
            }
        }
        for (Animal newborn : newborns) {
            place(newborn);
        }
    }

    /**
     * Generates 1 grass in jungle if possible and 1 grass outside jungle if possible.
     */
    private void generateGrass() {
        Random rng = new Random();
        int freeSpacesInsideJungle = positionsWithoutElementInsideJungle.size();
        if(freeSpacesInsideJungle != 0) {
            Position newGrassInJunglePosition = positionsWithoutElementInsideJungle.get(rng.nextInt(freeSpacesInsideJungle));
            Grass newGrassInJungle = new Grass(newGrassInJunglePosition);
            placeGrass(newGrassInJungle);
        }
        int freeSpacesOutsideJungle = positionsWithoutElementOutsideJungle.size();
        if(freeSpacesOutsideJungle != 0) {
            Position newGrassOutJunglePosition = positionsWithoutElementOutsideJungle.get(rng.nextInt(freeSpacesOutsideJungle));
            Grass newGrassOutJungle = new Grass(newGrassOutJunglePosition);
            placeGrass(newGrassOutJungle);
        }
    }

    /**
     * Generates first animals on the map.
     * @param progenitorsAmount Amount of animals to generate.
     */
    private void generateProgenitors(int progenitorsAmount) {
        if(progenitorsAmount < 0 || progenitorsAmount > width*height) {
            throw new IllegalArgumentException("Amount of animals at beginning not in scope");
        }
        Random rng = new Random();
        for (int i = 0; i < progenitorsAmount; i++) {
            int freeSpaces = positionsWithoutElementOutsideJungle.size() + positionsWithoutElementInsideJungle.size();
            int freeSpacesInJungle = positionsWithoutElementInsideJungle.size();
            int freeSpacesOutJungle = positionsWithoutElementOutsideJungle.size();
            int randomIndex = rng.nextInt(freeSpaces);
            if (randomIndex < freeSpacesInJungle) {
                Position progenitorPosition = positionsWithoutElementInsideJungle.get(randomIndex);
                Animal progenitor = new Animal(progenitorPosition, startEnergy, this);
                place(progenitor);
            }
            else {
                randomIndex -= freeSpacesInJungle;
                Position progenitorPosition = positionsWithoutElementOutsideJungle.get(randomIndex);
                Animal progenitor = new Animal(progenitorPosition, startEnergy, this);
                place(progenitor);
            }
        }
    }

    @Override
    public void init(int progenitorsAmount) {
        generateProgenitors(progenitorsAmount);
    }

    @Override
    public void cycle() {
        //System.out.println("clear");
        clearStarved();
        // System.out.println("move");
        moveAnimals();
        // System.out.println("eat");
        eatGrass();
        //System.out.println("copulate");
        copulate();
        // System.out.println("gengrass");
        generateGrass();
    }

}