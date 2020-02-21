package Entities;

import Interfaces.Overcomeable;

public class Human implements Overcomeable {
    private String name;
    private int runDistance;
    private int jumpHeight;

    public Human(String name, int runDistance, int jumpHeight) {
        this.name = name;
        this.runDistance = runDistance;
        this.jumpHeight = jumpHeight;
    }

    @Override
    public boolean overcome(Wall wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Человек перепрыгивает" : "Человек не смог перепрыгнуть");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean overcome(Distance distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Человек пробежал" : "Человек не смог перепрыгнуть");
        return this.runDistance >= distance.getLength();
    }
}
