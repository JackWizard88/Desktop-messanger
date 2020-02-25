package Lesson2.MyExceptions;

public class MyArrayDataException extends RuntimeException {
    private int indexI;
    private int indexJ;

    public MyArrayDataException (int i, int j) {
        indexI = i;
        indexJ = j;
    }

    @Override
    public String getMessage() {
        return "В ячейке (" + indexI + ", " + indexJ + ") находится неверный элемент. Вычисление суммы элементов невозможно..." ;
    }
}
