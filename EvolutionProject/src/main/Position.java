package main;

public class Position {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return this.x * 31 + this.y * 17;
    }

    /**
     * Checks if two positions are the same.
     *
     * @param other Position to compare.
     * @return True if positions are the same.
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Position)) return false;
        Position another = (Position) other;
        return (this.x == another.x && this.y == another.y);
    }

    /**
     * Adds positions.
     *
     * @param other Position to add.
     * @return New position after addition.
     */
    public Position add(Position other) {
        return new Position((this.x + other.x), (this.y + other.y));
    }

    /**
     * Checks if position is inside rectangle with vertexes is (0,0) and position given by parameter, with sides parallel to axises
     *
     * @param upperRight Position of second vertex of rectangle
     * @return true if position is contained
     */
    public boolean isContainedByUpperRight(Position upperRight) {
        if (this.x > upperRight.x || this.y > upperRight.y) return false;
        return true;
    }

    /**
     * Gets x of position.
     *
     * @return x of position.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets y of position.
     *
     * @return y of position.
     */
    public int getY() {
        return this.y;
    }

}
