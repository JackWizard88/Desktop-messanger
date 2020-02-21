package Entities;

import Interfaces.Obstacle;
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
    public boolean jump(Obstacle wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Человек " + this.getName() + " перепрыгивает "
                + wall.getHeight() + " м." : "Человек " + this.getName() + " не смог перепрыгнуть " + wall.getHeight() + " м.");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean run(Obstacle distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Человек " + this.getName() + " пробежал "
                + distance.getLength() + " м." : "Человек " + this.getName() + " не смог пробежать " + distance.getLength() + " м.");
        return this.runDistance >= distance.getLength();
    }

    public String getName() {
        return name;
    }

    public int getJumpHeight() {
        return jumpHeight;
    }

    public int getRunDistance() {
        return runDistance;
    }
}
