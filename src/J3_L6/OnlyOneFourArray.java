package J3_L6;

public class OnlyOneFourArray {

    /**   Написать метод, который проверяет состав массива из чисел 1 и 4.
     *   Если в нем нет хоть одной четверки или единицы, то метод вернет false;
     *   Написать набор тестов для этого метода (по 3-4 варианта входных данных).
     */

    public static boolean hasOnlyOneAndFour(int[] arr) {

        boolean flagFour = false;
        boolean flagOnes = false;
        if (arr != null) {
            for (int element : arr) {
                if (element != 1 && element != 4) return false;
                else if (element == 1) flagOnes = true;
                else if (element == 4) flagFour = true;
            }
        }
        return (flagFour && flagOnes);
    }
}
