package J3_L5;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {

    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier cbStart;
    private CountDownLatch cdlStart, cdlFinish;
    private long startTime;

    public String getFinishTime() {
        return finishTime;
    }

    private String finishTime;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cb1, CountDownLatch cdl1, CountDownLatch cdl2) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        cbStart = cb1;
        cdlStart = cdl1;
        cdlFinish = cdl2;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
            cdlStart.countDown(); //декремент счетчика защелки Старта
            cbStart.await();      //барьер ожидания остальных участников
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        long raceTime = System.currentTimeMillis() - this.startTime;
        finishTime = String.format("Время заедза %s: %s мс\n", this.getName(), raceTime);
        cdlFinish.countDown();  //декремент счетчика защелки Финиша
    }
}