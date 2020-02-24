import java.util.ArrayList;

import Entities.Cat;
import Entities.Robot;
import Entities.Human;
import Entities.Wall;
import Entities.Distance;
import Interfaces.Obstacle;
import Interfaces.Overcomeable;

public class Main {
    public static void main(String[] args) {
        ArrayList<Overcomeable> participants = new ArrayList();
        ArrayList<Obstacle> obstacles = new ArrayList();

        participants.add(new Cat("Barsik", 250, 2));
        participants.add(new Human("Boris", 100, 1));
        participants.add(new Robot("RoboOne", 500, 3));
        participants.add(new Cat("Murzik", 200, 2));
        participants.add(new Robot("RoboTwo", 650, 5));
        participants.add(new Human("Ivan", 500, 2));

        obstacles.add(new Distance(200));
        obstacles.add(new Wall(1));
        obstacles.add(new Distance(150));
        obstacles.add(new Wall(2));
        obstacles.add(new Distance(150));
        obstacles.add(new Wall(3));

        for (Overcomeable participant : participants) {
            for (Obstacle obstacle : obstacles) {
                if (!obstacle.performAction(participant)) {
                    System.out.println(participant.getName() + " сходит с дистанции...");
                    break;
                }
            }
            System.out.println(System.lineSeparator());
        }
    }
}
