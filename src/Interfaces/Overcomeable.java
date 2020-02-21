package Interfaces;

import Entities.Distance;
import Entities.Wall;

public interface Overcomeable {
    boolean overcome(Wall wall);
    boolean overcome(Distance distance);
}
