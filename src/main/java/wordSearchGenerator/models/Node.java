package wordSearchGenerator.models; /**
 * Created by chrisjluc on 2014-10-16.
 */
public class Node {
    private char letter = '0';

    public Node(){}

    public boolean isEmpty(){
        if(letter == '0')
            return true;
        return false;
    }

    public char getLetter(){
        return letter;
    }

    public void setLetter(char letter){
        this.letter = letter;
    }

}
