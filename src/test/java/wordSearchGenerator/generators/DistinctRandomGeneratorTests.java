package wordSearchGenerator.generators;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DistinctRandomGeneratorTests{

    @Test
    public void test0(){
        DistinctRandomGenerator drg = new DistinctRandomGenerator(0);
        assertEquals(null, drg.next());
        assertEquals(null, drg.next());
    }

    @Test
    public void test1(){
        DistinctRandomGenerator drg = new DistinctRandomGenerator(1);
        assertTrue(((Integer) drg.next()) < 1);
        assertEquals(null, drg.next());
    }

    @Test
    public void test10(){
        DistinctRandomGenerator drg = new DistinctRandomGenerator(10);
        int expectedSum = 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9;
        int actualSum = 0;
        Integer element = (Integer) drg.next();
        do{
            actualSum += element;
            element = (Integer) drg.next();
        }while(element != null);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void test1String(){
        DistinctRandomGenerator drg = new DistinctRandomGenerator(new String[]{"hi"});
        assertEquals("hi", drg.next());
        assertEquals(null, drg.next());
    }

    @Test
    public void test3String(){
        String[] strings = new String[]{"hi","okay","Alright"};
        List<String> stringList = Arrays.asList(Arrays.copyOfRange(strings,0,strings.length));
        DistinctRandomGenerator drg = new DistinctRandomGenerator(strings);

        Object element = drg.next();
        do{
            assertTrue(stringList.contains(element));
            element = drg.next();
        }while(element != null);
    }

    @Test
    public void testListString(){
        String[] strings = new String[]{"hi","okay","Alright"};
        List<String> stringList = Arrays.asList(Arrays.copyOfRange(strings,0,strings.length));
        DistinctRandomGenerator drg = new DistinctRandomGenerator(stringList);

        Object element = drg.next();
        do{
            assertTrue(stringList.contains(element));
            element = drg.next();
        }while(element != null);
    }

}
