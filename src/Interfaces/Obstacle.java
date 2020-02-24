package Interfaces;

import Entities.Distance;
import Entities.Wall;

public interface Obstacle {
    boolean performAction(Overcomeable participant);
    int getHeight();
    int getLength();
}
