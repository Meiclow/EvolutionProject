package main;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that Vector2d and MoveDirection classes are defined.
 *
 * @author apohllo
 */
public interface IWorldMap {

    /**
     * Place a element on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the element was placed. The element cannot be placed if the map is already occupied by element of the same class.
     */
    boolean place(Animal animal);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Position position);

    /**
     * Return an object at a given position.
     *
     * @param position The position of the object.
     * @return Object or null if the position is not occupied.
     */
    Object objectAt(Position position);

    /**
     * Initialize the map
     *
     * @param progenitorsAmount Amount of animals to place at the beginning of simulation.
     */
    void init(int progenitorsAmount);

    /**
     * Proceeds through days of simulation.
     */
    void cycle();
}
