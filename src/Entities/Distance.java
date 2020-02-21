package Entities;

public class Distance extends Obstacle {
    private int id;
    private int length;
    private static int counter;

    public Distance(int length) {
        this.id = ++counter;
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }

}
