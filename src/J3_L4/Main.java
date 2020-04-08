package J3_L4;

public class Main {

    private static final Object lock = new Object();
    private static char currentLetter = 'C';

    public static void main(String[] args) throws InterruptedException {

            Thread t1 = new Thread(() -> printLetter('A', 'C'));
            Thread t2 = new Thread(() -> printLetter('B', 'A'));
            Thread t3 = new Thread(() -> printLetter('C', 'B'));

            t1.start();
            t2.start();
            t3.start();

            t1.join();
            t2.join();
            t3.join();

    }

    public static void printLetter(char letter, char lastLetter) {

        for (int i = 0; i < 5; i++) {
            synchronized (lock) {
                while (currentLetter != lastLetter) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(letter);
                currentLetter = letter;
                lock.notifyAll();
            }
        }
    }

}
