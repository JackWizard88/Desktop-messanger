package Entities;

import Interfaces.Obstacle;
import Interfaces.Overcomeable;

public class Wall implements Obstacle {
    private int id;
    private int height;
    private static int counter;

    public Wall(int height) {
        this.id = ++counter;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean performAction(Overcomeable participant) {
        return participant.jump(this);
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getLength() {
        return 0;
    }
}
