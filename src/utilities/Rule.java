package utilities;

import java.util.Arrays;
import java.util.Objects;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class Rule {

    protected String leftSide;
    protected String[] rightSide;
    //this constructor call in LR0item
    public Rule(String leftSide, String[] rightSide) {
        this.rightSide = rightSide;
        this.leftSide = leftSide;
    }
// this is call in LR0item class
    public Rule(Rule rule) {
        this.leftSide = rule.getLeftSide();
        this.rightSide = rule.rightSide.clone();
    }
    
    public String getLeftSide() {
        return leftSide;
    }

    public String[] getRightSide() {
        return rightSide;
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Objects.hashCode(this.leftSide);
        hash = 29 * hash + Arrays.deepHashCode(this.rightSide);
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
        final Rule other = (Rule) obj;
        if (!Objects.equals(this.leftSide, other.leftSide)) {
            return false;
        }
        if (!Arrays.deepEquals(this.rightSide, other.rightSide)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        String str = leftSide + " -> ";
        for (int i = 0; i < rightSide.length; i++) {
            str += rightSide[i] + " ";
        }
        return str;
    }

}
