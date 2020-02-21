package Entities;

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
    public boolean overcome(Wall wall) {
        System.out.println((this.jumpHeight >= wall.getHeight()) ? "Кошка перепрыгивает" : "Кошка не смогла перепрыгнуть");
        return this.jumpHeight >= wall.getHeight();
    }

    @Override
    public boolean overcome(Distance distance) {
        System.out.println((this.runDistance >= distance.getLength()) ? "Кошка пробежала" : "Кошка не смогла пробежать");
        return this.runDistance >= distance.getLength();
    }
}
