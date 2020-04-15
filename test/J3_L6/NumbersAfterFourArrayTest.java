package J3_L6;

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class NumbersAfterFourArrayTest {

    private static NumbersAfterFourArray numbersAfterFourArray;
    private int[] arr;
    private int[] result;
    private boolean runtimeException;

    public NumbersAfterFourArrayTest(int[] arr, int[] result, boolean expectRuntimeException) {
        this.arr = arr;
        this.result = result;
        this.runtimeException = expectRuntimeException;
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> testCollection() {
        return Arrays.asList(new Object[][] {
                {new int[]{1,2,4,5,6}, new int[]{5,6}, false},
                {new int[]{2,5,5,3,6}, null, true},
                {new int[]{5,2,5,7,4}, new int[]{}, false},
                {new int[]{4,1,3,5,6,8}, new int[]{1,3,5,6,8}, false}
        });
    }

    @Before
    public void init() {
        numbersAfterFourArray = new NumbersAfterFourArray();
    }

    @Test
    public void numbersAfterFourArrayTest() {
        if (runtimeException) exception.expect(RuntimeException.class);
        Assert.assertArrayEquals(result, numbersAfterFourArray.numbersAfterFourArray(arr));
    }

    @After
    public void clear() {
        numbersAfterFourArray = null;
    }

}
