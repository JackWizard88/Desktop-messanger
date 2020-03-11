package Lesson2;

import Lesson2.MyExceptions.MyArrayDataException;
import Lesson2.MyExceptions.MyArraySizeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        String[][] array = createArray();
        try {
            System.out.println(arraySumm(array));
        } catch (MyArraySizeException | MyArrayDataException e) {
            System.out.println(e.getMessage());
        }
    }

    //метод для создания массива вручную или тестовым генератором

    private static String[][] createArray() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String generateArrError = "Введите \"1\" для создания и заполнения массива вручную. \n" +
                "Введите \"0\" чтобы сгенерировать тестовый массив размерностью int[1...6][1...6] наполненный случайными числами от 0 до 100. ";
        System.out.println(generateArrError);
        int k = 0,l = 0; // размерности массива
        String[][] array = null;

        while (array == null) {
            try {
                int x = Integer.parseInt(reader.readLine());

                switch (x) {
                    case (1):
                        while (array == null) {
                            try {
                                System.out.println("Введите размерность массива (два числа через пробел)");
                                System.out.println("Для корректной работы размеры массива не должны превышать 4х4");
                                String[] size = reader.readLine().split(" ");
                                k = Integer.parseInt(size[0]);
                                if (k < 1) throw new NumberFormatException();
                                l = Integer.parseInt(size[1]);
                                if (l < 1) throw new NumberFormatException();
                                array = new String[k][l];
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (NumberFormatException e) {
                                System.out.println("Указанны неверные размерности массива.");
                            }
                        }

                        for (int i = 0; i < k; i++) {
                            for (int j = 0; j < l; j++) {
                                System.out.printf("Элемент %s,%s ", i, j);
                                array[i][j] = (reader.readLine());
                            }
                        }
                        break;
                    case (0):
                        Random random = new Random();
                        k = random.nextInt(5) + 1;
                        l = random.nextInt(5) + 1;
                        array = new String[k][l];
                        for (int i = 0; i < array.length; i++) {
                            for (int j = 0; j < array[i].length; j++) {
                                array[i][j] = ("" + random.nextInt(100));
                            }
                        }
                        System.out.println("Сгенерирован случайный массив: ");
                        printArray(array);
                        break;
                    default: throw new NumberFormatException();
                }
            } catch (IOException | NumberFormatException e) {
                System.out.println(generateArrError);
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    //метод для вывода массива
    public static void printArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    //метод для суммирования всех элементов массива в случае валидности
    public static String arraySumm(String[][] stringArray) throws MyArraySizeException, MyArrayDataException {
        int summ = 0;
        if (stringArray.length != 4) throw new MyArraySizeException();  //исключение если столбец больше 4

        for (int i = 0; i < stringArray.length; i++) {
                if (stringArray[i].length != 4) throw new MyArraySizeException();  //исключение если стока больше 4
                for (int j = 0; j < stringArray[i].length; j++) {

                    try {
                        summ += Integer.parseInt(stringArray[i][j]);
                    } catch (NumberFormatException | NullPointerException e) {
                        throw new MyArrayDataException(i, j);  //кидаем исключение если элемент имеет неверный формат
                    }
                }
            }

        return "Сумма всех элементов массива = " + summ;
    }
}
