package J3_L7;

public class TestClass3 {

    @Beforesuite
    public void init() {
        System.out.println("initializing test");
    }

    @Test(priority = 2)
    public void test1() {
        System.out.println("Test1 done");
    }

    @Test(priority = 3)
    public void test2() {
        System.out.println("Test2 done");
    }

    @Test(priority = 1)
    public void test3() {
        System.out.println("Test3 done");
    }

    @Test(priority = 6)
    public void test4() {
        System.out.println("Test4 done");
    }

    @Test(priority = 7)
    public void test5() {
        System.out.println("Test5 done");
    }

    @Test(priority = 6)
    public void test6() {
        System.out.println("Test6 done");
    }

    @AfterSuite
    public void clear() {
        System.out.println("clearing test result");
    }

    @AfterSuite
    public void clear2() {
        System.out.println("clearing test result2");
    }
}
