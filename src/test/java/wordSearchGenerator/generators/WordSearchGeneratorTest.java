package wordSearchGenerator.generators;

import org.junit.Test;
import wordSearchGenerator.models.FillType;
import wordSearchGenerator.models.Point;

import static org.junit.Assert.assertEquals;

public class WordSearchGeneratorTest {

    @Test
    public void testReverse(){
        assertEquals(";lkjfdsa", StringUtils.reverse("asdfjkl;"));
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
    public void testBuildWordSearch1(){
        WordSearchGenerator w = new WordSearchGenerator(7,7,"hey", FillType.CharactersOfTheWord);
        w.build();
        w.print();
    }

    @Test
    public void testBuildWordSearch2(){
        WordSearchGenerator w = new WordSearchGenerator(7,7,"time", FillType.RandomCharacters);
        w.build();
        w.print();
    }

    @Test
    public void testBuildWordSearch3(){
        WordSearchGenerator w = new WordSearchGenerator(15,15,"timewatch", FillType.CharactersOfTheWord);
        w.build();
        w.print();
    }

    @Test
    public void testBuildWordSearch4(){
        WordSearchGenerator w = new WordSearchGenerator(500,500,"bigger", FillType.RandomCharacters);
        w.build();
    }

    @Test
    public void testBuildWordSearch5(){
        WordSearchGenerator w = new WordSearchGenerator(1000,1000,"verylargewordsearch", FillType.CharactersOfTheWord);
        w.build();
    }
}
