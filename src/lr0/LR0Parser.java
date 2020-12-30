package lr0;
/**
 *
 * @author Usama Sarwar <UsamaSarwarOfficial@gmail.com>
 * @version v1.0.0
 * @since 2020
 *
 */
import utilities.LRParser;
import utilities.ActionType;
import utilities.Action;
import utilities.Rule;
import utilities.Grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class LR0Parser extends LRParser {
    
    private ArrayList<LR0State> canonicalCollection;
    //constructor 
    public LR0Parser(Grammar grammar) {
        //this is call from LRParser Class
        super(grammar);
        System.out.println("LR0Parser Constructor");
    }
    
   
// it will return true or false
    public boolean parserLR0() {
        System.out.println("parserLR0 boolean method");
       createStates();
        createGoToTable();
        return createActionTableForLR0();
    }
    
    protected void createStates() {
        canonicalCollection = new ArrayList<>();
        HashSet<LR0Item> start = new HashSet<>();
        System.out.println("LR0 Rule "+grammar.getRules().get(0));
        //call lroitem class constructor
        start.add(new LR0Item(grammar.getRules().get(0)));
        System.out.println("LR0 Start "+start);
        //call LR0 Class constructor
        LR0State startState = new LR0State(grammar, start);
        canonicalCollection.add(startState);
        
        for (int i = 0; i < canonicalCollection.size(); i++) {
            System.out.println("cannonical collection"+canonicalCollection.size());
            System.out.println("cannonical collection"+canonicalCollection);
            HashSet<String> stringWithDot = new HashSet<>();
            for (LR0Item item : canonicalCollection.get(i).getItems()) {
                System.out.println("cannonical item "+item);
                //it get the current terminal
                if (item.getCurrentTerminal() != null) {
                    System.out.println("cannonical current terminal "+item.getCurrentTerminal());
                    stringWithDot.add(item.getCurrentTerminal());
                }
            }
            for (String str : stringWithDot) {
                System.out.println("cannonical first string "+str);
                HashSet<LR0Item> nextStateItems = new HashSet<>();
                for (LR0Item item : canonicalCollection.get(i).getItems()) {
                    
                    if (item.getCurrentTerminal() != null && item.getCurrentTerminal().equals(str)) {
                        LR0Item temp = new LR0Item(item);
                        //goto function is used to move dot on right side
                        temp.goTo();
                        nextStateItems.add(temp);
                    }
                }
                LR0State nextState = new LR0State(grammar, nextStateItems);
                boolean isExist = false;
                for (int j = 0; j < canonicalCollection.size(); j++) {
                    if (canonicalCollection.get(j).getItems().containsAll(nextState.getItems())
                            && nextState.getItems().containsAll(canonicalCollection.get(j).getItems())) {
                        isExist = true;
                        canonicalCollection.get(i).addTransition(str, canonicalCollection.get(j));
                    }
                }
                if (!isExist) {
                    canonicalCollection.add(nextState);
                    canonicalCollection.get(i).addTransition(str, nextState);
                }
            }
        }
        
    }
    
    @Override
    protected void createGoToTable() {
        // go to table hashmap define in LRParser class
        // create empty hashmap called gototable according to the size of canonicalcollection
        goToTable = new HashMap[canonicalCollection.size()];
        for (int i = 0; i < goToTable.length; i++) {
            goToTable[i] = new HashMap<>();
        }
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                //this is check if canonical collection contain non terminal then it will go to Gototable and add index number
                if (grammar.getVariables().contains(s)) {
                    goToTable[i].put(s, findStateIndex(canonicalCollection.get(i).getTransition().get(s)));
                }
            }
        }
    }
/* this method is used to create action table if table is created successfully 
  it will return true or false    
 */   private boolean createActionTableForLR0() {
         // action table hashmap define in LRParser class
        // create empty hashmap called actiontable according to the size of canonicalcollection
        
        actionTable = new HashMap[canonicalCollection.size()];
        
        for (int i = 0; i < goToTable.length; i++) {
            actionTable[i] = new HashMap<>();
        }
        // this method is used to check action type is shift or not
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (String s : canonicalCollection.get(i).getTransition().keySet()) {
                if (grammar.getTerminals().contains(s)) {
                    actionTable[i].put(s, new Action(ActionType.SHIFT, findStateIndex(canonicalCollection.get(i).getTransition().get(s))));
                  
                }
            }
        }
          // this method is used to check action type is accept or not
        for (int i = 0; i < canonicalCollection.size(); i++) {
            for (LR0Item item : canonicalCollection.get(i).getItems()) {
                if (item.getDotPointer() == item.getRightSide().length) {
                    if (item.getLeftSide().equals("S'")) {
                        actionTable[i].put("$", new Action(ActionType.ACCEPT, 0));
                    } else {
                          // this method is used to check action type is reduce or not
                        HashSet<String> terminals = grammar.getTerminals();
                        terminals.add("$");
                        Rule rule = new Rule(item.getLeftSide(), item.getRightSide().clone());
                        int index = grammar.findRuleIndex(rule);
                        Action action = new Action(ActionType.REDUCE, index);
                        for (String str : terminals) {
                            if (actionTable[i].get(str) != null) {
                                System.out.println("it has a REDUCE-" + actionTable[i].get(str).getType() + " confilct in state " + i);
                                return false;
                            } else {
                                actionTable[i].put(str, action);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private int findStateIndex(LR0State state) {
        for (int i = 0; i < canonicalCollection.size(); i++) {
            if (canonicalCollection.get(i).equals(state)) {
                return i;
            }
        }
        return -1;
    }
// this method is for canonical collection
    public String canonicalCollectionStr() {
        String str = "Canonical Collection : \n";
        for (int i = 0; i < canonicalCollection.size(); i++) {
            str += "State " + i + " : \n";
            str += canonicalCollection.get(i)+"\n";
        }
        return str;
    }
    
}
