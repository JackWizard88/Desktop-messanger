package J3_L7;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {

    private static int counter = 1;
    private static final String separator = "---------------------";

    public static void main(String[] args) {

        Class clazz = TestClass.class;
        Class clazz2 = TestClass2.class;
        Class clazz3 = TestClass3.class;

        start(clazz);
        System.out.println(separator);
        start(clazz2);
        System.out.println(separator);
        start(clazz3);

    }

    private static void start(Class clazz) {
        try {
            test(clazz);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void test(Class clazz) throws RuntimeException {
        System.out.println("Test #" + counter++);
        Object testObj = null;
        try {
            testObj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        Method[] methods = clazz.getDeclaredMethods();
        ArrayList<Method> arr = new ArrayList();
        Method beforeSuite = null;
        Method afterSuite = null;

        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                arr.add(method);
            }
            if (method.isAnnotationPresent(Beforesuite.class)) {
                if (beforeSuite == null) beforeSuite = method;
                else throw new RuntimeException("RuntimeException: More than one BeforeSuite");
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                if (afterSuite == null) afterSuite = method;
                else throw new RuntimeException("RuntimeException: More than one AfterSuite");
            }
        }

        arr.sort(Comparator.comparingInt(m -> m.getAnnotation(Test.class).priority()));

        try {
            if (beforeSuite != null) beforeSuite.invoke(testObj);
            for (Method method : arr) method.invoke(testObj);
            if (afterSuite != null) afterSuite.invoke(testObj);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
