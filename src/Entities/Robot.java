package Entities;

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
    public boolean overcome(Wall wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Робот перепрыгивает" : "Робот не смог перепрыгнуть");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean overcome(Distance distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Робот пробежал" : "Робот не смог пробежать");
        return this.runDistance >= distance.getLength();
    }
}
