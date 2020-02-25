package Lesson1.Entities;

import Lesson1.Interfaces.Obstacle;
import Lesson1.Interfaces.Overcomeable;

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
        if (this.jumpHeight >= wall.getHeight()) {
            System.out.println("Робот " + this.getName() + " перепрыгивает "
                    + wall.getHeight() + " м.");
        } else {
            System.out.println("Робот " + this.getName() + " не смог перепрыгнуть " + wall.getHeight() + " м.");
        }
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean run(Obstacle distance) {
        if (this.runDistance >= distance.getLength()) {
            System.out.println("Робот " + this.getName() + " пробежал "
                    + distance.getLength() + " м.");
        } else {
            System.out.println("Робот " + this.getName() + " не смог пробежать " + distance.getLength() + " м.");
        }
        return this.runDistance >= distance.getLength();
    }
}
