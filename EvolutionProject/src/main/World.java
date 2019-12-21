package main;


public class World {

    public static void main(String args[]) {

        Config config = new Config();

        WorldMap map = new WorldMap(config.getWidth(), config.getHeight(), config.getStartEnergy(), config.getMoveEnergy(), config.getPlantEnergy(), config.getJungleRatio());
        map.init(config.getStartAnimals());
        MapVisualizer visualizer = new MapVisualizer(map);
        Position lowerLeft = new Position(0, 0);
        Position upperRight = new Position(config.getWidth() - 1, config.getHeight() - 1);
        System.out.println(visualizer.draw(lowerLeft, upperRight));

        while (true) {
            map.cycle();
            System.out.println(visualizer.draw(lowerLeft, upperRight));
        }

    }

}
