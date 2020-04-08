package J3_L5;

import java.util.concurrent.*;

public class MainClass {

    public static final int CARS_COUNT = 4;
    public static final int TUNNEL_CAPACITY = 2;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(CARS_COUNT);
        CyclicBarrier cbStart = new CyclicBarrier(4);              //Барьер для метода подготовки к гонкам
        final CountDownLatch cdlStart = new CountDownLatch(CARS_COUNT);   //защелка для мейнПотока на Старт
        final CountDownLatch cdlFinish = new CountDownLatch(CARS_COUNT);  //защелка для мейнПотока на Финиш
        final Semaphore smp = new Semaphore(TUNNEL_CAPACITY, true);  //семафор для тоннеля проходимость TUNNEL_CAPACITY, в порядке очереди

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Race race = new Race(new Road(60), new Tunnel(80, smp), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cbStart, cdlStart, cdlFinish);
        }

        for (int i = 0; i < cars.length; i++) {
            executor.execute(cars[i]);
        }

        wait(cdlStart); // ждем пока все 4 участника подготовятся
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        wait(cdlFinish); //ждем пока все 4 участника финишируют
        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

        //время заезда участников в мс
        for (Car car: cars) {
            System.out.print(car.getFinishTime());
        }

        //закрываем экзекьютор
        executor.shutdown();

    }


    //метод ожидания защелки
    private static void wait(CountDownLatch cdl) {
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}




