package Lesson5;

import java.util.Arrays;

public class Main {

    static final int SIZE = 10_000_000;
    static final int HALF = SIZE / 2;
    static float[] arr = new float[SIZE];
    static float[] arr1 = new float[SIZE];

    public static void main(String[] args) {

        Arrays.fill(arr, 1.0f);
        calcMethod1(arr);
        Arrays.fill(arr1, 1.0f);
        calcMethod2(arr1);

        System.out.println(Arrays.equals(arr, arr1));
    }

    //метод однопоточного расчета
    public static void calcMethod1(float[] arr) {

        long a = System.currentTimeMillis();
        calculation(arr, 0, SIZE);
        System.out.println("Estimated time (single thread), ms: " + (System.currentTimeMillis() - a));
    }

    //метод многопоточного расчета
    public static void calcMethod2(float[] arr) {

        float[] a1 = new float[HALF];
        float[] a2 = new float[HALF];

        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1, 0, HALF);
        System.arraycopy(arr, HALF, a2, 0, HALF);

        Thread thread1 =  new Thread(() -> calculation(a1, 0, HALF));
        Thread thread2 =  new Thread(() -> calculation(a2, HALF , HALF));

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
    public static void calculation(float[] arr, int shift, int length) {
        for (int i = 0; i < length; i++) {
            arr[i] = (float)(arr[i] * Math.sin(0.2f + (i + shift) / 5) * Math.cos(0.2f + (i + shift) / 5) * Math.cos(0.4f + (i + shift) / 2));
        }
    }
}
