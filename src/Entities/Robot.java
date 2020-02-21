package Entities;

import Interfaces.Obstacle;
import Interfaces.Overcomeable;

public class Robot implements Overcomeable {
    private String name;
    private int runDistance;
    private int jumpHeight;

    public Robot(String name, int runDistance, int jumpHeight) {
        this.name = name;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean jump(Obstacle wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Робот " + this.getName() + " перепрыгивает "
                + wall.getHeight() + " м." : "Робот " + this.getName() + " не смог перепрыгнуть " + wall.getHeight() + " м.");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean run(Obstacle distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Робот " + this.getName() + " пробежал "
                + distance.getLength() + " м." : "Робот " + this.getName() + " не смог пробежать " + distance.getLength() + " м.");
        return this.runDistance >= distance.getLength();
    }
}
