package main;

import java.util.Random;

public enum MapDirection {

    NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;

    /**
     * @return Next direction clockwise.
     */
    public MapDirection next() {
        switch (this) {
            case NORTH:
                return NORTHEAST;
            case NORTHEAST:
                return EAST;
            case EAST:
                return SOUTHEAST;
            case SOUTHEAST:
                return SOUTH;
            case SOUTH:
                return SOUTHWEST;
            case SOUTHWEST:
                return WEST;
            case WEST:
                return NORTHWEST;
            default:
                return NORTH;
        }
    }

    /**
     * Rotates direction clockwise.
     *
     * @param rotationPower How many times rotate the direction clockwise.
     * @return New direction after rotation.
     */
    public MapDirection rotate(int rotationPower) {
        if (rotationPower > 7 || rotationPower < 0) {
            throw new IllegalArgumentException("rotationPower not in scope");
        }
        MapDirection direction = this;
        for (int i = 0; i < rotationPower; i++) {
            direction = direction.next();
        }
        return direction;
    }

    /**
     * Changes direction into 'wersor'(not really).
     *
     * @return 'Wersor' of the direction.
     */
    public Position toUnitVector() {
        switch (this) {
            case NORTH:
                return new Position(0, 1);
            case NORTHEAST:
                return new Position(1, 1);
            case EAST:
                return new Position(1, 0);
            case SOUTHEAST:
                return new Position(1, -1);
            case SOUTH:
                return new Position(0, -1);
            case SOUTHWEST:
                return new Position(-1, -1);
            case WEST:
                return new Position(-1, 0);
            default:
                return new Position(-1, 1);
        }
    }

    /**
     * Creates random direction.
     *
     * @return Random direction.
     */
    public static MapDirection randomDirection() {
        Random rng = new Random();
        switch (rng.nextInt(8)) {
            case 0:
                return NORTH;
            case 1:
                return NORTHEAST;
            case 2:
                return EAST;
            case 3:
                return SOUTHEAST;
            case 4:
                return SOUTH;
            case 5:
                return SOUTHWEST;
            case 6:
                return WEST;
            default:
                return NORTHWEST;
        }
    }
}
