package J3_L5;

import java.util.concurrent.*;

public class MainClass {

    public static final int CARS_COUNT = 4;
    public static final int TUNNEL_CAPACITY = CARS_COUNT / 2;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(CARS_COUNT);
        final CyclicBarrier cbStart = new CyclicBarrier(CARS_COUNT + 1);    //Барьер для метода подготовки к гонкам
        final CountDownLatch cdlStart = new CountDownLatch(CARS_COUNT);    //защелка для мейнПотока на Старт
        final CountDownLatch cdlFinish = new CountDownLatch(CARS_COUNT);  //защелка для мейнПотока на Финиш
        final Semaphore smp = new Semaphore(TUNNEL_CAPACITY, true);  //семафор для тоннеля проходимость TUNNEL_CAPACITY, в порядке очереди

        race(executor, cbStart, cdlStart, cdlFinish, smp);

        //закрываем экзекьютор
        executor.shutdown();

    }

    private static void race(Executor executor, CyclicBarrier cbStart, CountDownLatch cdlStart, CountDownLatch cdlFinish, Semaphore smp) {
        System.err.println("ОБЪЯВЛЕНИЕ >>> ПОДГОТОВКА К ГОНКЕ <<< ОБЪЯВЛЕНИЕ");
        final Race race = new Race(new Road(60), new Tunnel(80, smp), new Road(40));
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cbStart, cdlStart, cdlFinish);
        }

        for (Car car : cars) {
            executor.execute(car);
        }

        wait(cdlStart); // ждем пока все участники подготовятся
        System.err.println("ОБЪЯВЛЕНИЕ >>> ГОНКА НАЧАЛАСЬ <<< ОБЪЯВЛЕНИЕ");
        System.out.println();

        try {
            cbStart.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        wait(cdlFinish); //ждем пока все участники финишируют
        System.err.println("ОБЪЯВЛЕНИЕ >>> ГОНКА ЗАКОНЧИЛАСЬ <<< ОБЪЯВЛЕНИЕ");

        //время заезда участников в мс
        for (Car car: cars) {
            System.out.print(car.getFinishTime());
        }
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




