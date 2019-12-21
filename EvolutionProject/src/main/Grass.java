package main;

public class Grass {

    private Position position;

    public Grass(Position position) {
        this.position = position;
    }

    /**
     * Gets position of grass.
     *
     * @return Position of grass.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Shows grass as String.
     *
     * @return "g"
     */
    public String toString() {
        return "g";
    }
}
