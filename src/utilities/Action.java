package utilities;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
public class Action {
    private ActionType type;
    private int operand;

    public Action(ActionType type, int operand) {
        this.type = type;
        this.operand = operand;
    }

    @Override
    public String toString() {
        return type + " " + (type == ActionType.ACCEPT ? "":operand);
    }
//getter methods
    public ActionType getType() {
        return type;
    }
//getter methods
    public int getOperand() {
        return operand;
    }
    
}
