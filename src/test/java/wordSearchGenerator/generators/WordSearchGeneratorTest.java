package wordSearchGenerator.generators;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

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
    public void testBuildWordSearchSmall(){
        WordSearchGenerator w = new WordSearchGenerator(30,20,"run");
        w.build();
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
