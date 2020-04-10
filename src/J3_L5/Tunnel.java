package J3_L5;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private final Semaphore smp;

    public Tunnel(int length, Semaphore smp) {
        this.length = length;
        this.description = "Тоннель " + length + " метров";
        this.smp = smp; //ссылка на семафор
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                smp.acquire(); // просим разрешение на вход
                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
                smp.release(); //отпускаем замок на семафоре
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
