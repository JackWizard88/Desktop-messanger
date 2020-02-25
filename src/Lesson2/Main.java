package Lesson2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Для заполнения массива вручную введите 1. \nИначе чтобы использовать тестовый массив введите 0");
        int k,l; // размерности массива
        ArrayList<ArrayList<String>> arrayOfArrays = new ArrayList<>();


        try {
            int x = Integer.parseInt(reader.readLine());
            switch (x) {
                case (1) :
                    System.out.println("Введите размерность массива (два числа через пробел)");
                    k = reader.read();
                    l = reader.read();

                    for (int i = 0; i < k; i++) {
                        ArrayList<String> array = new ArrayList<>();
                        for (int j = 0; j < l; j++){
                            System.out.printf("Элемент %s,%s", i, j);
                            array.add(reader.readLine());
                            System.out.println();
                        }
                        arrayOfArrays.add(array);
                    }
                    break;
                case (0) :
                    arrayOfArrays = {{"1", "2", "3"}, {"2", "3", "4"}, {"3", "4", "5"}};


            }
        } catch (IOException e) {
            System.out.println("На ввод принимаются только 0 или 1");
        } finally {
        }

        System.out.println(arraySumm());
    }





    public int arraySumm(String[][] stringArray) {
        int summ = 0;



        return summ;
    }
}
