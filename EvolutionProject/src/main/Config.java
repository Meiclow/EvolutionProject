package main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class Config {

    private int width;
    private int height;
    private double jungleRatio;
    private int moveEnergy;
    private int plantEnergy;
    private int startEnergy;
    private int startAnimals;

    public Config() {
        Object obj = null;
        try {
            obj = new JSONParser().parse(new FileReader("config.json"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONObject jo = (JSONObject) obj;

        width =  Integer.parseInt((String) jo.get("width"));
        height = Integer.parseInt((String) jo.get("height"));
        jungleRatio = Double.parseDouble((String) jo.get("jungleRatio"));
        moveEnergy = Integer.parseInt((String) jo.get("moveEnergy"));
        plantEnergy = Integer.parseInt((String) jo.get("plantEnergy"));
        startAnimals = Integer.parseInt((String) jo.get("startAnimals"));
        startEnergy = Integer.parseInt((String) jo.get("startEnergy"));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public double getJungleRatio() {
        return jungleRatio;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getStartAnimals() {
        return startAnimals;
    }

    public int getStartEnergy() {
        return startEnergy;
    }
}
