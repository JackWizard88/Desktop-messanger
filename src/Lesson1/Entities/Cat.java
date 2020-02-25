package Lesson1.Entities;

import Lesson1.Interfaces.Obstacle;
import Lesson1.Interfaces.Overcomeable;

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
        if (this.jumpHeight >= wall.getHeight()) {
            System.out.println("Кот " + this.getName() + " перепрыгивает "
                    + wall.getHeight() + " м.");
        } else {
            System.out.println("Кот " + this.getName() + " не смог перепрыгнуть " + wall.getHeight() + " м.");
        }
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean run(Obstacle distance) {
        if (this.runDistance >= distance.getLength()) {
            System.out.println("Кот " + this.getName() + " пробежал "
                    + distance.getLength() + " м.");
        } else {
            System.out.println("Кот " + this.getName() + " не смог пробежать " + distance.getLength() + " м.");
        }
        return this.runDistance >= distance.getLength();
    }

    public String getName() {
        return name;
    }
}
