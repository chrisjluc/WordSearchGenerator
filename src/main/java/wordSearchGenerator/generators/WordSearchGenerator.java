package wordSearchGenerator.generators;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import wordSearchGenerator.models.Node;
import wordSearchGenerator.models.Point;
import wordSearchGenerator.models.PossibleInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordSearchGenerator {
    private List<Character> mUniqueChars;
    private String mWord;
    private int nRow;
    private int nCol;
    private Node[][] mWordSearch;
    private Random mRandom = new Random();

    private Point mRandomPoint;
    private Integer mRandomOrientation;

    private int mBuildFailures = 0;

    public static final int RIGHT = 0, RIGHTDOWN = 1, DOWN = 2, LEFTDOWN = 3, LEFT = 4, LEFTUP = 5, UP = 6, RIGHTUP = 7;

    public WordSearchGenerator(int nRow, int nCol, String mWord) {
        this.nRow = nRow;
        this.nCol = nCol;
        this.mWord = mWord;

        this.mWordSearch = new Node[nRow][nCol];
        initializeWordSearch();
    }

    public void build() {
        mUniqueChars = StringUtils.getDistinctCharacters(mWord);
        while (true) {
            try {
                insertWord();
                fillWordSearch();
                break;
            } catch (Exception e) {
                initializeWordSearch();
                mBuildFailures++;
            }
        }
    }

    public void print() {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                System.out.print(mWordSearch[i][j].getLetter());
            }
            System.out.println();
        }
        System.out.println();

    }

    public List<Point> getStartAndEndPointOfWord() {
        List<Point> points = new ArrayList<Point>();
        points.add(mRandomPoint);
        Point endPoint = getRelativePoint(mRandomOrientation, mRandomPoint, mWord.length() - 1);
        points.add(endPoint);
        return points;
    }

    private void initializeWordSearch() {
        for (int i = 0; i < nRow; i++)
            for (int j = 0; j < nCol; j++)
                mWordSearch[i][j] = new Node();
    }

// -------------- LETTER INSERTION ALGORITHM METHODS --------------------//

    /**
     * Manages general loop logic for letter insertion into the word search
     *
     * @throws Exception
     */
    private void fillWordSearch() throws Exception {
        for (int i = 0; i < nRow; i++) {
            for (int j = 0; j < nCol; j++) {
                Node n = mWordSearch[i][j];
                // Word isn't empty if that letter is part of the inserted word to be found
                if (!n.isEmpty()) continue;
                Point currentPoint = new Point(i, j);
                DistinctRandomGenerator charGenerator = new DistinctRandomGenerator(mUniqueChars);
                // Generate random character from unique chars
                Character candidate = (Character) charGenerator.next();
                while (candidate != null) {

                    // If the first or last character, continue
                    if (candidate == mWord.charAt(0) || candidate == mWord.charAt(mWord.length() - 1)) {
                        if (isAllOrientationsValid(candidate, currentPoint))
                            break;
                    } else if (arePossibleInstancesSatisfied(candidate, currentPoint)) break;

                    // Characters violate constraints, get the next unique character
                    candidate = (Character) charGenerator.next();

                    // No character satisfies the constraints
                    if (candidate == null) throw new Exception("Random Char generator returned null");
                }
                n.setLetter(candidate);
            }
        }
    }

    private boolean isAllOrientationsValid(char candidate, Point p) {
        int nOrientations = 8;
        for (int i = 0; i < nOrientations; i++) {
            String s = getStringByOrientation(i, p);
            if (s == null) continue;
            s = "" + candidate + s;

            if (mWord.equals(s) || mWord.equals(StringUtils.reverse(s))) return false;

            MutableBoolean isReversed = new MutableBoolean(false);
            MutableInt position = new MutableInt(-1);

            if (isChanceOfPossibleInstance(s, position, isReversed))
                validateAndSetPossibleInstance(p, position.intValue(), i, isReversed.toBoolean());

        }
        return true;
    }

    /**
     * Detects if there is a chance we can have another instance of a string
     * Ex. If word is easy
     * and we find a string such as ea0y, e0sy or e00y
     * We want to create a flag which tells the generator
     * there is a chance of creating another instance of that word
     * <p/>
     * e000 there is no possible instance needed because will check the 4th position
     * if there is a last character and invalidate it
     *
     * @param s
     * @param position   index of first '0', we will set this node to have a possible instance
     * @param isReversed
     * @return if there is a possible instance
     */
    private boolean isChanceOfPossibleInstance(String s, MutableInt position, MutableBoolean isReversed) {
        if (s.length() != mWord.length()) return false;

        // If all but 1 character is '0' no possible chance because we check all orientation when at first or last character
        // also if there are no '0' there can't be a chance because no open spaces in to insert a character
        int matches0 = org.apache.commons.lang3.StringUtils.countMatches(s, "0");
        if (s.length() - matches0 == 1 || matches0 == 0) return false;

        int countMatching = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == mWord.charAt(i))
                countMatching++;
            else if (position.intValue() == -1)
                position.setValue(i);
        }
        if (countMatching > 1)
            return true;

        // Compare with the reverse and see if there's a chance
        countMatching = 0;
        position.setValue(-1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == mWord.charAt(s.length() - 1 - i))
                countMatching++;
            else if (position.intValue() == -1)
                position.setValue(i);
        }
        if (countMatching > 1) {
            isReversed.setTrue();
            return true;
        }
        return false;
    }

    /**
     * Next node which is current point incremented by position by relative orientation
     * holds the possibleInstance
     *
     * @param point
     * @param position    to set in possible instance
     * @param orientation
     * @return
     */
    private void validateAndSetPossibleInstance(Point point, int position, int orientation, boolean isReversed) {
        // Will be covered by other IsAllOrientationsValid
        if (position >= mWord.length() - 1) return;

        Point relativePoint = getRelativePoint(orientation, point, position);
        Node relativeNode = mWordSearch[relativePoint.x][relativePoint.y];

        // There exists a character already, can't write a character here
        if (!relativeNode.isEmpty()) return;

        PossibleInstance pi = new PossibleInstance(orientation, isReversed, position);
        relativeNode.addToPossibleInstances(pi);
    }

    private void extendPossibleInstance(Point point, int delta, PossibleInstance pi) {
        Point relativePoint = getRelativePoint(pi.orientation, point, delta + pi.positionInWord);
        Node relativeNode = mWordSearch[relativePoint.x][relativePoint.y];
        if (!relativeNode.isEmpty()) return;
        PossibleInstance relativePi = new PossibleInstance(pi.orientation, pi.reversed, delta + pi.positionInWord);
        relativeNode.addToPossibleInstances(relativePi);
    }

    private boolean arePossibleInstancesSatisfied(char candidate, Point p) {

        Node current = mWordSearch[p.x][p.y];
        List<PossibleInstance> possibleInstances = current.getPossibleInstances();
        if (possibleInstances == null) return true;
        int wordLength = mWord.length();
        for (PossibleInstance pi : possibleInstances) {

            // Continue if candidate isn't the character at the position in the Word
            if ((!pi.reversed && mWord.charAt(pi.positionInWord) == candidate) || (pi.reversed && mWord.charAt(wordLength - 1 - pi.positionInWord) == candidate)) {
            } else
                continue;

            if (pi.positionInWord == wordLength - 1)
                return false;

            Point nextPoint = getRelativePoint(pi.orientation, p, 1);
            Node next = mWordSearch[nextPoint.x][nextPoint.y];

            // Next character is empty and isn't the end of the word, if it's the end of the word
            // it would be checked by isAllOrientations Valid
            if (next.isEmpty() && pi.positionInWord < wordLength - 1) {

                // The next character is the end of the Word
                if (pi.positionInWord >= wordLength - 2)
                    continue;

                extendPossibleInstance(p, 1, pi);

                // Next letter isn't empty but isn't the next character in the word.
                // Th string won't match the word anymore, so current letter and possibleInstance is valid, continue on
            } else if ((!pi.reversed && next.getLetter() != mWord.charAt(pi.positionInWord + 1)) || (pi.reversed && next.getLetter() != mWord.charAt(wordLength - 2 - pi.positionInWord))) {


                // Next letter is equal to the next character in the word and the character after that is empty.
                // If that position 2 over is not the end of the letter, create a possible instance there.
            } else if ((!pi.reversed && next.getLetter() == mWord.charAt(pi.positionInWord + 1)) || (pi.reversed && next.getLetter() == mWord.charAt(wordLength - 2 - pi.positionInWord))) {

                // The next character is the end of the word
                if (pi.positionInWord >= wordLength - 2)
                    return false;

                // The next next character will be the end of the word
                if (pi.positionInWord >= wordLength - 3)
                    continue;

                Point nextNextPoint = getRelativePoint(pi.orientation, nextPoint, 1);
                Node nextNext = mWordSearch[nextNextPoint.x][nextNextPoint.y];

                if (nextNext.isEmpty())
                    extendPossibleInstance(nextPoint, 2, pi);
            }
        }
        current.clearPossibleInstances();
        return true;
    }


// -------- INSERTING THE SINGLE INSTANCE OF THE WORD METHODS -------------//

    private void insertWord() throws Exception {

        // Choose random coordinate and orientation
        DistinctRandomGenerator rOrientation = new DistinctRandomGenerator(8);
        mRandomPoint = new Point(mRandom.nextInt(nCol), mRandom.nextInt(nRow));

        mRandomOrientation = (Integer) rOrientation.next();
        while (!isValidOrientationForInsertion(mRandomOrientation, mRandomPoint)) {
            mRandomOrientation = (Integer) rOrientation.next();
            if (mRandomOrientation == null)
                throw new Exception("Random orientation generator returned null");
        }
        insertWordWithOrientation(mRandomOrientation, mRandomPoint);
    }

    private void insertWordWithOrientation(int orientation, Point p) {
        for (int i = 0; i < mWord.length(); i++) {
            Point relative = getRelativePoint(orientation, p, i);
            mWordSearch[relative.x][relative.y].setLetter(mWord.charAt(i));
        }
    }

// -------- ORIENTATION METHODS -------------//


    private String getStringByOrientation(int orientation, Point p) {
        switch (orientation) {
            case RIGHT:
                return lookRight(p, mWord.length() - 1);

            case RIGHTDOWN:
                return lookRightDown(p, mWord.length() - 1);

            case DOWN:
                return lookDown(p, mWord.length() - 1);

            case LEFTDOWN:
                return lookLeftDown(p, mWord.length() - 1);

            case LEFT:
                return lookLeft(p, mWord.length() - 1);

            case LEFTUP:
                return lookLeftUp(p, mWord.length() - 1);

            case UP:
                return lookUp(p, mWord.length() - 1);

            case RIGHTUP:
                return lookRightUp(p, mWord.length() - 1);

            default:
                return null;
        }
    }

    public static Point getRelativePoint(int orientation, Point p, int d) {
        switch (orientation) {
            case RIGHT:
                return new Point(p.x + d, p.y);

            case RIGHTDOWN:
                return new Point(p.x + d, p.y + d);

            case DOWN:
                return new Point(p.x, p.y + d);

            case LEFTDOWN:
                return new Point(p.x - d, p.y + d);

            case LEFT:
                return new Point(p.x - d, p.y);

            case LEFTUP:
                return new Point(p.x - d, p.y - d);

            case UP:
                return new Point(p.x, p.y - d);

            case RIGHTUP:
                return new Point(p.x + d, p.y - d);

            default:
                return null;
        }
    }

    private boolean isValidOrientationForInsertion(int orientation, Point p) {
        return getStringByOrientation(orientation, p) != null;
    }


    private String lookRight(Point p, int d) {
        if (p.x + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x + i][p.y].getLetter();
        }
        return ret;
    }

    private String lookRightDown(Point p, int d) {
        if (p.x + d >= nCol || p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x + i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookDown(Point p, int d) {
        if (p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeftDown(Point p, int d) {
        if (p.x - d < 0 || p.y + d >= nCol)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x - i][p.y + i].getLetter();
        }
        return ret;
    }

    private String lookLeft(Point p, int d) {
        if (p.x - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x - i][p.y].getLetter();
        }
        return ret;
    }

    private String lookLeftUp(Point p, int d) {
        if (p.x - d < 0 || p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x - i][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookUp(Point p, int d) {
        if (p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x][p.y - i].getLetter();
        }
        return ret;
    }

    private String lookRightUp(Point p, int d) {
        if (p.x + d >= nCol || p.y - d < 0)
            return null;
        String ret = "";
        for (int i = 1; i <= d; i++) {
            ret += mWordSearch[p.x + i][p.y - i].getLetter();
        }
        return ret;
    }
}