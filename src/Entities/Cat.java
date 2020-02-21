package Entities;

import Interfaces.Obstacle;
import Interfaces.Overcomeable;

public class Cat implements Overcomeable {
    private String name;
    private int runDistance;
    private int jumpHeight;

    public Cat(String name, int runDistance, int jumpHeight) {
        this.name = name;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
    }

    @Override
    public boolean jump(Obstacle wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Кот " + this.getName() + " перепрыгивает "
                + wall.getHeight() + " м." : "Кот " + this.getName() + " не смог перепрыгнуть " + wall.getHeight() + " м.");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean run(Obstacle distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Кот " + this.getName() + " пробежал "
                + distance.getLength() + " м." : "Кот " + this.getName() + " не смог пробежать " + distance.getLength() + " м.");
        return this.runDistance >= distance.getLength();
    }

    public String getName() {
        return name;
    }
}
