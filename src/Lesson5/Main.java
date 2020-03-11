package Lesson5;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        calcMethod1();
        calcMethod2();
    }

    //метод однопоточного расчета
    public static void calcMethod1() {
        final int SIZE = 10000000;
        float[] arr = new float[SIZE];

        Arrays.fill(arr, 1f);
        long a = System.currentTimeMillis();

        calculation(arr);

        System.out.println("Estimated time (single thread), ms: " + (System.currentTimeMillis() - a));
    }

    //метод многопоточного расчета
    public static void calcMethod2() {
        final int SIZE = 10000000;
        final int HALF = SIZE / 2;
        float[] arr = new float[SIZE];
        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];

        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1, 0, HALF);
        System.arraycopy(arr, HALF, a2, 0, HALF);

        Thread thread1 =  new Thread(() -> calculation(a1));
        Thread thread2 =  new Thread(() -> calculation(a2));

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(a1, 0, arr, 0, HALF);
        System.arraycopy(a2, 0, arr, HALF, HALF);

        System.out.println("Estimated time (multithreading), ms: " + (System.currentTimeMillis() - a));

    }

    //метод калькуляций по формуле из ТЗ
    public static void calculation(float[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
    }
}
