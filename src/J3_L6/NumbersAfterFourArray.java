package J3_L6;

import java.util.Arrays;

public class NumbersAfterFourArray {

    /**    Написать метод, которому в качестве аргумента передается не пустой одномерный целочисленный массив.
     *   Метод должен вернуть новый массив, который получен путем вытаскивания из исходного массива элементов,
     *   идущих после последней четверки. Входной массив должен содержать хотя бы одну четверку, иначе в методе
     *    необходимо выбросить RuntimeException. Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     *    Вх: [ 1 2 4 4 2 3 4 1 7 ] -> вых: [ 1 7 ].
     */

    public static int[] numbersAfterFourArray(int[] arr) {

        for (int i = arr.length - 1; i >= 0; i--) {
            if (arr[i] == 4) {
                return Arrays.copyOfRange(arr, i + 1, arr.length);
            }
        }
        throw new RuntimeException();
    }
}
