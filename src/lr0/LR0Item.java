package lr0;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
import utilities.Rule;

import java.util.Arrays;
import java.util.Objects;
//LR0 Item class is used to manage . Pointer
// it is extend with rule class because we get left and right side from this class
public class LR0Item extends Rule {

    protected int dotPointer;
    
    // this constructor is called in Lr0Parser and Lr0State
    public LR0Item(Rule r) {
        super(r.getLeftSide(), r.getRightSide());
        this.dotPointer = 0;
    }

    public LR0Item(String leftSide, String[] rightSide, int dotPointer) {
        super(leftSide, rightSide);
        this.dotPointer = dotPointer;
    }
//this constructor use in LR0Parser class
    public LR0Item(LR0Item item) {
        super(item);
        dotPointer = item.getDotPointer();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.dotPointer;
        hash = 89 * hash + Objects.hashCode(this.leftSide);
        hash = 89 * hash + Arrays.deepHashCode(this.rightSide);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LR0Item other = (LR0Item) obj;
        if (this.dotPointer != other.dotPointer) {
            return false;
        }
        if (!this.leftSide.equals(other.leftSide)) {
            return false;
        }
        if (!Arrays.equals(this.rightSide, other.rightSide)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String str = leftSide + " -> ";
        for (int i = 0; i < rightSide.length; i++) {
            if (i == dotPointer) {
                str += ".";
            }
            str += rightSide[i];
            if(i != rightSide.length - 1){
                str+= " ";
            }
        }
        if (rightSide.length == dotPointer) {
                str += ".";
        }
        return str;
    }
    

    public int getDotPointer() {
        return dotPointer;
    }
// this is used in LR0Parser class
    boolean goTo() {
        if (dotPointer >= rightSide.length) {
            return false;
        }
        dotPointer++;
        return true;
    }
// this is use in LR0Parser 
    String getCurrentTerminal() {
        if(dotPointer == rightSide.length){
            return null;
        }
        return rightSide[dotPointer];
    }

}
