package wordSearchGenerator.generators;

import org.junit.Test;
import wordSearchGenerator.models.Point;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by chrisjluc on 2014-10-17.
 */
public class WordSearchGeneratorTest {

    @Test
    public void testUniqueCharactersAllUnique(){
        List<Character> c = WordSearchGenerator.getDistinctCharacters("hela");
        assertEquals(4, c.size());
        assertTrue(c.contains('h'));
        assertTrue(c.contains('e'));
        assertTrue(c.contains('l'));
        assertTrue(c.contains('a'));
    }

    @Test
    public void testUniqueCharactersSomeUnique(){
        List<Character> c = WordSearchGenerator.getDistinctCharacters("hhelaaaaaaa");
        assertEquals(4, c.size());
        assertTrue(c.contains('h'));
        assertTrue(c.contains('e'));
        assertTrue(c.contains('l'));
        assertTrue(c.contains('a'));
    }

    @Test
    public void testReverse(){
        assertEquals(";lkjfdsa", WordSearchGenerator.reverse("asdfjkl;"));
    }

    @Test
    public void testRelativePointLeft(){
        Point p =  WordSearchGenerator.getRelativePoint(WordSearchGenerator.LEFT, new Point(1,2), 1);
        assertEquals(new Point(0,2), p);
    }

    @Test
    public void testRelativePointRightUp(){
        Point p =  WordSearchGenerator.getRelativePoint(WordSearchGenerator.RIGHTUP, new Point(0,0), 5);
        assertEquals(new Point(5,-5), p);
    }

    @Test
    public void testBuildWordSearchSmall(){
        WordSearchGenerator w = new WordSearchGenerator(5,5,"okay");
        w.build();
        w.print();
    }

    @Test
    public void testBuildWordSearchMedium(){
        WordSearchGenerator w = new WordSearchGenerator(500,500,"bigger");
        w.build();
    }

    @Test
    public void testBuildWordSearchLarge(){
        WordSearchGenerator w = new WordSearchGenerator(1000,1000,"verylargewordsearch");
        w.build();
    }
}
