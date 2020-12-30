package compiler_construction;

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

    public Rule(String leftSide, String[] rightSide) {
        this.leftSide = leftSide;
        System.out.println("left side "+leftSide);
        this.rightSide = rightSide;
        for (int i = 0; i < this.rightSide.length; i++) {
        System.out.println("right side ..................++"+this.rightSide[i]);
        }
        
    }

    public Rule(Rule rule) {
        this.leftSide = rule.getLeftSide();
        this.rightSide = rule.getRightSide().clone();
        System.out.println("rule constructor is called rule constructor is called");
    }
    // call this function in grammer firstset
    public String getLeftSide() {
        return leftSide;
    }
    // call this function in grammer firstset
    public String[] getRightSide() {
        return rightSide;
    }
    
  

    @Override
    public String toString() {
        String str = leftSide + " -> ";
        for (int i = 0; i < rightSide.length; i++) {
            str += rightSide[i] + " ";
            System.out.println(str);
            
        }
        return str;
    }

}
