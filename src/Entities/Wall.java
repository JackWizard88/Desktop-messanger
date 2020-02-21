package Entities;

import Entities.Obstacle;

public class Wall extends Obstacle {
    private int id;
    private int height;
    private static int counter;

    public Wall(int height) {
        this.id = ++counter;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }
}
