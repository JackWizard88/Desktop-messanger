package Lesson2.MyExceptions;

public class MyArraySizeException extends RuntimeException {

    @Override
    public String getMessage() {
        return "Размер передаваемого массива должен быть менее 4х4." ;
    }
}
