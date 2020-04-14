package J3_L6;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class OnlyOneFourArrayTest {

    private static OnlyOneFourArray hasOnlyOneAndFour;
    private final int[] array;
    private final boolean result;

    public OnlyOneFourArrayTest(int[] array, boolean result) {
        this.array = array;
        this.result = result;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> testCollection() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,4,1,4,1,4,1,4,1,4,1,1}, true},
                {new int[]{1,1,1}, false},
                {new int[]{4,4,4,4,4,4}, false},
                {new int[]{4,1,4,1,5,4}, false},
                {new int[]{1,1,6,4,1,9,1,1,4}, false},
        });
    }

    @Before
    public void init(){
        hasOnlyOneAndFour = new OnlyOneFourArray();
    }

    @Test
    public void hasOnlyOnesAndFoursTest(){
        Assert.assertEquals(result, hasOnlyOneAndFour.hasOnlyOneAndFour(array));
    }

    @After
    public void clear() {
        hasOnlyOneAndFour = null;
    }

}
